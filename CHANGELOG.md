# Smart Cache - Storage Libraries - Change Log

# 0.4.0

- Introduced `MongoTestCluster` interface in `tests` classifier of `mongodb` module
    - New `BasicMongoTestCluster` implementation for plain MongoDB cluster
    - New `SecureMongoTestCluster` for MongoDB cluster with authentication enabled
- Build improvements:
    - Upgraded Apache Commons IO to 2.19.0
    - Upgraded Hibernate to 6.6.13.Final
    - Upgraded Hypersistence Utils to 3.9.10
    - Upgraded MongoDB to 5.4.0
    - Upgraded Postgres to 42.7.5
    - Upgraded Smart Caches Core to 0.28.2

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
