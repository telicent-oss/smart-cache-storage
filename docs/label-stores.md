# Smart Cache Storage - Label Stores

From `0.6.0` onwards these libraries provide two new APIs as part of the base `common` module:

- [`DictionaryLabelsStore`](#dictionarylabelsstore)
- [`LabelsStore`](#labelsstore)

Then in the `label-stores` module there are a number of child modules that provide concrete implementations of these
APIs:

- [`label-store-hibernate`](#hibernatelabelsstore)
- [`label-store-mongodb`](#mongodblabelsstore)
- [`label-store-rocksdb`](#rocksdblabelsstore)

Additionally there are also [caching decorators](#caching-decorators) that may be placed around these implementations to
improve performance by avoiding interactions with the underlying storage wherever possible.

## APIs

### `DictionaryLabelsStore`

The `DictionaryLabelsStore` API provides for mapping from arbitrary label `byte[]` sequences to `long` IDs.  Given a
particular `byte[]` sequence an implementation **MUST** consistently return the same `long` ID i.e. each unique label
byte sequence **MUST** have one, and only one, unique ID assigned to it by the store.

This API is intended for use cases where labels are already stored alongside data in an existing data store and the goal
is to minimise storage usage by storing compact identifiers for labels, rather than the labels themselves.  With a
`DictionaryLabelStore` each unique label byte sequence is stored at most twice (depends on the details of the underlying
implementation) and callers can translate from label `byte[]` to label `long`, and vice versa, as needed.  As the label
ID, in the form of a `long`, has a fixed small size (8 bytes), this offers large storage savings for stores that
currently store label `byte[]` sequences repeatedly.

The API provides for both singular operations, `idForLabel(byte[])` and `labelForId(long)`, as well as bulk operations,
`idsForLabels(List<byte[]>)` and `labelsForIds(List<Long>)`, so callers can use the most appropriate methods depending
on whether they process labelled data in bulk or not.

Regardless we always recommend that [Caching Decorators](#caching-decorators) are used to optimise performance and avoid
unncessary operations upon the underlying storage.

### `LabelsStore`

The `LabelStore` API extends the `DictionaryLabelStore` API with the ability to associated label IDs, in the form of
`long`'s, against arbitrary keys in the form of `byte[]` sequences.  Each key may be associated with one, and only one,
label ID.

This API is intended for use cases where labels are stored separately from the primary data store, e.g. because the
primary data store isn't customisable.  The caller is responsible for generating keys that identify items in the primary
data store in a repeatable and consistent way such that it can read/write the label ID associated with a given item of
data.

The API provides for singular, or bulk, write operations in the form of the `setLabel(byte[], long)` and
`setLabels(Map<byte[], Long>)` methods.  For read only singular operations are currently provided, `getLabel(byte[])`
and `getLabelAsBytes(byte[])`.  The latter method is equivalent to first calling `getLabel(byte[])` and then passing the
returned label ID to the `labelForId(long)` method, and indeed the default method implementation does just that, actual
implementations may optimise this implementation appropriately.

Regardless we always recommend that [Caching Decorators](#caching-decorators) are used to optimise performance and avoid
unncessary operations upon the underlying storage.

## Implementations

In all cases we would always recommend using [Caching Decorators](#caching-decorators) over any concrete implementation
in order to eliminate calls to the underlying storage where unnecessary.

### `HibernateLabelsStore`

The `label-store-hibernate` module provides the `HibernateLabelsStore` implementation, this uses two database tables:

1. `ENCODED_LABELS` to map from label `byte[]` sequences to label `long` IDs.
2. `ASSIGNED_LABELS` to map from key `byte[]` sequences to label `long` IDs.

Label `byte[]` sequences are stored as Base64 encoded text fields as this allows the database to index on the field,
which a blob field would not, which is key for performance.

This is based upon our normal [`hibernate`](hibernate.md) storage so creating a store means providing a `Properties`
object with the necessary JPA properties to connect to the underlying database.

### `MongoDBLabelsStore`

The `label-store-mongodb` module provides the `MongoDBLabelsStore` implementation, this uses two collections:

1. `encoded-labels` to store the mapping from label `byte[]` sequences to `long` label IDs.
2. `assigned-labels` to store the mapping from key `byte[]` sequences to `long` label IDs.

This is based upon our normal [`mongodb`](mongodb.md) storage so creating a store means providing a `MongoClient` and a
database name to connect to the MongoDB database.

### `RocksDBLabelsStore`

The `label-store-rocksdb` module provides the `RocksDBLabelStore` implementation, this uses several column families:

1. Storing a mapping from label `byte[]` sequences to `long` label IDs.
2. Storing the reverse mapping from `long` label IDs to label `byte[]` sequences.
3. Storing the mapping from key `byte[]` sequences to `long` label IDs.
4. Storing a persistent counter used to assign fresh label IDs to previously unknown labels `byte[]` sequences.

This is based upon our normal [`rocksdb`](rocksdb.md) storage so creating a store means providing a `File` representing
the directory on disk where the label store is/will be persisted.

### Caching Decorators

Also in the `common` module you will find `CachingDictionaryLabelsStore` and `CachingLabelsStore` decorators that may be
placed around any implementation of `DictionaryLabelsStore` or `LabelsStore` to provide caching over the underlying
implementation.

Caching uses the Caffeine library and cache sizes may be set either as a fixed size for all caches, or individually
customised.  For example a read-only usage of the API would probably want to set the cache size for the write operations
(`labelsToIdsCacheSize`) to a minimal number, while setting the cache sizes for read operations (`idsToLabelsCacheSize`)
to a larger number.

## Contract Testing

The `tests` classifier of the `common` modules contains two contract test suites that can be used by any implementation
of the `DictionaryLabelsStore` or `LabelsStore` interfaces to ensure that they are implementing the API contract
correctly.  These are as follows:

- `AbstractDictionaryLabelStoreTests`
- `AbstractLabelStoreTests`

To use these contracts tests extend the appropriate one of these classes in your own implementations test suite and
override the `newDictionaryStore()` or `newStore()` methods as appropriate, and add any necessary resource
setup/teardown your implementation might require.  For example here's the test class for the
[`RocksDBLabelsStore`](#rocksdblabelsstore) with `import` directives omitted for brevity:

```java
public class TestRocksDBLabelStore extends AbstractLabelStoreTests {

    private File rocksDir;

    @Override
    protected LabelsStore newStore() {
        try {
            return new RocksDbLabelsStore(rocksDir.getAbsoluteFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeMethod
    public void setup() throws IOException {
        rocksDir = Files.createTempDirectory("rocks").toFile();
    }

    @AfterMethod
    public void cleanUp() throws IOException {
        // Walk and delete directory tree properly
        FileUtils.deleteDirectory(rocksDir);
    }
}
```

Note that the `newDictionaryStore()` or `newStore()` methods **MUST** return a fresh and empty store instance each time
they are called otherwise test isolation will not be maintained and the test suite will likely report many failures.
