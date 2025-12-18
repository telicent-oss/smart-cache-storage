/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.model;

import io.telicent.smart.cache.storage.hibernate.AbstractHibernateStorage;
import io.telicent.smart.cache.storage.hibernate.TransactionContext;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class OrderManager extends AbstractHibernateStorage {
    /**
     * Creates a new order manager
     *
     * @param dbProperties Database connection properties
     */
    public OrderManager(Properties dbProperties) {
        super(dbProperties, "hibernate-storage-example");
    }

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

    public List<Address> getAddresses() {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            return this.loadAll(transaction, Address.class);
        }
    }

    public List<Address> getAddresses(String postCode) {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            List<Address> addresses = this.loadByNamedQuery(transaction, Address.class, "findByPostalCode",
                                                            Map.of("postalCode", postCode));
            transaction.commit();
            return addresses;
        }
    }

    public Address saveAddress(String recipient, String nameOrNumber, String street, String city, String postalCode) {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            Address address = this.getOrCreateByNamedQuery(transaction, Address.class, "findByDetails",
                                                           Map.of("recipient", recipient, "nameOrNumber", nameOrNumber,
                                                                  "street", street, "city", city, "postalCode",
                                                                  postalCode),
                                                           () -> new Address(null, recipient, nameOrNumber, street,
                                                                             city, postalCode));
            transaction.commit();
            return address;
        }
    }

    public Address badSaveAddress(String recipient, String nameOrNumber, String street, String city,
                                  String postalCode) {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            // This will always fail if the street has more than one address, this is included merely to allow for test
            // validation and coverage of this corner case of behaviour.
            Address address =
                    this.getOrCreateByNamedQuery(transaction, Address.class, "findByStreet", Map.of("street", street),
                                                 () -> new Address(null, recipient, nameOrNumber, street, city,
                                                                   postalCode));
            transaction.commit();
            return address;
        }
    }

    public List<Product> listProducts() {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            return this.loadAll(transaction, Product.class);
        }
    }

    public List<Product> listProducts(boolean inStock) {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            return this.loadByNamedQuery(transaction, Product.class, inStock ? "inStock" : "outOfStock");
        }
    }

    public Product getProduct(String code) {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            Product product = this.loadByNaturalId(transaction, code, Product.class);
            transaction.commit();
            return product;
        }
    }

    public void saveProduct(String code, String name, String description, BigDecimal price, long available) {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            this.getOrCreateByNaturalId(transaction, code, Product.class,
                                        () -> new Product(null, code, name, description, price, available));
            transaction.commit();
        }
    }

    public void updateStock(String code, long diff) {
        ensureNotClosed();
        try (TransactionContext transaction = this.begin()) {
            Product product = this.loadByNaturalId(transaction, code, Product.class);
            if (product == null) {
                throw new IllegalStateException("Product not found");
            }
            product.setAvailable(product.getAvailable() + diff);
            if (product.getAvailable() < 0) {
                throw new IllegalArgumentException(
                        "Product availability would become negative with a difference of " + diff + " applied");
            }
            transaction.getEntityManager().merge(product);
            transaction.commit();
        }
    }
}
