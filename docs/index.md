# Smart Cache Storage Documentation

The libraries in this repository are designed to simplify boilerplate of creating storage backends without the baggage
of heavyweight APIs like Spring Data.  The individual modules are all built around commonly used Java libraries for the
relevant storage backends so should be familiar to most developers while helping to eliminate the grunt work of building
storage implementations.

# Modules

The following modules are currently provided

- [`hibernate`](hibernate.md)
- [`mongodb`](mongodb.md)
- [`rocksdb`](rocksdb.md)
- [`label-stores`](label-stores.md)

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

Where `MODULE` is the desired storage module and `X.Y.Z` is the desired version, refer to the
[`CHANGELOG.md`](../CHANGELOG.md) for available versions and latest changes.

### Additional Maven Repositories

For those using the [`hibernate`](hibernate/) module an additional Maven repository may need to be defined in order to
successfully resolve the Redgate published Flyway libraries:

```xml
<repository>
    <id>redgate</id>
    <url>https://download.red-gate.com/maven/release</url>
</repository>
```
