# Smart Cache Storage - Hibernate

The `hibernate` module provides the building blocks for working with Hibernate+JPA backed storage, which is generally
some form of relational database. This uses the Hibernate/JPA API so any underlying storage supported by Hibernate/JPA
may be used.

The API consists of two core classes:

- `AbstractHibernateStorage` - An abstract implementation of a Hibernate backed store.
- `TransactionContext` - A transaction context.

Developers using this API should have their storage classes extend `AbstractHibernateStorage`, and then use the various
protected methods to implement their actual storage. See the test [`OrderManager`][OrderManager] or 
[`HibernateLabelsStore`][HibLabelsStore] implementations for exemplars of this pattern.

## Transaction Contexts

`TransactionContext` is an internal helper interface used to allow developers to access the Hibernate `Session` and JPA
`EntityManager` in a transactionally safe manner with minimal fuss. A `TransactionContext` can be obtained by calling
the `AbstractHibernateStorage.begin()` method. It is `AutoCloseable` meaning it **SHOULD** be used in a
try-with-resources block and the transaction will be rolled back if the developer does not explicitly `commit()` it in
their code. Again refer to the test [`OrderManager`][OrderManager] class to see this pattern in use.

## Hibernate Configuration

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
configuration obtained using the Smart Caches [Configurator][Configurator] API by using the static
`DatabaseConfiguration.fromConfigurator()` method.  This supports the following configuration variables:

| Variable                            | Purpose                                       |
|-------------------------------------|-----------------------------------------------|
| `DATABASE_JDBC_URL`                 | Full JDBC URL for connecting to the database. | 
| `DATABASE_HOST`                     | Hostname to connect to the database on.       |
| `DATABASE_PORT`                     | Port to connect to the database on.           |
| `DATABASE_NAME`                     | Name of the database to connect to.           |
| `DATABASE_USER`/`DATABASE_USERNAME` | Username for authenticating to the database.  |
| `DATABASE_PASSWORD`                 | Password for authenticating to the database.  |

Note that in order to be considered valid configuration you must either specify `DATABASE_JDBC_URL`, or both
`DATABASE_HOST` and `DATABASE_NAME`.  Also note that when a full JDBC URL is specified any values of the
`DATABASE_HOST`, `DATABASE_PORT` and `DATABASE_NAME` are ignored in favour of the JDBC URL specified.  If a JDBC URL is
used then it **MUST** be correct, and compatible with both the actual database being used and the application code.

There are also a `PostgresConfiguration` and `H2Configuration` classes that provide static methods that take in a
`DatabaseConfiguration` object and produce an appropriately populated `Properties` object with the necessary JPA
configuration set for those database backends.

You can find examples of these APIs being used in the various test classes under
[`hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/`][HibExamples].

## Flyway Schema Migration

From `0.6.0` onwards the `AbstractHibernateStorage` base class includes optional support for [Flyway][Flyway] schema
migration.  To opt-in to this support implementations derived from the abstract base class should override the
`configureFlyway()` method appropriately.  For example here's a minimal implementation that takes the JDBC information
from the provided `dbProperties` object:

```java
@Override
protected Flyway configureFlyway(Properties dbProperties) {
    return Flyway.configure()
                 .dataSource(dbProperties.getProperty(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL),
                             dbProperties.getProperty(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_USER),
                             dbProperties.getProperty(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_PASSWORD))
                 .baselineVersion("0")
                 .baselineOnMigrate(true)
                 .load();
}
```

If this method returns a `Flyway` instance then the `AbstractHibernateStorage` constructor will automatically call the
`migrate()` method prior to initialising the JPA/Hibernate.  This ensures that your database schema is appropriately
created and/or updated as needed.  When opting in the implementation will need to provide appropriate Flyway migration
scripts per Flyway's documentation, at a minimum you will likely need a
`src/main/resources/db/migration/V1__InitialSchema.sql` (or similarly named script) with the necessary SQL statements to
create the current schema. As your schema evolves over time additional scripts can be added to implement necessary
incremental schema migrations.

Also if you were previously setting the `jakarta.persistence.schema-generation.database.action` property to a value
that caused JPA/Hibernate to do its own schema generation then that should be removed in favour of use of Flyway.

**NB** If an application may use more than one Hibernate storage instance, where each instance has independent schemas,
then those implementations should ensure that they configure Flyway to use different sets of migration scripts e.g. by
setting `sqlMigrationPrefix()` differently on each Flyway configuration.

## JSON Fields

The `hibernate` module includes a dependency on [Hypersistence Utils][HypUtils] which adds support for JSON
serialization and deserialization to Hibernate.  This allows you to have complex fields on your `@Entity` classes that
are stored as JSON fields within the underlying database.  To use this support you simply need to add the relevant
annotations to your `@Entity` classes fields e.g.

```java
@Type(JsonType.class)
@Column(nullable = false, name = "data", columnDefinition = "jsonb")
private Map<String, Object> json;
```

In this example we have a `Map` typed field that will be stored in the database as a `jsonb` typed column within the
underlying database.

**NB:** Not all databases support the `jsonb` type, depending on the target database backend you may need to use
`columnDefinition = "json"` instead as that column type is more widely supported.

Instead of a `Map` you could type your field as an arbitrary Java POJO e.g.

```java
@Type(JsonType.class)
@Column(nullable = false, name = "data", columnDefinition = "jsonb")
private MyComplexType complex;
```

Provided that `MyComplexType` can be serialized and deserialized using Jackson then this will work seamlessly.  When you
persist your entities then those fields are automatically serialized as JSON, and when you load your entities those
fields are automatically deserialized from the underlying JSON.

**NB:** If you are using your own POJO then it **MUST** implement `equals()` and `hashCode()` appropriately otherwise
some unnecessary `UPDATE` statements may be issued to the underlying database in some scenarios.

Please refer to [JsonStore][JsonStore] and [JsonHolder][JsonHolder] for exemplars of JSON typed fields.

[OrderManager]: ../hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/model/OrderManager.java
[HibExamples]: ../hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/
[HibLabelsStore]: ../label-stores/label-store-hibernate/src/main/java/io/telicent/smart/cache/storage/labels/hibernate/HibernateLabelsStore.java
[Configurator]: https://github.com/telicent-oss/smart-caches-core/blob/main/docs/configurator/index.md
[Flyway]: https://documentation.red-gate.com/fd/flyway-documentation-138346877.html
[HypUtils]: https://github.com/vladmihalcea/hypersistence-utils/tree/master
[JsonStore]: ../hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/model/JsonStore.java
[JsonHolder]: ../hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/model/JsonHolder.java
