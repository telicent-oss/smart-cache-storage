/**
 * Copyright (C) Telicent Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.telicent.smart.cache.storage.hibernate;

import io.telicent.smart.cache.storage.hibernate.model.Address;
import io.telicent.smart.cache.storage.hibernate.model.OrderManager;
import io.telicent.smart.cache.storage.hibernate.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Abstract test suite that exercise the {@link OrderManager} which is an exemplar implementation of deriving from the
 * {@link AbstractHibernateStorage} base class.  Therefore, these tests are designed to call methods that ultimately
 * call all the helper methods provided by that class.
 * <p>
 * See derived classes for running these tests against different database backends by varying the JPA configuration.
 * </p>
 */
public abstract class AbstractOrderManagerTests {

    /**
     * Creates a new instance of the order manager to test against
     *
     * @return Order Manager
     */
    protected abstract OrderManager createOrderManager();

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenStore_whenClosing_thenSubsequentOperationsFail() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            // When
            orderManager.close();
            Assert.assertTrue(orderManager.isClosed());

            // Then
            orderManager.listProducts();
        }
    }

    @Test
    public void givenEmptyStore_whenLoadingAllAddresses_thenEmptyList() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            // When
            List<Address> addresses = orderManager.getAddresses();

            // Then
            Assert.assertTrue(addresses.isEmpty());
        }
    }

    private void generateAddresses(OrderManager orderManager, int streets, int addressesPerStreet) {
        String city = "Testville";
        String postCodeStart = "AB";
        String postCodeEnd = "CD";
        int postCodeNumberStart = 12;
        int postCodeNumberEnd = 3;

        for (int s = 1; s <= streets; s++) {
            String street = nth(s) + " Street";
            String postalCode =
                    String.format("%s%d %d%s", postCodeStart, postCodeNumberStart, postCodeNumberEnd, postCodeEnd);

            for (int a = 1; a <= addressesPerStreet; a++) {
                String recipient = title(a) + " " + letter(a) + " " + surname(a);
                String nameOrNumber = nameOrNumber(a);
                orderManager.saveAddress(recipient, nameOrNumber, street, city, postalCode);
            }

            postCodeNumberStart++;
            postCodeNumberEnd++;
        }
    }

    private String title(int a) {
        return switch (a % 4) {
            case 0 -> "Ms";
            case 1 -> "Dr";
            case 2 -> "Mrs";
            default -> "Mr";
        };
    }

    private String letter(int a) {
        return StringUtils.repeat((char) ('A' - 1 + (a % 26)), 1);
    }

    private String surname(int a) {
        return switch (a % 5) {
            case 0 -> "Smith";
            case 1 -> "James";
            case 2 -> "Black";
            case 3 -> "White";
            default -> "Doe";
        };
    }

    private String nth(int n) {
        return switch (n % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private String nameOrNumber(int a) {
        return switch (a % 7) {
            case 3 -> "Flat " + ((a + 7) / 7);
            default -> Integer.toString(a);
        };
    }

    @Test
    public void givenPopulatedStore_whenLoadingAllAddresses_thenPopulatedList_andLoadingByWrongPostCodeReturnsNothing() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            this.generateAddresses(orderManager, 1, 10);

            // When
            List<Address> addresses = orderManager.getAddresses();

            // Then
            Assert.assertEquals(addresses.size(), 10);
            verifyAddressesArePopulated(addresses);

            // And
            addresses = orderManager.getAddresses("XY77 8ZZ");
            Assert.assertTrue(addresses.isEmpty());
        }
    }

    private static void verifyAddressesArePopulated(List<Address> addresses) {
        addresses.forEach(address -> {
            Assert.assertNotNull(address.getRecipient());
            Assert.assertNotNull(address.getNameOrNumber());
            Assert.assertNotNull(address.getStreet());
            Assert.assertNotNull(address.getCity());
            Assert.assertNotNull(address.getPostalCode());
        });
    }

    @Test
    public void givenStore_whenPopulatingWithSameAddressesMultipleTimes_thenAddressesAreOnlyStoredOnce() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            // When
            for (int i = 1; i <= 10; i++) {
                this.generateAddresses(orderManager, 1, 10);
            }

            // Then
            List<Address> addresses = orderManager.getAddresses();
            Assert.assertEquals(addresses.size(), 10);
        }
    }

    @Test
    public void givenStorePopulatedWithManyAddresses_whenLoadingByPostcode_thenOnlyPostcodeSpecificAddressesAreReturned() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            this.generateAddresses(orderManager, 10, 30);

            // When
            int postCodeStart = 12;
            int postCodeEnd = 3;
            for (int i = 1; i <= 10; i++) {
                String expectedPostCode = "AB" + postCodeStart + " " + postCodeEnd + "CD";
                List<Address> addresses = orderManager.getAddresses(expectedPostCode);

                // Then
                Assert.assertEquals(addresses.size(), 30);
                verifyAddressesArePopulated(addresses);
                addresses.forEach(address -> {
                    Assert.assertEquals(address.getPostalCode(), expectedPostCode);
                });

                postCodeStart++;
                postCodeEnd++;
            }
        }
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = ".*more than one result")
    public void givenStorePopulatedWithManyAddresses_whenBadSaveOperation_thenIllegalStateException() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            this.generateAddresses(orderManager, 10, 30);
            List<Address> addresses = orderManager.getAddresses();
            Assert.assertEquals(addresses.size(), 300);
            Address address = addresses.get(0);

            // When and Then
            orderManager.badSaveAddress(address.getRecipient(), address.getNameOrNumber(), address.getStreet(),
                                        address.getCity(), address.getPostalCode());
        }
    }

    @Test
    public void givenEmptyStore_whenListingProducts_thenEmptyList() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            // When
            List<Product> products = orderManager.listProducts();

            // Then
            Assert.assertTrue(products.isEmpty());
        }
    }

    @Test
    public void givenEmptyStore_whenListingInStockProducts_thenEmptyList() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            // When
            List<Product> products = orderManager.listProducts(true);

            // Then
            Assert.assertTrue(products.isEmpty());
        }
    }

    @Test
    public void givenEmptyStore_whenListingOutOfStockProducts_thenEmptyList() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            // When
            List<Product> products = orderManager.listProducts(false);

            // Then
            Assert.assertTrue(products.isEmpty());
        }
    }

    @Test
    public void givenEmptyStore_whenGettingProductById_thenNull() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            // When
            Product product = orderManager.getProduct("A123");

            // Then
            Assert.assertNull(product);
        }
    }

    @Test
    public void givenPopulatedStore_whenListingProducts_thenProductsReturned_andStockQueriesReturnCorrectly() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            orderManager.saveProduct("A1", "Test Item", null, BigDecimal.valueOf(1.0), 100);
            orderManager.saveProduct("A2", "Another Item", null, BigDecimal.valueOf(0.2), 0);

            // When
            List<Product> products = orderManager.listProducts();

            // Then
            Assert.assertEquals(products.size(), 2);

            // And
            List<Product> inStock = orderManager.listProducts(true);
            List<Product> outOfStock = orderManager.listProducts(false);
            Assert.assertEquals(inStock.size(), 1);
            Assert.assertEquals(outOfStock.size(), 1);
            Assert.assertNotEquals(inStock.get(0), outOfStock.get(0));
        }
    }

    @Test
    public void givenEmptyStore_whenPopulatingProductsWithDuplicateId_thenFails_andOnlyFirstProductIsStored() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            // When
            try {
                orderManager.saveProduct("A1", "Test Item", null, BigDecimal.valueOf(1.0), 100);

                // Then
                orderManager.saveProduct("A1", "Another Item", null, BigDecimal.valueOf(0.2), 0);
                Assert.fail("Should have thrown an exception on duplicate product code");
            } catch (Throwable e) {
                // And
                Product product = orderManager.getProduct("A1");
                Assert.assertNotNull(product);
                Assert.assertEquals(product.getName(), "Test Item");
                Assert.assertEquals(product.getPrice().compareTo(BigDecimal.valueOf(1.0)), 0);
                Assert.assertEquals(product.getAvailable(), 100);
            }
        }
    }

    @Test
    public void givenPopulatedStore_whenUpdatingStock_thenCorrectAvailabilityReturned() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            orderManager.saveProduct("A1", "Test Item", null, BigDecimal.valueOf(1.0), 100);

            // When
            orderManager.updateStock("A1", -50);

            // Then
            Product product = orderManager.getProduct("A1");
            Assert.assertEquals(product.getAvailable(), 50);
        }
    }

    @Test
    public void givenPopulatedStore_whenUpdatingStockInvalidly_thenAvailabilityUnchanged() {
        // Given
        try (OrderManager orderManager = createOrderManager()) {
            orderManager.saveProduct("A1", "Test Item", null, BigDecimal.valueOf(1.0), 100);

            // When
            Assert.assertThrows(IllegalArgumentException.class, () -> orderManager.updateStock("A1", -1_000));

            // Then
            Product product = orderManager.getProduct("A1");
            Assert.assertEquals(product.getAvailable(), 100);
        }
    }
}
