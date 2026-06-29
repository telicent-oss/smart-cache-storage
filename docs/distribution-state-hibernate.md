# Smart Cache Storage - Distribution State Hibernate

Since `0.13.0` the `distribution-state-hibernate` module provides a [Hibernate](hibernate.md) storage backed
implementation of the [`DistributionLifecycleStateStore`][DistLifeStateStore] API from Smart Caches Core.

This is intended for use by lifecycle aware services that want to share a central state store rather than maintain their
own state store.  The store is capable of tracking acknowledgements from many applications so can also be used to back a
central distribution lifecycle management API.

## Creating the State Store

As with all our Hibernate storage creating an instance of the storage requires providing a `Properties` object with
appropriate JPA configuration properties, the [`DatabaseConfiguration`](hibernate.md#hibernate-configuration) API can be
used to help prepare those properties.

```java
// Assumes the props object has been populated with necessary JPA properties
DistributionLifecycleStateStore stateStore
  = new HibernateDistributionLifecycleStateStore(props);
```

Once you have an instance of this state store then you can pass it to any of the
[`distribution-lifecycle`][DistLifecycle] APIs that require a state store.

[DistLifecycle]: https://github.com/telicent-oss/smart-caches-core/blob/main/docs/distribution-lifecycle/index.md
[DistLifeStateStore]: https://github.com/telicent-oss/smart-caches-core/blob/main/docs/distribution-lifecycle/index.md#state-stores
