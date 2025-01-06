# Smart Cache - Storage Libraries

This repository provides helper libraries around common storage layers for Smart Caches, this aims to allow
developers to focus effort on the actual details of a particular Smart Cache implementation rather than on the
boilerplate involved in using a particular storage.

# Requirements

This is a Java based project that requires Maven to build.

Some tests **MAY** require Docker, you can disable this via the `docker` profile, i.e. `-P-docker`, if you don't
have a working Docker environment.

# Modules

There is only a single storage module provided currently:

## Hibernate

The `hibernate` module provides the building blocks for working with Hibernate+JPA backed storage. This works at
the Hibernate/JPA API level so any underlying storage supported by Hibernate/JPA may be used.

The API consists of two core classes:

- `AbstractHibernateStorage` - An abstract implementation of a Hibernate backed store.
- `TransactionContext` - A transaction context.

Developers using this API should have their storage classes extend `AbstractHibernateStorage`, and then use the
various protected methods to implement their actual storage. See the test [`OrderManager`][1] implementation for an
exemplar of this pattern.

[1]: hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/model/OrderManager.java

`TransactionContext` is an internal helper interface used to allow developers to access the Hibernate `Session` and
JPA `EntityManager` with minimal fuss. A `TransactionContext` can be obtained by calling the
`AbstractHibernateStorage.begin()` method. It is `AutoCloseable` meaning it **SHOULD** be used in a try-with-resources
block and the transaction will be rolled back if the developer does not `commit()` it in their code. Again refer to
the test [`OrderManager`][1] class to see this pattern in use.

## Depending on these modules

These modules have the Maven Group ID `io.telicent.smart-caches.storage`, and the Maven Artifact IDs noted above.  
You can add a dependency like so:

```xml

<dependency>
    <groupId>io.telicent.smart-caches.storage</groupId>
    <artifactId>hibernate</artifactId>
    <version>X.Y.Z</version>
</dependency>
```

Where `X.Y.Z` is the desired version, refer to the [`CHANGELOG.md`] for available versions and latest changes.


