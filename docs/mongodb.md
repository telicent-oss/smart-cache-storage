# Smart Cache Storage - MongoDB

The `mongodb` module provides the building blocks for working with MongoDB, and other MongoDB API compatible storage
e.g. AWS DocumentDB.  This is built using the official MongoDB Sync Driver and the MongoJack library that provides
Jackson integration with MongoDB allowing for any Jackson serializable type to be read/written to a MongoDB collection.

The API consists of a single `AbstractMongoStorage` class which should be extended and its helper methods used to
implement your actual storage logic.  See
[`UserDataStore`][UserDataStore] or [`MongoDBLabelsStore`][MongoLabelsStore] for
exemplars of this.  Most of the helper methods revolve around first obtaining a `JacksonMongoCollection<T>` via the
`getCollection()` method and then passing that into relevant helper methods.

## Entity Requirements

The only requirement for using is that your entity classes **MUST** either have a `@ObjectId`/`@Id` annotated field defined, see
[`@ObjectId`][ObjectIds] documentation for more details.  See the test
[`User`](mongodb/src/test/java/io/telicent/smart/cache/storage/mongodb/model/User.java) class for an example of this. As
noted in the MongoJack documentation if you use `@Id` instead of `@ObjectId` then your code **MUST** ensure the ID is
approriately generated and populated.

## MongoDB Configuration

Connecting to MongoDB requires providing a `MongoClient` that you have configured appropriately, and the name of the
database you wish to access.  To help API consumers with this a `MongoConfiguration` object is provided, that can be
automatically populated from configuration obtained using the Smart Caches [Configurator][Configurator] API by using the
static `MongoConfiguration.fromConfigurator()` method.  Once you have an instance of this it provides access to both the
configured `MongoClient` and the database to connect to via that client.

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

Note that the Mongo test cluster will have a default database of `test` available for use in your tests, available as a
constant `MongoTestCluster.DEFAULT_TEST_DB`.  The `resetCollection()` method shown above assumes that database.  You can
of course create and work with multiple databases in your tests if necessary.

[UserDataStore]: ../mongodb/src/test/java/io/telicent/smart/cache/storage/mongodb/model/UserDataStore.java
[ObjectIds]: https://mongojack.org/object-ids.html
[MongoLabelsStore]: ../label-stores/label-store-mongodb/src/main/java/io/telicent/smart/cache/storage/labels/mongodb/MongoDBLabelsStore.java
[Configurator]: https://github.com/telicent-oss/smart-caches-core/blob/main/docs/configurator/index.md
