# Smart Cache - Storage Libraries - Change Lang

# 0.2.0

- Add improved Javadoc to `AbstractHibernateStorage`
- **BREAKING** Renamed `beginInternal()` to `begin()` on AbstractHibernateStorage
- `ShortLivedTransactionContext` provides stronger guarantees around cleaning up transactional resources

# 0.1.2

- Use correct private workflow
 
# 0.1.1

- Setup GitHub Workflows to get automated releases working correctly

# 0.1.0

- Initial release of `hibernate` module with initial `AbstractHibernateStorage` and `TransactionContext` APIs
