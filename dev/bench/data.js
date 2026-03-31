window.BENCHMARK_DATA = {
  "lastUpdate": 1774972203857,
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
      }
    ]
  }
}