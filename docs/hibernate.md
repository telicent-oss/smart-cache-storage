# Smart Cache Storage - Hibernate

The `hibernate` module provides the building blocks for working with Hibernate+JPA backed storage, which is generally
some form of relational database. This uses the Hibernate/JPA API so any underlying storage supported by Hibernate/JPA
may be used.

The API consists of two core classes:

- `AbstractHibernateStorage` - An abstract implementation of a Hibernate backed store.
- `TransactionContext` - A transaction context.

Developers using this API should have their storage classes extend `AbstractHibernateStorage`, and then use the various
protected methods to implement their actual storage. See the test [`OrderManager`][OrderManager] or [`HibernateLabelsStore`][HibLabelsStore] implementations for exemplars of this pattern.

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
`DatabaseConfiguration. fromConfigurator()` method.

There are also a `PostgresConfiguration` and `H2Configuration` classes that provide static methods that take in a
`DatabaseConfiguration` object and produce an appropriately populated `Properties` object with the necessary JPA
configuration set for those database backends.

You can find examples of these APIs being used in the various test classes under
[`hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/`][HibExamples].

[OrderManager]: hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/model/OrderManager.java
[HibExamples]: ../hibernate/src/test/java/io/telicent/smart/cache/storage/hibernate/
[HibLabelsStore]: ../label-stores/label-store-hibernate/src/main/java/io/telicent/smart/cache/storage/labels/hibernate/HibernateLabelsStore.java
[Configurator]: https://github.com/telicent-oss/smart-caches-core/blob/main/docs/configurator/index.md
