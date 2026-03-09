# Smart Cache - Storage Libraries - Change Log

# 0.9.1
- Build improvements:
  - Addressing CVE-2026-1605 (Jetty)

# 0.9.0

- Hibernate improvements:
    - Hibernate Hikari Connection Pool is now provided as a dependency by default so will automatically be used if
      persistence.xml includes any hibernate.hikari.* properties
- Build improvements:
    - **BREAKING** Minimum Java version is now 21
    - Added Hibernate Hikari Connection Pool
    - Upgraded Flyway to 12.0.3
    - Upgraded Jackson 3 to 3.1.0
    - Upgraded Hibernate to 7.2.6.Final
    - Upgraded Hypersistence Utils to 3.15.2
    - Upgraded MongoDB to 5.6.4
    - Upgraded Postgres to 42.7.10
    - Upgraded Smart Caches Core to 0.36.1

# 0.8.0

- Label store improvements:
    - Provided a thread-safe `DigestHelper` for use in label store implementations which need to compute digests of
      labels to ensure they can do so safely
    - Existing implementations updated to use this
- Build improvements:
    - Upgraded Apache Commons Codec to 1.21.0
    - Upgraded Flyway to 12.0.0
    - Upgraded Hibernate to 7.2.3.Final
    - Upgraded Hypersistence Utils to 3.15.1
    - Upgraded MongoDB to 5.6.3
    - Upgraded MongoJack to 5.1.0
    - Upgraded Smart Caches Core to 0.35.0
    - Added additional benchmarks around digest computation

# 0.7.0

- Hibernate improvements:
    - `DatabaseConfiguration` can now optionally specify the full JDBC URL to use via new configuration variable
      `DATABASE_JDBC_URL` which may be the preferred way to configure it in some circumstances.
        - **BREAKING CHANGE** Previously insufficient configuration would cause a NPE, callers should instead use the
          new `isValid()` method to test whether it is sufficiently populated
        - Related helpers like `PostgresConfiguration.prepareConnectionProperties()` may instead now throw
          `IllegalArgumentException` if passed invalid configuration
- Build improvements:
    - Upgraded Apache Commons Codec to 1.20.0
    - Upgraded Flyway to 11.20.2
    - Upgraded Hibernate to 7.2.1.Final
    - Upgraded Postgres to 42.7.9

# 0.6.1

- Hibernate improvements:
    - Fixed a version misalignment between Hibernate and Hypersistence Utils dependencies that caused
      `@Type(JsonType.class)` typed entity fields to trigger Hibernate errors after upgrading to `0.6.0`
    - Added additional unit test coverage for JSON typed fields to ensure that this misalignment cannot get silently
      reintroduced in future
- Build improvements:
    - Realigned Hypersistence Utils to Hibernate 7.1 target

# 0.6.0

- Hibernate improvements:
    - Added optional Flyway schema migration support by overriding the `configureFlyway()` method in storage
      implementations derived from `AbstractHibernateStorage`
- New `DictionaryLabelsStore` and `LabelStores` APIs in the `common` module along with caching decorator implementations
- New `rocksdb` module provides `AbstractRocksDBStorage` and associated helper classes to abstract some of the low level
  details of RocksDB storage implementations
- New `label-stores` parent module with various concrete implementation submodules:
    - `label-store-hibernate` for Hibernate backed implementation
    - `label-store-mongodb` for MongoDB backed implementation
    - `label-store-rocksdb` for RocksDB backed implementation
    - `label-store-alternates` for other experimental implementations
    - Plus `label-store-benchmarks` module for JMH benchmarking of implementations
- Build improvements:
    - Upgraded Hibernate to 7.1.10.Final
    - Upgraded Smart Caches Core to 0.32.1
    - Added new Flyway 11.20.1 dependency
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
