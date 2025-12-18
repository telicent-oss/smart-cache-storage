# Smart Cache - Storage Libraries - Change Log

# 0.6.0

- Hibernate improvements:
    - Added optional Flyway schema migration support by overriding the `prepareFlywayConfiguration()`
      method in storage implementations derived from `AbstractHibernateStorage`
- New `DictionaryLabelsStore` and `LabelStores` APIs in the `common` module along with caching decorator implementations
- New `rocksdb` module provides `AbstractRocksDBStorage` and associated helper classes to abstract some of the low level
  details of RocksDB storage implementations
- New `label-stores` module with various submodules:
    - `label-store-hibernate` for Hibernate backed implementation
    - `label-store-mongodb` for MongoDB backed implementation
    - `label-store-rocksdb` for RocksDB backed implementation
    - `label-store-alternates` for other experimental implementations
    - Plus `label-store-benchmarks` for JMH benchmarking of implementations
- Build improvements:
    - Upgraded Hibernate to 7.1.10.Final
    - Upgraded Smart Caches Core to 0.32.1
    - Added new Flyway 11.19.0 dependency
    - Added new RocksDB 10.2.1 dependency
    - Added new JMH 1.37 dependency
    - Various build and test dependencies upgraded to latest available 

# 0.5.2

- Build improvements
    - Upgraded Hibernate to 7.1.8.Final
    - Upgraded Hypersistence Utils to 3.12.0
    - Upgraded Smart Caches Core to 0.31.1

# 0.5.1

- Updated README to properly reflect improvements to Mongo support that were added back in 0.4.0
- Build improvements:
    - Upgraded Hibernate to 7.1.3.Final
    - Upgraded MongoDB to 5.6.1
    - Upgraded Smart Caches Core to 0.30.1
    - Various build and test dependencies upgraded to latest available

# 0.5.0

- Build improvements:
    - Upgraded Hibernate to 7.1.1.Final
    - Upgraded HyPersistence Utils to 3.11.0 and Hibernate 7 target
    - Upgraded MongoDB to 5.6.0
    - Upgraded Mongojack to 5.0.3
    - Upgraded Postgres to 42.7.8
    - Upgraded Smart Caches Core to 0.29.6
        - Removed dependency properties that were unnecessarily duplicated those inherited from Smart Caches Core

# 0.4.1

- Mongo Configuration improvements:
    - Wraps low-level exceptions from parsing invalid `MONGO_URL` into a more informative error 

# 0.4.0

- Mongo Configuration improvements:
    - Logs more information about detected and applied configuration
    - If `authSource` is specified in `MONGO_URL` use that in preference to default unless `MONGO_AUTH_DATABASE` is
      explicitly configured, log a warning if these two values are mismatched
    - If user credentials are specified in `MONGO_URL` use those unless `MONGO_USER` and `MONGO_PASSWORD` are set, log a
      warning if the credentials are mismatched
    - If database is specified in `MONGO_URL` use that in preference to default unless `MONGO_DATABASE` is specified,
      log a warning if the credentials are mismatched.
- Introduced `MongoTestCluster` interface in `tests` classifier of `mongodb` module
    - New `BasicMongoTestCluster` implementation for plain MongoDB cluster
    - New `SecureMongoTestCluster` for MongoDB cluster with authentication enabled
- Build improvements:
    - Upgraded Apache Commons IO to 2.19.0
    - Upgraded Hibernate to 6.6.13.Final
    - Upgraded Hypersistence Utils to 3.9.10
    - Upgraded MongoDB to 5.4.0
    - Upgraded Postgres to 42.7.5
    - Upgraded Smart Caches Core to 0.29.0

# 0.3.2

- Added `MongoConfiguration` class to `mongodb` module
- Added ability to supply default values to `DatabaseConfiguration.fromConfigurator()`

# 0.3.1

- Build improvements:
    - Upgrade Smart Cache Core Libraries to 0.26.1

# 0.3.0

- New `mongodb` module introduced

# 0.2.1

- Build and test improvements:
    - Fix test dependency incorrectly leaked to `compile` scope

# 0.2.0

- Add improved Javadoc to `AbstractHibernateStorage`
- **BREAKING** Renamed `beginInternal()` to `begin()` on AbstractHibernateStorage
- `ShortLivedTransactionContext` provides stronger guarantees around cleaning up transactional resources
- Build and test improvements:
    - Increased test coverage significantly 
    - Upgraded Smart Caches Core Libraries to 0.25.2

# 0.1.2

- Use correct private workflow
 
# 0.1.1

- Setup GitHub Workflows to get automated releases working correctly

# 0.1.0

- Initial release of `hibernate` module with initial `AbstractHibernateStorage` and `TransactionContext` APIs
