# Smart Cache Storage - RocksDB

The `rocksdb` module provides the building blocks for working with RocksDB storage, which is a key value store. This
uses the RocksDB Java APIs and provides convenient starting points for RocksDB backed storage.

This API consists of several classes:

- `AbstractRocksDBStorage` - An abstract implementation of a RocksDB backed store.
- `TransactionContext` - A transaction helper for interacting with RocksDB in a transactionally safe way.
- `RocksDBCounter` - A persistent auto-incrementing counter stored as a single key within a RocksDB column family.

Developers using this API should have their storage classes extend `AbstractRocksDBStorage`, and then use the various
protected methods to customise and implement their actual storage.  Most importantly they **MUST** override the
`prepareColumnFamilyDescriptors()` method to supply a list of column families that the storage uses and should
create/open when accessing the RocksDB database.  Note that you **MUST** always include the default column family
(`RocksDB.DEFAULT_COLUMN_FAMILY`) in your list of descriptors otherwise RocksDB will refuse to open the database.

Various other `protected` methods may be overridden in order to customise other aspects of the storage
behaviour/configuration, see [configuration](#rocksdb-configuration).

See the [`RocksDBLabelsStore`][RocksLabelsStore] implementations for an exemplar of this pattern.

## Key Helper Methods

Some key helper methods are as follows:

- `getHandle(byte[])` for obtaining a `ColumnFamilyHandle`, the given name **MUST** match a column family descriptor
  name previously initialised in `prepareColumnFamilyDescriptors()`.
- `getDefaultHandler()` for obtaining a `ColumnFamilyHandle` for the default column family.
- `getCounter()` for obtaining a [`RocksDBCounter`](#rocksdb-counters).
- `begin()` for starting a new [transaction](#transaction-context).

There are a number of additional [configuration](#rocksdb-configuration) methods called by the constructor that can be
used to customise the storage as needed.

Additionally there are some static methods available:

- `bytesToLong(byte[])` and `longToBytes(long)` for translating between a `byte[]` and a `long` as needed.

## RocksDB Configuration

The only configuration required by this API is the database directory where the persistent RocksDB storage exists, or
should be stored.  Implementations can override various `protected` methods if they need to further customise the
configuration for their RocksDB storage, the following methods are called once, and only once, by the constructor while
initialising the storage:

- **REQUIRED** `prepareColumnFamilyDescriptors()` - Prepares a list of `ColumnFamilyDescriptors` describing the column
  families the storage wishes to access.
- `createDefaultOptions()` to create the `Options` used to open the RocksDB database.
- `createDefaultTransactionOptions()` to create the `TransactionDBOptions` for interacting with RocksDB transactionally.
- `defaultColumnFamilyOptions()` to create the `ColumnFamilyOptions` passed to the `prepareColumnFamilyDescriptors()`
  method.
- `prepareCounters()` to prepare any [counters](#rocksdb-counters) needed.

The following methods are called anytime a new [transaction](#transaction-context) is begun:

- `defaultWriteOptions()` to create the `WriteOptions` for writing to RocksDB.
- `defaultReadOptions()` to create the `ReadOptions` for reading from RocksDB.

## Transaction Context

The `TransactionContext` interface provides an abstraction for interacting with RocksDB transactionally, within your
storage classes methods you should use the following code pattern:

```java
// Start a fresh transaction
try (TransactionContext context = this.begin()) {
  ColumnFamilyHandle handle = this.getHandle(HANDLE_NAME);
  // Perform some operations
  context.put(handle, key, value);
  // etc.

  // Commit the transaction
  context.commit();
}
```

The `TransactionContext` provides access to a intentionally limited subset of `RocksDB` operations:

- `get(ColumnFamilyHandle, byte[])` for getting the value associated with a single key.
- `put(ColumnFamilyHandle, byte[], byte[])` for setting the value associated with a single key.
- `multiGetAsList(List<ColumnFamilyHandle>, List<byte[]>)` for getting the values associated with multiple keys in a
  single operation.
- `count(ColumnFamilyHandle)` for counting the keys in a column family.
- `isEmpty(ColumnFamilyHandle)` for determining whether a given column family contains any keys.
- `forEach(ColumnFamilyHandle, Consumer<KeyValue>)` for full iteration and processing of a column family, intended
  for expensive one-time operations like data migrations.
- `commit()` for committing the transaction.

**NB** If you do not `commit()` the transaction then when the `close()` method is called the transaction will be
automatically rolled back, thus developers **MUST** call `commit()` at the end of their transaction or any writes will
be lost.

### Nested Transactions

If your storage implementation needs to call several other methods that would otherwise `begin()` separate transactions
it can call `beginNested()` instead.  This creates a "nested" transaction from an application perspective that ensures
all the operations that happen within are committed atomically.  Each time one of your storage methods calls
`begin()`/`beginNested()` this increments the nesting of the pre-existing transaction e.g.

```java
// Start a fresh transaction
try (TransactionContext context = this.beginNested()) {
  // Call some other methods that would begin()/beginNested() their own transactions
  this.other();
  this.another();

  // Perform some operations
  context.put(handle, key, value);
  // etc.

  // Commit the transaction
  context.commit();
}
```

Only the outermost `commit()`/`close()` operation actually takes effect.  The intervening `commit()`/`close()` calls
that are carried out serve only to track the level of transaction "nesting" and apply the action when the outermost
nesting level is reached.

## RocksDB Counters

The `RocksDBCounter` class is a helper that provides an auto-incrementing counter backed by a specific key within a
specific column family.  Implementations can initialise these by overriding the `prepareCounters()` method and using the
`createCounter()` method to create each named counter they wish to have available.

Once registered counters may be retrieved via the `getCounter()` method, and the counter instances provide the following
methods:

- `next()` for obtaining the next available value, optionally persisting the counter.  Optional persistence means you
  can write the counter value only once for bulk operations that may increment the counter many times.
- `update()` for explicitly persisting the counter.
- `sync()` for synchronising the counter value with the underlying database.  This is called automatically during
  counter construction and should rarely, if ever, need calling after this.

Note that when you `close()` the owning `AbstractRocksDBStorage` any registered counters are explicitly persisted by
calling `update()` to ensure that values are not lost if you forgot to persist them during usage.

Again see [`RocksDBLabelStore`][RocksLabelStore] for an exemplar of its usage.

[RocksLabelStore]: ../label-stores/label-store-rocksdb/src/main/java/io/telicent/smart/cache/storage/labels/rocksdb/RocksDbLabelsStore.java
