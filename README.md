# Smart Cache - Storage Libraries

This repository provides helper libraries around common storage layers for Smart Caches, this aims to allow developers
to focus effort on the actual details of a particular Smart Cache implementation rather than on the boilerplate involved
in using a particular kind of storage backend.

# Requirements

This is a Java based project that requires Maven to build.

Some tests **MAY** require Docker, you can disable this via the `docker` profile, i.e. `-P-docker`, if you don't have a
working Docker environment.

# Modules

The following modules are currently provided

- [`hibernate`](#hibernate)
- [`mongodb`](#mongodb)

## Hibernate

The `hibernate` module provides the building blocks for working with Hibernate+JPA backed storage, which is generally
some form of relational database. This uses the Hibernate/JPA API so any underlying storage supported by Hibernate/JPA
may be used.

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

### Hibernate Configuration

The `AbstractHibernateStorage` class requires derived classes to supply database connection as a Java `Properties`
object and the name of your persistence unit. The expectation here is that your `persistence.xml` that defines your JPA
Persistence Unit contains only the generic configuration e.g. Entity classes/packages, sets the `<provider>` and basic
properties e.g.

```xml

<persistence xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
             version="2.0">

    <persistence-unit name="example" transaction-type="RESOURCE_LOCAL">
        <!-- Set the provider to Hibernate -->
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <!-- Your entity classes -->
        <class>your.package.SomeClass</class>
        <class>your.package.AnotherClass</class>

        <properties>
            <!--
            NB - No database connection properties are supplied in this persistence unit intentionally
            -->
            <!-- Don't automatically create the schema -->
            <property name="jakarta.persistence.schema-generation.database.action" value="none"/>

            <!-- Echo all executed SQL to console -->
            <!-- Uncomment when debugging if you're changing the model or adding new named queries -->
            <!--<property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.highlight_sql" value="true"/>-->
        </properties>
    </persistence-unit>
</persistence>
```

Then at runtime you populate and supply a `Properties` object with the necessary JPA configuration keys set.

To help API consumers with this a `DatabaseConfiguration` object is provided, that can be automatically populated from
configuration obtained using the Smart Caches [Configurator][2] API by using the static `DatabaseConfiguration.
fromConfigurator()` method. There are also a `PostgresConfiguration` and `H2Configuration` classes that provide static
methods that take in a `DatabaseConfiguration` object and product a populated `Properties` object with the necessary JPA
configuration set for those database backends. Again you can find examples of these APIs being used in the various test
classes under [`hibernate/src/test/io/telicent/smart/cache/storage/hibernate/`]
(hibernate/src/test/io/telicent/smart/cache/storage/hibernate).

[2]: https://github.com/telicent-oss/smart-caches-core/blob/main/docs/configurator/index.md

## MongoDB

The `mongodb` module provides the building blocks for working with MongoDB, and other MongoDB API compatible storage
e.g. AWS DocumentDB.  This is built using the official MongoDB Sync Driver and the MongoJack library that provides
Jackson integration with MongoDB allowing for any Jackson serializable type to be read/written to a MongoDB collection.

The only requirement is that your entity classes **MUST** either have a `@ObjectId`/`@Id` annotated field defined, see
[`@ObjectId`][3] documentation for more details.  See the test
[`User`](mongodb/src/test/java/io/telicent/smart/cache/storage/mongodb/model/User.java) class for an example of this. As
noted in the MongoJack documentation if you use `@Id` instead of `@ObjectId` then your code **MUST** ensure the ID is
approriately generated and populated.

[3]: https://mongojack.org/object-ids.html

Connecting to MongoDB requires providing a `MongoClient` that you have configured appropriately, and the name of the
database you wish to access.

The API consists of a single `AbstractMongoStorage` class which should be extended and its helper methods used to
implement your actual storage logic.  See
[`UserDataStore`](mongodb/src/test/java/io/telicent/smart/cache/storage/mongodb/model/UserDataStore.java) for an
exemplar of this.  Most of the helper methods revolve around first obtaining a `JacksonMongoCollection<T>` via the
`getCollection()` method and then passing that into relevant helper methods.

### Testing with MongoDB

To aid testing against MongoDB in unit/integration tests we provide a `MongoTestCluster` interface in our `tests`
classifier for this module.  This has two concrete implementations:

- `BasicMongoTestCluster` - For a plain MongoDB cluster with no authentication.
- `SecureMongoTestCluster` - For a MongoDB cluster that requires authentication.

In your unit tests you create an instance of one of these and then call the appropriate methods in your test
setup/teardown, and actual test methods, as appropriate e.g.

```java
public class TestMongoDBExample {
    private final MongoTestCluster mongo = new BasicMongoTestCluster();

    @BeforeClass
    public void setup() {
        this.mongo.setup();
    }

    @AfterClass
    public void teardown() {
        this.mongo.teardown();
    }

    @AfterMethod
    public void reset() {
        try (MongoClient client = this.mongo.createMongoClient()) {
            MongoTestCluster.resetCollection(client, "some-collection")
        }
    }

    @Test
    public void testSomething() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            // When
            // Perform some action with the client

            // Then
            // Verify the results of that action
        }
    }
```

Note that the Mongo test cluster will have a default database of `test` available for use in your tests, available as a constant `MongoTestCluster.DEFAULT_TEST_DB`.  The
`resetCollection()` method shown above assumes that database.  You can of course create and work with multiple databases
in your tests if necessary.

## Depending on these modules

These modules have the Maven Group ID `io.telicent.smart-caches.storage`, and the Maven Artifact IDs noted above.  You
can add a dependency like so:

```xml
<dependency>
    <groupId>io.telicent.smart-caches.storage</groupId>
    <artifactId>MODULE</artifactId>
    <version>X.Y.Z</version>
</dependency>
```

Where `MODULE` is the desired storage module and `X.Y.Z` is the desired version, refer to the [`CHANGELOG.md`] for
available versions and latest changes.

## License

Copyright 2024-2025 and All Rights Reserved Telicent Ltd, see [LICENSE](LICENSE) and [NOTICE](NOTICE).


