window.BENCHMARK_DATA = {
  "lastUpdate": 1794192465618,
  "repoUrl": "https://github.com/telicent-oss/smart-cache-storage",
  "entries": {
    "Label Store Benchmarks": [
      {
        "commit": {
          "author": {
            "name": "Rob Vesse",
            "username": "rvesse",
            "email": "rob.vesse@telicent.io"
          },
          "committer": {
            "name": "Rob Vesse",
            "username": "rvesse",
            "email": "rob.vesse@telicent.io"
          },
          "id": "d139fe94dd5146ae52b6495633bae5b8d8105c62",
          "message": "Document existence of benchmarks",
          "timestamp": "2026-03-31T14:22:19Z",
          "url": "https://github.com/telicent-oss/smart-cache-storage/commit/d139fe94dd5146ae52b6495633bae5b8d8105c62"
        },
        "date": 1774972202406,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 8858.681275300729,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.4837588592911009,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4687105484550228,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 442.41511077006487,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 1975.7152080650903,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.4491400895612658,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.47823459651289896,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 136.52577975272862,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 4345.413436819967,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.4687226992818654,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4812690598737851,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 3988.0184716619733,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 12632.155963881843,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.1850704135737806,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 3.1409493074321406,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 567.1818523312368,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 6282.775956641296,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 6274.407292148075,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 5961.917339321168,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 5697.801085945524,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 5797.250854389024,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 5281.748157915188,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 5741.987558061131,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 6227.011908835079,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA512\"} )",
            "value": 1487.605733198114,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA256\"} )",
            "value": 6558.351960005052,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA512\"} )",
            "value": 1441.5871461701488,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA256\"} )",
            "value": 6117.711373392956,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA512\"} )",
            "value": 1500.3273060213196,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA256\"} )",
            "value": 6565.345237483819,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 225.73548694513246,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.6174731222339815,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.400089145068576,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 89.10300100617106,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 27.12426425890243,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.6061063679710805,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.39224583287874976,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 16.005554925447893,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 55.344084577435204,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.6058351150385886,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4069976968439752,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 45.45786008263872,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 7420.4743447098535,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.8602391873296682,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 2.589901034894542,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 293.5500451214505,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 5866.8644114555045,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 2.6212116681880326,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 3.8341851269956906,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 1160.288239950382,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 14375.994688545587,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 15007.373674969693,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 16479.11899586838,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 16710.21749812268,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 520.7844031562216,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.159294876139049,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.45134933576625064,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 49.896778577974146,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 323.0598304905358,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.11302812219047,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4417345527853531,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 18.552332696326253,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 310.9676823131505,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.0935477371665454,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4407303111943176,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 18.486166756441587,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 6184.868872822376,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.1150471244778253,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.3793530371999614,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 246.39228056678402,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 1312.4760985915648,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 1.2997913246326118,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 1.5388018684957587,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 106.09915971489261,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3666.6357346169984,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3625.3680212968184,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3570.5065121365724,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3610.6730976450453,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.7557419911885073,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.007859308520253116,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.003714914061878635,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.09242645016554571,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.411068897511263,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.37882464036979957,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.3325797962666849,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.40393878958671897,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.39767663815821885,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.37868740769649,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.31618458582354725,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.40535012549723304,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 8988.953705493932,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.042622714544382,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.8571490967391342,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 485.78129033775804,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 4358.0447234574585,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3851.4708487270605,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3594.801530637029,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 4346.596499383633,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 4271.81114910201,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 4039.372768067841,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 3444.427475355221,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 4188.694784122489,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 5185.66319069729,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.0546613205117699,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.1524322460228862,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 240.1960224812301,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3381.8847407418116,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 2807.837657018864,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 2610.1870128765427,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3384.8620297205953,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 3506.155747479685,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 2710.293873457639,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 2725.601508671928,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 3508.557934465111,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.07677245846456718,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.011870879371947357,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.010530905517182785,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.5402986343137617,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.062446233357512794,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.06141759828149569,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.060327205946240844,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.06262862465676952,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.062406532249759894,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.061411702787294555,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.06039161174490195,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.06276666855739065,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.031799,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 6.618826,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 6.147215,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.129831,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.170698,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 5.152035,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 8.568053,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.278709,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.151502,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 6.987813,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 7.370728,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.233054,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "name": "Rob Walpole",
            "username": "robwtelicent",
            "email": "183595007+robwtelicent@users.noreply.github.com"
          },
          "committer": {
            "name": "GitHub",
            "username": "web-flow",
            "email": "noreply@github.com"
          },
          "id": "c102f3963e8a2ca56e1a59ed5042cd3d27d454e1",
          "message": "Merge pull request #182 from telicent-oss/fix-weekly-benchmarks\n\nFix weekly-benchmarks.yml",
          "timestamp": "2026-10-05T09:09:56Z",
          "url": "https://github.com/telicent-oss/smart-cache-storage/commit/c102f3963e8a2ca56e1a59ed5042cd3d27d454e1"
        },
        "date": 1791208791902,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 8645.264461827079,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.778967143498694,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5197224027822782,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 408.38116905385704,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3358.101882405868,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.747575436753149,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.555643398195833,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 187.72488473974377,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 5668.858242364384,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.7535937910092592,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5478369049958365,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 5258.183142226625,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 12418.832996320789,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.613675645840501,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 3.9057511990220464,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 541.4146677268393,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 8854.679034727751,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 8327.730938261,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 8810.27928549802,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 8456.14990756647,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 8936.853498273078,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 8025.679404268708,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 8529.00680360458,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 8680.104407335546,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA512\"} )",
            "value": 2865.5910889296792,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA256\"} )",
            "value": 12864.343220556104,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA512\"} )",
            "value": 2628.8090070049975,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA256\"} )",
            "value": 10331.87681030537,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA512\"} )",
            "value": 2908.9023962627407,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA256\"} )",
            "value": 13114.721831753614,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 209.73671697466324,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.7258017914668284,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4846056341655228,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 140.9413455588443,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 37.03316917967716,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.7026053516305617,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4875314742903405,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 25.699711586112578,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 58.861853417280884,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.7097949555211679,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5062517996808217,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 47.0827657975691,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 14486.610044958605,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.16951906507264,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 5.973702771234768,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 1133.823695641007,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 20515.340310848595,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 6.107138226346874,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 9.253695416877088,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 14667.61163482517,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 30415.97822236749,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 25673.204917613923,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 29897.78229057748,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 28788.31692333312,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 769.8218507032692,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.4442864366240602,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5863312195954217,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 49.29430278019774,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 469.26769565739465,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.3438818373080201,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5597682056137357,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 26.85308709349521,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 448.68888986228137,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.3793659428417353,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5886884909792518,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 26.95018057439948,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 6169.482890208064,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.311261712187649,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.8514952704794925,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 306.35596797292686,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 1218.9447097706889,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 1.5472811287354442,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 2.1099785561364572,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 118.93384960559843,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 2895.507999120868,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3767.992710649559,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 2930.705737971674,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 2776.48976232856,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.7617926364111789,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.00916797607173214,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.004834470206841514,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.08059919246345457,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.40643325182130174,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.4345438014727926,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.3588946521297518,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.43055545827643593,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.40533119099982573,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.4080473445525592,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.3456426990436019,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.39395655153913683,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 9071.510978915447,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.47258341685463,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 2.409322050032788,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 465.1089421469741,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 4369.12802555842,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4098.9655838642475,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3788.052552751672,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3956.1698878638335,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 4032.5668268335417,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 4100.793086822492,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 3641.1321922839975,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 3995.969492768664,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 5221.284379409289,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.283765993674189,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.5296246778114093,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 309.1491734627807,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 2666.7233405590314,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 2831.5761687486493,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 2202.126411191342,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 2692.4927801049516,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 2792.956937357453,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 5484.049325502404,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 2190.074584725117,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 2627.345526003246,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.07717117135557819,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.012561405166001202,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.011205358106788176,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.381489550103356,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.06249353487731507,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.06120148763046014,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.06082967726663635,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.062940728874329,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.06273089055929093,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.06213819357736435,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.060637682284690776,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.06275452876930214,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.023804,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.691587,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 4.977136,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.181589,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.061826,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4.765338,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 5.003596,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.297316,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.074108,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 5.261586,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 5.344667,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.29414,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "name": "Rob Walpole",
            "username": "robwtelicent",
            "email": "183595007+robwtelicent@users.noreply.github.com"
          },
          "committer": {
            "name": "GitHub",
            "username": "web-flow",
            "email": "noreply@github.com"
          },
          "id": "c102f3963e8a2ca56e1a59ed5042cd3d27d454e1",
          "message": "Merge pull request #182 from telicent-oss/fix-weekly-benchmarks\n\nFix weekly-benchmarks.yml",
          "timestamp": "2026-10-05T09:09:56Z",
          "url": "https://github.com/telicent-oss/smart-cache-storage/commit/c102f3963e8a2ca56e1a59ed5042cd3d27d454e1"
        },
        "date": 1791775243576,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 11083.007700884482,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 3.377712311825133,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.48366067386322487,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 556.105074960378,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 4138.850959227813,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3.1856316886455707,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.3938301766166394,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 258.99179409218175,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 7770.528095623427,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 2398.0822340189516,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.3806638803978366,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 7887.88495559085,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 16220.159200790391,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.351665330541558,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 5.854579423143304,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 741.8180382075537,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 10882.277402529722,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 10728.648104542503,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 10722.576067121698,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 10810.381340056907,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 10921.120810551307,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 10488.252580591487,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 10785.977701735794,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 10781.584804023782,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA512\"} )",
            "value": 3624.203421341366,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA256\"} )",
            "value": 14820.449412290283,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA512\"} )",
            "value": 3313.877439616363,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA256\"} )",
            "value": 11753.980675624676,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA512\"} )",
            "value": 3698.6443291671626,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA256\"} )",
            "value": 14414.96506394654,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 292.4663419847793,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.1408508292584811,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4285681406190709,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 179.02425880705013,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 45.39360374480795,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.100576105642094,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.369028704667169,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 35.30009850713256,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 68.3383114505643,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.1114224548478553,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.43373640529492247,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 61.3331954758389,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 22180.131913960166,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 6.833027629725093,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 8.29489739661261,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 1534.730108467347,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 27038.518283740937,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 9.538976559367473,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 13.218442711678819,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 17856.744065369672,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 30094.29516532499,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 34474.54148909635,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 35939.619773315135,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 31465.86139721307,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 823.04394901645,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.3088321506732448,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5966780292253067,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 72.55065353010161,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 585.8707750320501,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 2.2369740776978126,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5546202263981317,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 50.88197772000923,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 579.1728320840026,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 2.151472613888299,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5691433269840722,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 52.49690040235009,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 8707.268087828867,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.1710829028901477,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 2.69984147258366,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 415.9131993530747,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 1615.4969792944298,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 2.5482212352152667,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 3.056152306850648,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 181.3648225860814,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3629.2049604491804,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 5414.021456134832,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3657.6604560267588,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3599.5383284213376,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.9554470132988147,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.014547696851099138,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.006903673877687107,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.10958022820845052,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.7045689550262868,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.64689251939606,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.6143680918916454,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.7207159873836069,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.7200872459321286,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.6672778797517622,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5842271784716664,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.7323478463526939,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 10943.597990320935,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.164382357564065,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 3.0670329182590397,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 619.2747482326662,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 7448.115777276718,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 6976.065675572847,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 6450.202928155928,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 7804.088137828263,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 7811.792229507449,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 7067.3460825746615,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 6510.37843841507,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 7560.111934732327,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 7192.757433955953,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.102465781190811,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.977063186555115,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 407.2481366535219,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3216.3236390107363,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4013.6845196224285,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 2519.661961424373,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3222.626034682212,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 3199.7401269454795,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 3784.5541172151266,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 2437.8051685280575,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 3236.113996970701,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.10028163034724931,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.01650617512031379,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.01504352742915438,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.4997420352750255,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.07268142737674937,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.07156866549917955,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.07072062468745705,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.07312706976002772,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.07258620070252104,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.07199717783437605,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.07089702583112305,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.07327003611206467,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.01945,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 3.787114,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 3.482191,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.118828,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.057506,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4.381419,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3.606797,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.2464,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.061122,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 3.889963,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 4.044454,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.212458,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "name": "Paul Gallagher",
            "username": "TelicentPaul",
            "email": "132362215+TelicentPaul@users.noreply.github.com"
          },
          "committer": {
            "name": "GitHub",
            "username": "web-flow",
            "email": "noreply@github.com"
          },
          "id": "35b4ade2bfaa1243e4895cf47afebd1a43618e87",
          "message": "Merge pull request #187 from telicent-oss/release/0.15.2\n\nComplete Release 0.15.2",
          "timestamp": "2026-10-15T12:33:20Z",
          "url": "https://github.com/telicent-oss/smart-cache-storage/commit/35b4ade2bfaa1243e4895cf47afebd1a43618e87"
        },
        "date": 1792379051878,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 11008.99202430781,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 3.354841167181462,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4807998203615943,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 590.1101150847674,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 4179.542778943719,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3.2076070407190533,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.44514842716518244,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 249.2188400726021,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 7856.483356387274,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 2251.708267940813,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.39009405257790164,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 7971.057307949249,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 16304.681222542578,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.395505636652433,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 5.31188421360107,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 784.0038517727374,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 10660.871570309533,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 10409.49860268546,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 10508.848545023702,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 11203.252675342907,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 10626.601176786698,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 10316.835411423268,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 10728.35069945164,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 11012.231451700605,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA512\"} )",
            "value": 3720.0816564346287,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA256\"} )",
            "value": 15946.869255128742,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA512\"} )",
            "value": 3341.5316677128912,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA256\"} )",
            "value": 12487.109346052006,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA512\"} )",
            "value": 3685.6315714296434,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA256\"} )",
            "value": 14769.723830556719,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 296.2625663231216,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.1467627636552347,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.2839568653425706,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 181.25597510346014,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 46.04948212696099,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.1131886300085607,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.41326417667531484,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 40.1157763965095,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 68.68195543532056,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.120794160655132,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.3964174986927738,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 61.344700496276104,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 22397.583901906808,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 6.473846881087439,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 7.688333579181842,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 1608.8988831153297,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 29427.88776736697,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 9.287178395339263,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 12.396806258199518,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 17948.83432897184,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 31282.68127384457,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 37400.73418791101,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 36994.28159073723,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 32927.81154148935,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 894.2456529006863,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.265635706067393,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5298085471118752,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 75.92043185952126,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 579.4202438584929,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 2.223453594474618,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.41190803823519967,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 52.085169286268886,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 576.8929429728291,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 2.14830313215245,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4312807317288125,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 51.048641110494664,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 8784.874064236854,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.177401830201835,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 2.5529880756996874,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 425.1771828564718,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 1632.9488589191783,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 2.5758919602745287,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 2.9279861454584437,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 187.84340472812502,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3576.1379811218926,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 5370.454175708391,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3666.647361918076,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3625.2733936092272,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.9586614486349392,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.01470151259905259,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.006280947269666985,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.11381890494981879,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.701534819954184,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.6580646701134057,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5631256845185503,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.7097887595315484,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.7004482948188056,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.6877574588210652,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5197607957547123,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.7200535195863775,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 11015.709481816259,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.181431859739858,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 3.0783152072389823,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 659.4909387946631,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 7486.53321701133,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 7019.639227303863,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 5936.38620690434,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 7426.611036350434,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 7543.5120458636575,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 6804.317337158056,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 5830.907054672545,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 7201.7920568201835,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 7193.437753681428,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.147603538060129,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.866780105452047,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 432.86464363861387,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3183.4271596639833,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3848.250810344803,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 2429.1956861077933,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3168.380917123548,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 3205.1702611696865,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 3845.377553875345,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 2475.5042296347738,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 3266.238198973787,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.09959944671131968,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.016563012354268172,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.014808136860148483,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.5725004259353142,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.0726429969876238,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.07185219547461366,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.07049904749116634,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.07323113674464285,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.07267434618388455,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.0704807167774515,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.07075204216946514,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.07314862898220556,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.019148,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.11828,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 4.253869,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.096395,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.075183,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4.129171,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 4.289843,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.198429,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.058138,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 4.139656,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 4.372798,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.21891,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "name": "Rob Walpole",
            "username": "robwtelicent",
            "email": "183595007+robwtelicent@users.noreply.github.com"
          },
          "committer": {
            "name": "GitHub",
            "username": "web-flow",
            "email": "noreply@github.com"
          },
          "id": "6a66e9f0d2ec8380a06e1b19d31d7c7aabc1fa0a",
          "message": "Merge pull request #188 from telicent-oss/dependabot/maven/patches-5987f2af40\n\nBump the patches group with 3 updates",
          "timestamp": "2026-10-19T07:56:29Z",
          "url": "https://github.com/telicent-oss/smart-cache-storage/commit/6a66e9f0d2ec8380a06e1b19d31d7c7aabc1fa0a"
        },
        "date": 1792983926671,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 8615.74483034243,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.7637268785664535,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5548312471520667,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 451.35682687683965,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3547.7470282689537,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.7466024616351024,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5420550173655386,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 175.30412886820676,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 5524.626698184819,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.7413496047256345,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.548889346481982,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 4808.471992322799,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 12465.335216702673,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.579754503035797,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 3.629273367244206,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 601.5539975567439,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 8195.27354311867,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 8394.291437349799,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 7932.034685175859,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 9013.048035028249,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 8126.675834112047,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 8082.7772594137805,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 8536.43388892032,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 7199.531930131436,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA512\"} )",
            "value": 2958.1900991361426,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA256\"} )",
            "value": 12617.508230201245,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA512\"} )",
            "value": 2684.8660240087456,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA256\"} )",
            "value": 9742.546823590677,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA512\"} )",
            "value": 2944.598038205734,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA256\"} )",
            "value": 12947.208182698423,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 239.32918100530756,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.7121819070684807,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.505137414268229,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 140.6737652033512,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 36.49449915460921,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.6750574079263296,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4047057901710403,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 21.801322161796627,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 57.50477245679069,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.7146120777279592,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.49750329176996055,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 48.37325248959719,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 15116.565202645628,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.3774474288374945,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 5.707873608333283,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 1107.8786509296506,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 26932.126133786292,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 6.253477559702604,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 8.941511418341136,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 13722.776612132526,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 28681.482876828515,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 30408.499309967738,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 27920.80090246014,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 27817.49336582471,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 769.4280432022052,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.4585222861404366,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.6087764241589322,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 50.39090853238761,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 467.5587988030961,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.3916079183072871,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5696866878366039,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 30.136372316519118,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 456.03069086477524,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.3886795570446584,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5765585073160936,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 25.607551355309873,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 6264.472170113224,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.29839329058052,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.7502552970922736,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 323.2766719411812,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 1201.1631008149552,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 1.5701466535442157,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 2.0317470158057604,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 120.7613421032685,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 2865.0686863407273,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3689.107829304802,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 2700.2998361790683,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 2822.890534421451,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.7702367062671438,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.008794454860316514,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.004489164138259677,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.07787147107470357,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.4210950057276892,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.3948980116192513,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.35293714553486943,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.4133740257259341,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.4019205845412296,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.4082343544321939,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.3687947121510731,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.39446222393919894,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 9050.152721403227,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.4238750822439044,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 2.352602032760406,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 504.5932604112298,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 4184.753200119436,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4342.293622030978,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3801.365915667142,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 4392.039638750562,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 4056.618668902399,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 4239.98185636103,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 3803.2013234889428,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 4063.213315311709,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 5225.247125559372,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.2601633536241885,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.4351502466395438,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 328.18526207553094,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 2546.5299999125896,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 2969.74982113953,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 2132.2181680250337,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 2617.4514160879903,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 2625.673365648485,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 2845.6869971278074,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 2144.5757351294037,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 2593.326355687752,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.07898566238836344,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.013583126896534977,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.011697761898578556,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.5562678967749601,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.06276717931263771,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.06148963756908715,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.06112212394293439,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.06287371084402983,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.0626406263872987,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.061652197885102664,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.0610390770572725,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.06287907936704269,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.022182,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.562228,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 5.887967,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.144751,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.110727,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4.834778,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 6.497687,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.253424,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.079389,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 5.89465,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 5.035088,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.265187,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "name": "Rob Walpole",
            "username": "robwtelicent",
            "email": "183595007+robwtelicent@users.noreply.github.com"
          },
          "committer": {
            "name": "GitHub",
            "username": "web-flow",
            "email": "noreply@github.com"
          },
          "id": "ee9139e8d27c53ea7b27c939a91d3ebccb42cfb7",
          "message": "Merge pull request #190 from telicent-oss/tc_598_sq_common\n\n[TC-598] Use Core SQ suppressions",
          "timestamp": "2026-10-27T17:57:54Z",
          "url": "https://github.com/telicent-oss/smart-cache-storage/commit/ee9139e8d27c53ea7b27c939a91d3ebccb42cfb7"
        },
        "date": 1793587547887,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 12848.961018891818,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 5.536802043606928,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.6497297832203497,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 805.418108492195,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 4789.196687089478,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 5.161048829246364,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.6375910935240745,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 307.9209157890251,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 9605.396693011697,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 5076.9889585939,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.6415003401400619,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 8979.897862548049,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 18071.131520679264,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 6.520649307355093,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 10.193179776117546,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 1027.659255905244,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 13795.958505894032,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 14120.8911062155,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 14306.165003950484,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 13784.773600499393,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 14045.323888325409,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 13561.415516124714,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 13957.376181122574,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 13495.821309375286,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA512\"} )",
            "value": 6015.809590944378,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA256\"} )",
            "value": 17578.63183211416,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA512\"} )",
            "value": 5219.329702461548,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA256\"} )",
            "value": 12610.065647482863,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA512\"} )",
            "value": 5799.944486115068,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA256\"} )",
            "value": 17728.475242311128,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 276.05028854558583,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.5163451340665208,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5178019266343096,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 225.1659957469379,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 59.173691264141326,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.4988317684193384,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.49844567369416526,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 51.48418781454436,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 91.1231242216513,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.488945237227562,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4928290943210148,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 84.21527043005094,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 27436.21064816975,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 7.655071678160075,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 10.789227496539093,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 2040.906278945796,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 34581.06019210174,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 12.986064507875696,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 18.989049486266982,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 27311.009294006206,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 44491.44109928099,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 46547.5494282553,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 52492.05341523166,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 52349.26111033089,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 912.675069277788,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 3.8061084262130147,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.7963230423855963,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 98.55847096051322,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 517.6358886403173,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3.4587863521505424,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.7561620010446587,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 70.23334177675399,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 588.1209319421765,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 3.4336578005560425,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.7875598799670828,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 71.75925228905865,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 11003.992860668692,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 3.251159049812575,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 4.484250563300593,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 549.001706716891,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 1916.1384378854375,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 3.8375300707031643,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 5.272257736863969,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 230.89175476185082,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 4364.5402324567995,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 6263.972031482391,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 4498.5309075543355,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 4323.030994195178,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 1.166111622905658,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.023550521826154684,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.011220513050225037,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.1377211334554294,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.7565535301767505,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.7275183591965296,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.6442483645495278,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.7292023078568468,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.773102745388986,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.735861960473697,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.6660747816716803,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.7718778763146652,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 13458.169977248495,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 6.02727304834459,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 5.417829219110095,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 815.6191986952924,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 7564.66526560586,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 7079.333601051764,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 6627.4211344076375,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 7788.723547410924,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 8161.728562240796,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 7846.554380217266,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 7161.264182952749,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 8024.762790104704,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 8885.940004931805,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 3.1269070125806593,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 3.6350771318709265,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 537.4300874784919,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3920.0251370049336,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4877.374322495806,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3481.4994488003003,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3917.3647803348586,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 3863.119331411412,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 4617.003231802886,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 3410.8690770934786,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 3885.5944068345025,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.1993857499599036,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.019952271944226403,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.021499723260758795,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.8233025019952199,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.09744520110207777,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.0968059193805639,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.09836850330833655,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.10020661417656032,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.09881108829817815,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.09736234977511724,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.09455812180637099,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.09480276068260714,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.021142,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.45491,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 5.181909,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.103376,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.073551,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4.369108,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 6.464365,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.281384,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.077026,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 4.349244,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 3.97976,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.237138,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "name": "Paul Gallagher",
            "username": "TelicentPaul",
            "email": "132362215+TelicentPaul@users.noreply.github.com"
          },
          "committer": {
            "name": "GitHub",
            "username": "web-flow",
            "email": "noreply@github.com"
          },
          "id": "aa12fa95ee6a8b123a7db5a018ad19426eb86ff2",
          "message": "Merge pull request #194 from telicent-oss/release/0.15.3\n\nComplete Release 0.15.3",
          "timestamp": "2026-11-06T14:50:18Z",
          "url": "https://github.com/telicent-oss/smart-cache-storage/commit/aa12fa95ee6a8b123a7db5a018ad19426eb86ff2"
        },
        "date": 1794192464750,
        "tool": "jmh",
        "benches": [
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 10896.099055469977,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 3.4925344138099645,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4375185093090341,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 545.193871430032,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 4241.211417579051,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3.2611820486494665,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4638902263974531,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 255.34176013633538,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 6892.717685527838,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 2108.8223650235745,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4311422726387676,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForRepeatedLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 6434.770235067139,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 16134.668580165773,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.482173743350072,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 5.6475224200426,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 757.0135646835582,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 10225.622874462035,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 9540.77770347529,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 9534.306816896376,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 10243.341002445955,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 9889.999516122873,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 9914.141683759894,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 9846.837322036134,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.BasicBenchmark.getIdForSameLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 10083.431819848203,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA512\"} )",
            "value": 3728.8374858326847,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestHelper ( {\"algorithm\":\"SHA256\"} )",
            "value": 14570.30573410021,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA512\"} )",
            "value": 3458.552452298894,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerComputation ( {\"algorithm\":\"SHA256\"} )",
            "value": 11608.00052089286,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA512\"} )",
            "value": 3721.0910131772193,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.DigestBenchmark.digestInstancePerThread ( {\"algorithm\":\"SHA256\"} )",
            "value": 16089.155568047823,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 4"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 242.60510305680518,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 1.1735347506925815,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.34671854954240133,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 171.58764645835993,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 45.387179913466795,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 1.1377293508724249,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4277438239828716,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 38.32206478991075,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 67.9788089154072,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 1.126500913125561,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.3362689346546489,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.LargeLabelsBenchmark.getIdForLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 60.85943704825396,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 22485.349054784692,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 5.540800044369705,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 7.5151856586709815,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 1238.3818242947139,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 26283.86739656992,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 9.386430380115396,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 12.933711016290292,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 21099.496576431426,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 38721.7636919023,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 32404.047769895005,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 38104.095179339914,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.MultiThreadedBenchmark.ReadWrite ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 38062.64415625524,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 9"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 772.6042203649469,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.2981105671887896,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4943626451675509,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 72.30169188322925,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 443.519019849044,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 2.192844211575833,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.36058566296980604,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 52.57273735284501,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 431.08590867687286,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 2.145896090131986,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.5112723735315027,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.PathologicalBenchmark.getIdForAlwaysUniqueLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 51.47884737737156,
            "unit": "ops/ms",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 8577.117273674272,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.2136057812092647,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 2.6665914634794072,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 403.51905852112304,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Memory\"} )",
            "value": 1470.8912301603784,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"Postgres\"} )",
            "value": 2.581415030012047,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"MongoDB\"} )",
            "value": 3.015441142214617,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"100\",\"implementation\":\"RocksDB\"} )",
            "value": 153.69705915604445,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 3219.674697797748,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4673.000704573034,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 3089.324422460777,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ReadOnlyBenchmark.readOnly ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 3239.7661983379244,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.9337353594034756,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.014562282542998262,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.00652837404868749,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.10640685384930967,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.617244539441943,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.5986707564860608,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4807459232030059,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.6504629054565914,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.6274959001717588,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.5899022771375896,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.4879769961714282,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.bulkIdsForLabels ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.6520346093812921,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 10948.512651915033,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.34154691808514,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 3.315145904110883,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 629.6423920081388,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 6875.324466263157,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 6240.992568112122,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 5323.045399163335,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 6549.874040872206,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 6607.832698717624,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 6165.704574220543,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 5591.775827251749,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 6695.492343389039,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 6933.906960401449,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 2.2174630290354544,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 1.9571165006956375,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 402.16380964382233,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 2582.2616686331357,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 3494.879549416664,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 2171.103230508371,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 2653.2078961552593,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 2661.8061933436934,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 3499.7040789640982,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 1821.2378141806876,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.RealWorldBenchmark.getIdForLabel_andResolveIdToLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 2541.186373932931,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.10113135395277743,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 0.01624646287606484,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 0.014737439200056573,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.6668005303634024,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.07258960705436387,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 0.07177320164822515,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 0.06983779467168569,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.07260060476587196,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.07254164351452319,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 0.07152219557983493,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 0.07002078350522067,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.VeryLargeLabelBenchmark.getIdForVeryLargeLabel ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.07281552456714445,
            "unit": "ops/ms",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Memory\"} )",
            "value": 0.020861,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"Postgres\"} )",
            "value": 4.063003,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"MongoDB\"} )",
            "value": 4.099252,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"0\",\"implementation\":\"RocksDB\"} )",
            "value": 0.126958,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Memory\"} )",
            "value": 0.052738,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"Postgres\"} )",
            "value": 4.194725,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"MongoDB\"} )",
            "value": 5.074019,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"500\",\"implementation\":\"RocksDB\"} )",
            "value": 0.186987,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Memory\"} )",
            "value": 0.163002,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"Postgres\"} )",
            "value": 3.954212,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"MongoDB\"} )",
            "value": 4.425305,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          },
          {
            "name": "io.telicent.smart.cache.storage.labels.benchmarks.ToyBenchmark.getIdForUniqueLabel_toy ( {\"cacheSize\":\"10000\",\"implementation\":\"RocksDB\"} )",
            "value": 0.184404,
            "unit": "ms/op",
            "extra": "iterations: 1\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}