/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.model;

import io.telicent.smart.cache.storage.hibernate.AbstractH2MemoryStorage;
import io.telicent.smart.cache.storage.hibernate.TransactionContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderManager extends AbstractH2MemoryStorage {
    /**
     * Creates a new order manager
     */
    public OrderManager() {
        super("hibernate-storage-example");
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
            List<Address> addresses =
                    this.loadByNamedQuery(transaction, Address.class, "findByPostalCode",
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
                                                                  postalCode), () -> {
                        Address newAddress = new Address();
                        newAddress.setRecipient(recipient);
                        newAddress.setNameOrNumber(nameOrNumber);
                        newAddress.setStreet(street);
                        newAddress.setCity(city);
                        newAddress.setPostalCode(postalCode);
                        return newAddress;
                    });
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
                                        () -> new Product(null, code, name, description, price,
                                                          available));
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
