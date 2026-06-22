# RocksDB Configuration Reference

A guide to **every RocksDB option available to us**, what each one does, where it gets set,
what it defaults to, whether we use it, what we set it to, and why. 

> RocksDB is a C++ library used from Java via JNI, so most of its memory lives **outside** 
> the JVM heap and is thus invisible to normal Java tooling.

---

## 1. How to read this document

RocksDB has hundreds of settings. They are split into a few **option groups**, and *which group a
setting belongs to* matters.

| Option group | Java class | Scope | Set once per… |
|---|---|---|---|
| **Database options** | `DBOptions` (or `Options`) | The whole database | DB open |
| **Column family options** | `ColumnFamilyOptions` (or `Options`) | One column family (a named key/value namespace) | Each column family |
| **Block-based table options** | `BlockBasedTableConfig` | The on-disk SST file format + block cache | Lives inside column family options |
| **Read options** | `ReadOptions` | A single read / iterator | Per read |
| **Write options** | `WriteOptions` | A single write | Per write |
| **Transaction options** | `TransactionDBOptions`, `TransactionOptions` | Transactions | DB open / per transaction |

A note on `Options`: the Java class `org.rocksdb.Options` is a convenience class that is **both**
`DBOptions` and `ColumnFamilyOptions` at once. That convenience is exactly what trips us up — see §3.

Each catalog table below uses these columns:
- **Option** — the RocksDB (C++) name.
- **Java setter** — the method we call from Java.
- **Default** — the value RocksDB uses if we set nothing.
- **What it does** — a quick description.
- **Mem?** — does it affect native memory? (Yes/No)
- **Ours** — what *we* set it to, or `—` if we leave it at the default. `⚠ dropped` means we
  *try* to set it, but it never takes effect (see §3).

---

## 2. How RocksDB uses memory

Fundamentally speaking, a RocksDB database is an **LSM-tree**. 
Writes land in an in-memory **memtable**; when a memtable fills up it is flushed to 
an immutable on-disk **SST file**; background **compaction** merges SST files over time. 
Reads check the memtables, then SST files, using per-file **index** and **bloom
filter** blocks to avoid unnecessary disk reads. A **block cache** holds recently used blocks in
memory.

The native (non-JVM-heap) memory RocksDB uses is therefore, roughly:

```
  memtables (write buffers)        <- write_buffer_size × max_write_buffer_number × #column families
+ block cache                      <- block_cache capacity (data blocks, + index/filter if configured)
+ table reader memory              <- index + filter blocks of every OPEN SST file (if not in the cache)
+ iterator / pinned blocks         <- blocks held by open iterators and pinned settings
+ misc (WAL buffers, stats, …)
```

The four settings that matter most for our memory issues, and where they live:

| Lever | Option | Group | Default | Effect if left default |
|---|---|---|---|---|
| Cap total memtable memory | `db_write_buffer_size` / `WriteBufferManager` | DB | `0` (uncapped) | Memtable memory grows with the number of column families |
| Bound open SST files | `max_open_files` | DB | `-1` (unlimited) | Every SST stays open; its index+filter stays resident **forever** |
| Put index/filter in the (bounded) cache | `cache_index_and_filter_blocks` | Table | `false` | Index/filter live in unbounded table-reader memory instead of the capped cache |
| Size the cache | `block_cache` (`LRUCache`) | Table | a small internal 32 MB cache | Memory not centrally bounded/observable |

Keep these four in mind; §6 is a deep-dive on them.

---

## 3. How our code applies configuration 

Both our stores follow the same shape. Simplified from `AbstractRocksDBStorage`:

```java
// 1. Build column-family options (these DO reach the column families)
ColumnFamilyOptions cfOptions = defaultColumnFamilyOptions();
List<ColumnFamilyDescriptor> cfDescriptors = prepareColumnFamilyDescriptors(cfOptions);

// 2. Build "Options" and then derive DBOptions from it
this.options = createDefaultOptions();          // an org.rocksdb.Options
DBOptions dbOptions = new DBOptions(this.options);   // <-- only DB-wide fields are copied here

// 3. Open
db = TransactionDB.open(dbOptions, transactionOptions, dir, cfDescriptors, cfHandles);
```

### 3.1 The footgun: column-family settings on `Options` are silently dropped

`new DBOptions(this.options)` copies **only the database-wide fields** out of an `Options`. Anything
that is really a *column-family* setting — the block-based table config (block size, bloom filter,
`cache_index_and_filter_blocks`, …), `compaction_pri`, compression — is **not** copied, so if you set
it on the `Options` object it is silently discarded. The column families are opened with the
*separate* `cfOptions` from `defaultColumnFamilyOptions()`, so that is the object whose settings
actually reach the column families.

We avoid this by building the `BlockBasedTableConfig` in `createBlockBasedTableConfig()` and
attaching it inside `defaultColumnFamilyOptions()`
(`...setTableFormatConfig(createBlockBasedTableConfig())`). So the 16 KB blocks, bloom filter,
`cache_index_and_filter_blocks`, pinning and shared block cache genuinely take effect. The
`createDefaultOptions()` method even carries a comment warning future maintainers not to put table
config back on the `Options`.

**One residual (harmless).** `compaction_pri` is still set on the `Options`
(`setCompactionPriority(MinOverlappingRatio)`) and is therefore still dropped — but `MinOverlappingRatio`
is already the RocksDB default, so there is no functional difference.

### 3.2 What we actually set, and what survives

| Setting | Group | We call | Reaches RocksDB? | Why we set it |
|---|---|---|---|---|
| `create_if_missing` | DB | `setCreateIfMissing(true)` | ✅ yes | Create the DB on first open |
| `create_missing_column_families` | DB | `setCreateMissingColumnFamilies(true)` | ✅ yes | Create CFs on first open |
| `max_background_jobs` | DB | `setMaxBackgroundJobs(6)` | ✅ yes | More threads for flush/compaction (RocksDB tuning guide) |
| `bytes_per_sync` | DB | `setBytesPerSync(1 MB)` | ✅ yes | Smooth out fsync I/O instead of one big sync |
| **`max_open_files`** | DB | `setMaxOpenFiles(1024)` | ✅ yes | **Bound table-reader (index/filter) memory** instead of leaving every SST open forever |
| **`db_write_buffer_size`** | DB | `setDbWriteBufferSize(256 MB)` | ✅ yes | **Cap total memtable memory** across all column families |
| **`max_total_wal_size`** | DB | `setMaxTotalWalSize(1 GB)` | ✅ yes | Cap total WAL on disk (forces flushes) so storage doesn't balloon |
| **`statistics`** | DB | `setStatistics(...)` @ `EXCEPT_DETAILED_TIMERS` | ✅ yes | **Make memory/IO observable**; reused across reopen so stats stay cumulative |
| `compaction_pri` | **CF** | `setCompactionPriority(MinOverlappingRatio)` | ⚠ dropped* | Intended: pick lowest-write-amp files. *Harmless — `MinOverlappingRatio` is already the default.* |
| **block-based table config** | **CF (table)** | `defaultColumnFamilyOptions().setTableFormatConfig(createBlockBasedTableConfig())` | ✅ **yes (wired correctly)** | 16 KB blocks, bloom filter, cache index/filter, format v5 — see §6 |
| **block cache** | **CF (table)** | `new LRUCache(256 MB)` shared across this store's CFs | ✅ yes | One bounded, observable ceiling for data + index + filter blocks |
| **bloom filter** | **CF (table)** | `new BloomFilter(10.0)` | ✅ yes | ~10 bits/key bloom to skip disk reads on point lookups |
| `level_compaction_dynamic_level_bytes` | CF | on `cfOptions` | ✅ yes | Healthier LSM shape |
| `compression` (LZ4) | CF | on `cfOptions` | ✅ yes | Cheap, fast compression |
| `bottommost_compression` (ZLIB) | CF | on `cfOptions` | ✅ yes | Stronger compression on the biggest, coldest level |
| transaction options | Txn | `new TransactionDBOptions()` | ✅ (defaults) | We rely on defaults |
| read/write options | Read/Write | `new ReadOptions()` / `new WriteOptions()` | ✅ (defaults) | We rely on defaults |
| flush wait | Flush | `setWaitForFlush(true)` | ✅ yes | Block until explicit flushes finish (backup/compact/close) |
| snapshot per write txn | Txn | `transaction.setSnapshot()` | ✅ yes | Consistent read-your-writes within a transaction |

The four memory levers from §2 are all set: `max_open_files = 1024`, `db_write_buffer_size = 256 MB`,
an explicit 256 MB `LRUCache`, and (via the wired table config) `cache_index_and_filter_blocks = true`.
Everything not listed above is left at the RocksDB default.

### 3.3 Column-family count (memory multiplier)

Memtable and table-reader memory is **per column family**. The dictionary store opens
**9 column families** (5 base: default, labels→ids, ids→labels, keys→labels, counters; plus 4
retained for migration). With the default `write_buffer_size` of 64 MB and
`max_write_buffer_number` of 2, the *uncapped* memtable total would be `9 × 64 MB × 2 ≈ 1.1 GB` —
which is exactly why we set `db_write_buffer_size = 256 MB` to cap it.

---

## 4. Database options (`DBOptions`) — full catalog

Set on the `Options`/`DBOptions` object; apply to the whole database. These **do** reach RocksDB
in our code.

| Option | Java setter | Default | What it does | Mem? | Ours |
|---|---|---|---|---|---|
| create_if_missing | setCreateIfMissing | `false` | Create the DB if it doesn't exist on open. | No | `true` |
| create_missing_column_families | setCreateMissingColumnFamilies | `false` | Auto-create missing column families on open. | No | `true` |
| error_if_exists | setErrorIfExists | `false` | Fail to open if the DB already exists. | No | — |
| paranoid_checks | setParanoidChecks | `true` | Extra cheap corruption checks; refuse to open on SST errors. | No | — |
| track_and_verify_wals_in_manifest | setTrackAndVerifyWalsInManifest | `false` | Record/verify WAL numbers & sizes in the MANIFEST. | No | — |
| verify_sst_unique_id_in_manifest | setVerifySstUniqueIdInManifest | `true` | Check each SST's unique ID against the MANIFEST on open. | No | — |
| env | setEnv | `Env::Default()` | The environment used for all file I/O and background scheduling. | No | — |
| rate_limiter | setRateLimiter | `nullptr` | Throttle internal read/write bandwidth (flush/compaction). | No | — |
| sst_file_manager | setSstFileManager | `nullptr` | Track total SST size and throttle file deletion. | No | — |
| info_log | setLogger | `nullptr` | Logger that receives RocksDB progress/error messages. | Yes | — |
| info_log_level | setInfoLogLevel | `kDefaultLogLevel` | Minimum severity of messages sent to the info log. | No | — |
| **max_open_files** | setMaxOpenFiles | `-1` | Max files kept open at once; -1 = keep ALL open (and their index/filter resident). | **Yes** | **`1024`** |
| max_file_opening_threads | setMaxFileOpeningThreads | `16` | Threads used to open files when max_open_files = -1. | No | — |
| **max_total_wal_size** | setMaxTotalWalSize | `0` | Total WAL size that forces flushes; 0 = auto-size from buffers. | **Yes** | **`1 GB`** |
| **statistics** | setStatistics | `nullptr` | Collects detailed DB metrics (small memory + CPU cost). | Yes | **enabled @ `EXCEPT_DETAILED_TIMERS`** |
| use_fsync | setUseFsync | `false` | Use fsync instead of fdatasync for durability. | No | — |
| db_paths | setDbPaths | empty | Paths (with target sizes) where SSTs may live. | No | — |
| db_log_dir | setDbLogDir | `""` | Directory for info LOG files. | No | — |
| wal_dir | setWalDir | `""` | Directory for write-ahead log files. | No | — |
| delete_obsolete_files_period_micros | setDeleteObsoleteFilesPeriodMicros | `6h` | How often obsolete files are cleaned up. | No | — |
| **max_background_jobs** | setMaxBackgroundJobs | `2` | Max concurrent background compaction + flush jobs. | No | **`6`** |
| max_subcompactions | setMaxSubcompactions | `1` | Threads splitting a single compaction into parallel parts. | No | — |
| max_log_file_size | setMaxLogFileSize | `0` | Max info LOG file size before rolling; 0 = single file. | No | — |
| keep_log_file_num | setKeepLogFileNum | `1000` | Max number of info LOG files retained. | No | — |
| recycle_log_file_num | setRecycleLogFileNum | `0` | Reuse N old WAL files to save reallocation. | No | — |
| max_manifest_file_size | setMaxManifestFileSize | `1 GB` | Size at which the MANIFEST rolls over. | No | — |
| table_cache_numshardbits | setTableCacheNumshardbits | `6` | Shards (as bits) for the table cache. | Yes | — |
| WAL_ttl_seconds | setWalTtlSeconds | `0` | Age after which archived WALs are deleted; 0 = no TTL. | No | — |
| WAL_size_limit_MB | setWalSizeLimitMB | `0` | Size limit for the WAL archive. | No | — |
| manifest_preallocation_size | setManifestPreallocationSize | `4 MB` | Bytes preallocated for MANIFEST files. | No | — |
| allow_mmap_reads | setAllowMmapReads | `false` | Memory-map SST files for reading. | Yes | — |
| allow_mmap_writes | setAllowMmapWrites | `false` | Memory-map files for writing (disables SyncWAL). | Yes | — |
| use_direct_reads | setUseDirectReads | `false` | Use O_DIRECT for reads (bypass OS page cache). | Yes | — |
| use_direct_io_for_flush_and_compaction | setUseDirectIoForFlushAndCompaction | `false` | Use O_DIRECT for flush/compaction writes. | Yes | — |
| allow_fallocate | setAllowFallocate | `true` | Allow fallocate() preallocation (disable on btrfs). | No | — |
| stats_dump_period_sec | setStatsDumpPeriodSec | `600` | How often DB stats are dumped to the LOG; 0 = off. | No | — |
| stats_persist_period_sec | setStatsPersistPeriodSec | `600` | How often DB stats are persisted; 0 = off. | No | — |
| persist_stats_to_disk | setPersistStatsToDisk | `false` | Persist stats history to a hidden CF instead of RAM. | Yes | — |
| stats_history_buffer_size | setStatsHistoryBufferSize | `1 MB` | Memory cap for in-memory stats snapshots. | Yes | — |
| advise_random_on_open | setAdviseRandomOnOpen | `true` | Hint the FS that SST access is random when a file opens. | No | — |
| **db_write_buffer_size** | setDbWriteBufferSize | `0` | Total memtable memory across ALL column families before flush; 0 = uncapped. | **Yes** | **`256 MB`** |
| **write_buffer_manager** | setWriteBufferManager | `nullptr` | Shared object capping memtable memory (optionally charged to the block cache). | **Yes** | — *(we use `db_write_buffer_size` instead; see §6/§8)* |
| compaction_readahead_size | setCompactionReadaheadSize | `2 MB` | Readahead buffer for compaction reads. | Yes | — |
| writable_file_max_buffer_size | setWritableFileMaxBufferSize | `1 MB` | Max write buffer used by the writable-file writer. | Yes | — |
| **bytes_per_sync** | setBytesPerSync | `0` | Incremental OS sync every N bytes for SST files; 0 = off. | No | **`1 MB`** |
| wal_bytes_per_sync | setWalBytesPerSync | `0` | Same as bytes_per_sync but for WAL files. | No | — |
| strict_bytes_per_sync | setStrictBytesPerSync | `false` | Guarantee pending writeback never exceeds the bytes_per_sync limits. | No | — |
| listeners | setListeners | empty | EventListeners invoked on RocksDB events (flush, compaction, …). | No | — |
| delayed_write_rate | setDelayedWriteRate | `0` | Write rate (B/s) when throttled; 0 = inferred. | No | — |
| enable_pipelined_write | setEnablePipelinedWrite | `false` | Separate thread queues for WAL vs memtable writes. | No | — |
| unordered_write | setUnorderedWrite | `false` | Trade snapshot immutability for write throughput. | No | — |
| allow_concurrent_memtable_write | setAllowConcurrentMemtableWrite | `true` | Allow parallel memtable writers. | No | — |
| enable_write_thread_adaptive_yield | setEnableWriteThreadAdaptiveYield | `true` | Writer threads spin-yield before blocking, for throughput. | No | — |
| max_write_batch_group_size_bytes | setMaxWriteBatchGroupSizeBytes | `1 MB` | Max bytes in a single batched write group. | Yes | — |
| skip_stats_update_on_db_open | setSkipStatsUpdateOnDbOpen | `false` | Skip loading table stats on open to speed up Open. | No | — |
| wal_recovery_mode | setWalRecoveryMode | `kPointInTimeRecovery` | How strict WAL replay is on recovery. | No | — |
| allow_2pc | setAllow2pc | `false` | Recover two-phase-commit prepared transactions from the WAL. | No | — |
| row_cache | setRowCache | `nullptr` | Global cache of whole rows to speed up Get(). | Yes | — |
| dump_malloc_stats | setDumpMallocStats | `false` | Print malloc stats alongside rocksdb.stats in the LOG. | Yes | — |
| avoid_flush_during_recovery | setAvoidFlushDuringRecovery | `false` | Skip flushing memtables during recovery. | No | — |
| avoid_flush_during_shutdown | setAvoidFlushDuringShutdown | `false` | Skip flushing memtables on close (loses unpersisted data). | No | — |
| two_write_queues | setTwoWriteQueues | `false` | Two write queues so memtable writes don't lag. | No | — |
| manual_wal_flush | setManualWalFlush | `false` | Disable automatic WAL flush; you call FlushWAL yourself. | No | — |
| wal_compression | setWalCompression | `kNoCompression` | Compress WAL records (ZSTD only). | No | — |
| atomic_flush | setAtomicFlush | `false` | Flush multiple CFs atomically. | No | — |
| avoid_unnecessary_blocking_io | setAvoidUnnecessaryBlockingIo | `false` | Defer slow file/memtable deletions to background jobs. | No | — |
| best_efforts_recovery | setBestEffortsRecovery | `false` | Open to any valid point-in-time state, tolerating missing files. | No | — |
| max_bgerror_resume_count | setMaxBgerrorResumeCount | `INT_MAX` | Auto-retry count after retryable background I/O errors. | No | — |
| allow_data_in_errors | setAllowDataInErrors | `false` | Allow corrupted keys/values in error messages/logs. | No | — |
| db_host_id | setDbHostId | hostname | Host ID written into every SST for troubleshooting. | No | — |
| lowest_used_cache_tier | setLowestUsedCacheTier | `kNonVolatileBlockTier` | Lowest cache tier used (block only vs block + secondary). | Yes | — |
| daily_offpeak_time_utc | setDailyOffpeakTimeUtc | `""` | UTC off-peak window for scheduling low-priority compactions. | No | — |

*(Rarely-used DB options omitted for brevity: paranoid sub-flags, manifest/recovery tuning,
follower-mode, temperature hints, checksum-handoff, compaction-service. None are set by us; all
are at defaults.)*

---

## 5. Column-family options (`ColumnFamilyOptions`) — full catalog

Set per column family. In our code these come from `defaultColumnFamilyOptions()` — **only the
three rows marked `Ours` actually reach the column families**; anything we set on the `Options`
object instead (e.g. `compaction_pri`) is dropped (see §3.1).

### 5.1 Memtable / write-buffer options (the main per-CF memory)

| Option | Java setter | Default | What it does | Mem? | Ours |
|---|---|---|---|---|---|
| **write_buffer_size** | setWriteBufferSize | `64 MB` | Size of one memtable before it is flushed to an SST. | **Yes** | — *(default 64 MB)* |
| **max_write_buffer_number** | setMaxWriteBufferNumber | `2` | Max memtables held in memory at once for this CF. | **Yes** | — *(default 2)* |
| min_write_buffer_number_to_merge | setMinWriteBufferNumberToMerge | `1` | Min memtables merged before a flush. | Yes | — |
| max_write_buffer_size_to_maintain | setMaxWriteBufferSizeToMaintain | `0` | Recent write history (bytes) kept in memory for txn conflict checks. | Yes | — |
| arena_block_size | setArenaBlockSize | `0` | Block size of the memtable arena allocator; 0 ≈ 1/8 of write_buffer_size. | Yes | — |
| memtable_factory | setMemtableFactory | `SkipListFactory` | In-memory structure for the memtable (default skip-list). | Yes | — |
| memtable_prefix_bloom_size_ratio | setMemtablePrefixBloomSizeRatio | `0.0` | Fraction of write_buffer_size for an in-memtable prefix bloom. | Yes | — |
| memtable_whole_key_filtering | setMemtableWholeKeyFiltering | `false` | Whole-key bloom in the memtable for faster point lookups. | Yes | — |
| memtable_huge_page_size | setMemtableHugePageSize | `0` | Use huge pages for the memtable arena; 0 = normal malloc. | Yes | — |
| inplace_update_support | setInplaceUpdateSupport | `false` | Update memtable values in place instead of appending. | Yes | — |
| inplace_update_num_locks | setInplaceUpdateNumLocks | `10000` | Locks coordinating concurrent in-place updates. | Yes | — |
| max_successive_merges | setMaxSuccessiveMerges | `0` | Consecutive Merges on one key before collapsing the value. | Yes | — |
| memtable_protection_bytes_per_key | setMemtableProtectionBytesPerKey | `0` | Per-entry checksum bytes in the memtable (corruption detection). | Yes | — |
| memtable_max_range_deletions | setMemtableMaxRangeDeletions | `0` | Flush once the memtable holds this many range deletes. | Yes | — |

### 5.2 Compaction & level sizing

| Option | Java setter | Default | What it does | Mem? | Ours |
|---|---|---|---|---|---|
| compaction_style | setCompactionStyle | `kCompactionStyleLevel` | Compaction algorithm: level / universal / FIFO / none. | No | — |
| **compaction_pri** | setCompactionPriority | `kMinOverlappingRatio` | For level compaction, which files to pick next. | No | ⚠ dropped (set to the default value anyway) |
| **level_compaction_dynamic_level_bytes** | setLevelCompactionDynamicLevelBytes | `true` | Dynamically size levels for a healthier LSM shape. | No | **`true`** |
| level0_file_num_compaction_trigger | setLevel0FileNumCompactionTrigger | `4` | L0 file count that triggers compaction. | No | — |
| level0_slowdown_writes_trigger | setLevel0SlowdownWritesTrigger | `20` | L0 file count at which writes slow down. | No | — |
| level0_stop_writes_trigger | setLevel0StopWritesTrigger | `36` | L0 file count at which writes stop. | No | — |
| max_bytes_for_level_base | setMaxBytesForLevelBase | `256 MB` | Target total size of level-1. | No | — |
| max_bytes_for_level_multiplier | setMaxBytesForLevelMultiplier | `10` | Size ratio between consecutive levels. | No | — |
| target_file_size_base | setTargetFileSizeBase | `64 MB` | Target size of each SST at level-1. | No | — |
| target_file_size_multiplier | setTargetFileSizeMultiplier | `1` | Per-level multiplier for SST target size. | No | — |
| num_levels | setNumLevels | `7` | Number of LSM levels for this CF. | No | — |
| max_compaction_bytes | setMaxCompactionBytes | `0` | Soft cap on bytes in one compaction; 0 ⇒ target_file_size_base × 25. | No | — |
| soft_pending_compaction_bytes_limit | setSoftPendingCompactionBytesLimit | `64 GB` | Pending-compaction bytes above which writes slow. | No | — |
| hard_pending_compaction_bytes_limit | setHardPendingCompactionBytesLimit | `256 GB` | Pending-compaction bytes above which writes stop. | No | — |
| disable_auto_compactions | setDisableAutoCompactions | `false` | Turn off automatic background compactions. | No | — |
| compaction_filter / _factory | setCompactionFilterFactory | `nullptr` | Modify/drop key-values during compaction. | No | — |
| ttl | setTtl | ~30 days | Age after which entries are recompacted/deleted (style-dependent). | No | — |
| periodic_compaction_seconds | setPeriodicCompactionSeconds | ~30 days | Age after which files are periodically recompacted. | No | — |

### 5.3 Compression

| Option | Java setter | Default | What it does | Mem? | Ours |
|---|---|---|---|---|---|
| **compression** | setCompressionType | LZ4 if available else Snappy else none | Compression for most SST blocks. | No | **`LZ4`** |
| **bottommost_compression** | setBottommostCompressionType | `kDisableCompressionOption` (inherits `compression`) | Compression for the biggest, coldest level. | No | **`ZLIB`** |
| compression_per_level | setCompressionPerLevel | empty | Per-level compression overrides. | No | — |
| compression_opts | setCompressionOptions | default | Detailed params (level, window, dictionary). | No | — |
| bottommost_compression_opts | setBottommostCompressionOptions | default | Detailed params for the bottommost level. | No | — |
| sample_for_compression | setSampleForCompression | `0` | Sample 1-in-N blocks to estimate compressibility. | No | — |

### 5.4 Lookups, indexing, blobs, integrity

| Option | Java setter | Default | What it does | Mem? | Ours |
|---|---|---|---|---|---|
| comparator | setComparator | bytewise | Key sort order. | No | — |
| merge_operator | setMergeOperator | `nullptr` | Defines how `Merge` combines values. | No | — |
| prefix_extractor | setPrefixExtractor | `nullptr` | Groups keys into prefixes for prefix bloom/seek. | Yes | — |
| **table_factory** | setTableFormatConfig | block-based | Builds/reads SST files; **this is where the table config in §6 attaches**. | Yes | ✅ block-based config wired here |
| max_sequential_skip_in_iterations | setMaxSequentialSkipInIterations | `8` | Same-key entries skipped before a reseek. | No | — |
| optimize_filters_for_hits | setOptimizeFiltersForHits | `false` | Skip last-level bloom to save space when misses are rare. | No | — |
| force_consistency_checks | setForceConsistencyChecks | `true` | LSM consistency checks even in release builds. | No | — |
| block_protection_bytes_per_key | setBlockProtectionBytesPerKey | `0` | Per-key checksum bytes in in-memory blocks. | Yes | — |
| uncache_aggressiveness | setUncacheAggressiveness | `0` | How aggressively cache entries for obsolete files are erased. | Yes | — |
| paranoid_file_checks | setParanoidFileChecks | `false` | Re-read every newly written SST to verify it. | No | — |
| enable_blob_files | setEnableBlobFiles | `false` | Store large values in separate blob files (key-value separation). | No | — |
| min_blob_size | setMinBlobSize | `0` | Min value size to store in a blob file. | No | — |
| blob_file_size | setBlobFileSize | `256 MB` | Size at which a new blob file is started. | No | — |
| blob_cache | setBlobCache | `nullptr` | Cache for blobs read from blob files. | Yes | — |
| enable_blob_garbage_collection | setEnableBlobGarbageCollection | `false` | Relocate live blobs during compaction so old blob files can be deleted. | No | — |

*(Blob, tiered-storage temperature, and user-defined-timestamp options are all at defaults — we
use none of them.)*

---

## 6. Block-based table options (`BlockBasedTableConfig`) — full catalog

These control the on-disk SST format **and the block cache**. They belong to the column family
(`table_factory`) and are wired correctly via
`defaultColumnFamilyOptions().setTableFormatConfig(createBlockBasedTableConfig())`, so the `Ours`
values below are in effect.

| Option | Java setter | Default | What it does | Mem? | Ours |
|---|---|---|---|---|---|
| **block_cache** | setBlockCache | `nullptr` ⇒ internal 32 MB LRU | The shared cache for data (and, if enabled, index/filter) blocks. | **Yes** | **`LRUCache(256 MB)`** shared across this store's CFs |
| no_block_cache | setNoBlockCache | `false` | Disable the block cache entirely. | Yes | — |
| **block_size** | setBlockSize | `4 KB` | Approx uncompressed bytes per block; bigger = fewer/larger cache entries and smaller index. | **Yes** | **`16 KB`** |
| **cache_index_and_filter_blocks** | setCacheIndexAndFilterBlocks | `false` | Put index/filter blocks in the (bounded) block cache instead of unbounded table-reader memory. | **Yes** | **`true`** |
| cache_index_and_filter_blocks_with_high_priority | setCacheIndexAndFilterBlocksWithHighPriority | `true` | Give cached index/filter high priority so data blocks are evicted first. | Yes | **`true`** (explicitly set) |
| **pin_l0_filter_and_index_blocks_in_cache** | setPinL0FilterAndIndexBlocksInCache | `false` | Pin L0 files' index/filter in cache for the reader's life. | Yes | **`true`** |
| pin_top_level_index_and_filter | setPinTopLevelIndexAndFilter | `true` | Pin the top-level index of partitioned index/filter. | Yes | — (default true) |
| metadata_cache_options | setMetadataCacheOptions | all tiers `kFallback` | Per-tier pinning of metadata blocks. | Yes | — |
| **filter_policy** | setFilterPolicy | `nullptr` (no filter) | Bloom/Ribbon filter to skip disk reads on point lookups. | **Yes** | **`BloomFilter(10.0)`** (~10 bits/key) |
| whole_key_filtering | setWholeKeyFiltering | `true` | Put whole keys in the filter (needed for point Gets). | Yes | — (default true) |
| partition_filters | setPartitionFilters | `false` | Split the filter into cache-managed partitions (needs two-level index). | Yes | — |
| optimize_filters_for_memory | setOptimizeFiltersForMemory | `true` | Round filter sizes to cut malloc fragmentation. | Yes | — (default true) |
| index_type | setIndexType | `kBinarySearch` | Index structure (binary / hash / two-level / …). | No | — |
| data_block_index_type | setDataBlockIndexType | `kDataBlockBinarySearch` | Plain binary search vs added hash index in data blocks. | No | — |
| metadata_block_size | setMetadataBlockSize | `4096` | Target size of partitioned metadata blocks. | Yes | — |
| **format_version** | setFormatVersion | `7` (v11.6; rocksdbjni 10.10.x default is lower) | On-disk SST format version for new files. | No | **`5`** |
| checksum | setChecksumType | `kXXH3` | Block checksum algorithm. | No | — |
| block_restart_interval | setBlockRestartInterval | `16` | Keys between restart points (delta key encoding) in data blocks. | No | — |
| index_block_restart_interval | setIndexBlockRestartInterval | `1` | Same, for index blocks. | No | — |
| block_size_deviation | setBlockSizeDeviation | `10` | Close a block early if free space drops below this %. | No | — |
| use_delta_encoding | setUseDeltaEncoding | `true` | Delta-encode keys within blocks. | No | — |
| enable_index_compression | setEnableIndexCompression | `true` | Store index blocks compressed on disk. | No | — |
| read_amp_bytes_per_bit | setReadAmpBytesPerBit | `0` | Per-block bitmap to estimate read amplification (uses memory when on). | Yes | — |
| persistent_cache | setPersistentCache | `nullptr` | Optional on-device secondary cache. | Yes | — |
| prepopulate_block_cache | setPrepopulateBlockCache | `kDisable` | Warm the cache at flush/compaction time. | Yes | — |
| max_auto_readahead_size | setMaxAutoReadaheadSize | `256 KB` | Max iterator auto-readahead size. | Yes | — |
| initial_auto_readahead_size | setInitialAutoReadaheadSize | `8 KB` | Starting iterator auto-readahead size. | Yes | — |
| block_align | setBlockAlign | `false` | Align data blocks to page/block size. | No | — |
| detect_filter_construct_corruption | setDetectFilterConstructCorruption | `false` | Extra integrity check during filter construction (~30% slower). | No | — |

---

## 7. Read / Write / Flush / Transaction options

We use **defaults** for almost all of these; the exceptions are noted in the `Ours` column.

### 7.1 ReadOptions (per read / iterator)

| Setter | Default | What it does | Ours |
|---|---|---|---|
| setVerifyChecksums | `true` | Verify block checksums on every read. | — |
| setFillCache | `true` | Populate the block cache during reads (set false for big scans). | — |
| setSnapshot | `null` | Read as of a specific snapshot. | — |
| setReadTier | `READ_ALL_TIER` | Restrict reads to a cache tier (e.g. memtable/cache only). | — |
| setTotalOrderSeek | `false` | Force total-order seek regardless of prefix index. | — |
| setPinData | `false` | Keep blocks the iterator loaded pinned in memory while it lives. | — |
| setReadaheadSize | `0` | Per-iterator readahead bytes (helps spinning disks). | — |
| setIterateLowerBound / UpperBound | `null` | Constrain an iterator's key range. | — |
| setBackgroundPurgeOnIteratorCleanup | `false` | Delete obsolete files in a background job on iterator close. | — |

### 7.2 WriteOptions (per write)

| Setter | Default | What it does | Ours |
|---|---|---|---|
| setSync | `false` | fsync the write before returning (durable but slow). | — |
| setDisableWAL | `false` | Skip the write-ahead log (faster, writes lost on crash). | — |
| setNoSlowdown | `false` | Fail immediately instead of waiting if writes are stalled. | — |
| setLowPri | `false` | Mark write low priority so it yields when compaction is behind. | — |

### 7.3 FlushOptions

| Setter | Default | What it does | Ours |
|---|---|---|---|
| setWaitForFlush | `true` | Block until the flush finishes. | **`true`** (explicit, on backup/compact/close flushes) |
| setAllowWriteStall | `false` | Allow writes to stall so the flush proceeds immediately. | — |

### 7.4 TransactionDBOptions (DB open) and TransactionOptions (per txn)

We construct `new TransactionDBOptions()` and rely entirely on defaults; per-transaction we only
call `setSnapshot()` on the transaction (read-your-writes consistency).

| Setter | Default | What it does | Ours |
|---|---|---|---|
| setMaxNumLocks | `-1` (unlimited) | Max keys lockable per CF. | — (default) |
| setNumStripes | `16` | Lock-table sub-tables per CF (concurrency). | — |
| setTransactionLockTimeout | `1000 ms` | Wait time to acquire a key lock in a txn. | — |
| setDefaultLockTimeout | `1000 ms` | Lock wait for non-transactional writes. | — |
| setWritePolicy | `WRITE_COMMITTED` | When txn data is written to the DB. | — |
| (per-txn) setSnapshot | `false` | Take a snapshot at txn start for conflict checking. | **enabled** for write txns |
| (per-txn) setExpiration | `-1` | Fail/clear txns older than N ms (releases stuck locks). | — |
| (per-txn) setLockTimeout | `-1` | Per-txn lock wait; negative = DB default. | — |

---

## 8. Memory deep-dive & recommendations

This section ties the catalog back to the native-memory growth we see.

### 8.1 Where native memory goes (current)

With the recommendations applied, our memory-relevant configuration is:

- `cache_index_and_filter_blocks = true` → **index and filter blocks now live in the block cache**,
  not in unbounded table-reader memory. With `cache_index_and_filter_blocks_with_high_priority`,
  they are evicted only after regular data blocks.
- `max_open_files = 1024` → bounds how many SST files (and their readers) are open at once, so
  table-reader memory can no longer grow without limit as the dataset grows.
- `block_cache = LRUCache(256 MB)` (shared across the store's column families) → a single, sized,
  **observable** ceiling for data + index + filter blocks.
- `db_write_buffer_size = 256 MB` → caps total memtable memory across all **9 column families**
  (which would otherwise reach ~1.1 GB; §3.3).
- `statistics` enabled + `MetricsHolder` → memory and IO are now observable (see §8.2).

The remaining native memory not bounded by the block cache is mainly: the active memtables (capped
at 256 MB), iterator/pinned blocks while iterators are open, WAL write buffers, and small fixed
overheads (stats, table cache metadata). Note the 256 MB cache is **per store instance** — if a
process hosts several stores, the total is N × 256 MB unless a single cache is shared across them
(the `createDefaultBlockCache()` override point is provided for exactly that).

### 8.2 Observability — reading the numbers

RocksDB exposes memory via DB properties; with `statistics` on, the store also publishes
metrics through `MetricsHolder`. The properties worth watching:

- `rocksdb.block-cache-usage` / `rocksdb.block-cache-capacity` — cache fill vs the 256 MB ceiling.
- `rocksdb.estimate-table-readers-mem` — index/filter memory **outside** the cache; should now stay
  small and flat (it was the unbounded growth before the fix).
- `rocksdb.cur-size-all-mem-tables` / `rocksdb.size-all-mem-tables` — live memtable memory vs the
  256 MB `db_write_buffer_size` cap.
- `rocksdb.num-snapshots` / `rocksdb.num-running-flushes` — long-lived snapshots / background work.

### 8.3 Possible further tightening

- **Shared `WriteBufferManager`** instead of (or in addition to) `db_write_buffer_size`, optionally
  charged to the block cache, to unify the memtable + cache budget into one ceiling — and to share
  that budget across multiple store instances in one process.
- **`strict_capacity_limit`** on the `LRUCache` to make the 256 MB a hard cap rather than a target.
- **Share one cache across stores** (override `createDefaultBlockCache()`) if a process hosts many.

### 8.4 The allocator angle (separate from config)

Even with the above, the C++ allocator matters: glibc `malloc` fragments under RocksDB's
multi-threaded alloc/free pattern and is slow to return freed memory to the OS. jemalloc/tcmalloc
return memory more readily. That is a deployment/packaging concern (preloading the allocator),
**not** a RocksDB option, and is covered separately.

---

## 9. Quick reference: everything we set

| Where | Option | Value | Reaches RocksDB? |
|---|---|---|---|
| DB | create_if_missing | true | ✅ |
| DB | create_missing_column_families | true | ✅ |
| DB | max_background_jobs | 6 | ✅ |
| DB | bytes_per_sync | 1 MB | ✅ |
| DB | **max_open_files** | 1024 | ✅ |
| DB | **db_write_buffer_size** | 256 MB | ✅ |
| DB | **max_total_wal_size** | 1 GB | ✅ |
| DB | **statistics** | EXCEPT_DETAILED_TIMERS | ✅ |
| CF | level_compaction_dynamic_level_bytes | true | ✅ |
| CF | compression | LZ4 | ✅ |
| CF | bottommost_compression | ZLIB | ✅ |
| CF | compaction_pri | MinOverlappingRatio | ⚠ dropped (== default) |
| Table | **block cache** | LRUCache(256 MB) | ✅ |
| Table | block_size | 16 KB | ✅ |
| Table | cache_index_and_filter_blocks | true | ✅ |
| Table | cache_index_and_filter_blocks_with_high_priority | true | ✅ |
| Table | pin_l0_filter_and_index_blocks_in_cache | true | ✅ |
| Table | filter_policy | BloomFilter(10) | ✅ |
| Table | format_version | 5 | ✅ |
| Flush | wait_for_flush | true | ✅ |
| Txn | (per write txn) setSnapshot | enabled | ✅ |

Everything not in this table is left at the RocksDB default.

