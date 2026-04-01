package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.RocksDBException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestTransactionAbort extends AbstractRocksDBTests {

    public static final byte[] KEY = "key".getBytes(StandardCharsets.UTF_8);
    public static final byte[] VALUE = "value".getBytes(
            StandardCharsets.UTF_8);

    @Test
    public void givenTransaction_whenNotCommitted_thenChangesNotVisibleOutsideTransaction() throws RocksDBException,
            IOException {
        try (External external = new External(this.dbDir)) {
            // When
            try (TransactionContext context = external.begin()) {
                context.put(external.getDefaultHandle(), KEY, VALUE);
                Assert.assertEquals(context.get(external.getDefaultHandle(), KEY), VALUE);
            }

            // Then
            try (TransactionContext context = external.begin()) {
                Assert.assertNull(context.get(external.getDefaultHandle(), KEY));
            }
        }
    }

    @Test
    public void givenNestedTransaction_whenOutermostNotCommitted_thenChangesNotVisibleOutsideTransaction() throws RocksDBException,
            IOException {
        try (External external = new External(this.dbDir)) {
            // When
            try (TransactionContext context = external.beginNested()) {
                try (TransactionContext nested = external.begin()) {
                    nested.put(external.getDefaultHandle(), KEY, VALUE);
                    Assert.assertEquals(nested.get(external.getDefaultHandle(), KEY), VALUE);
                    // NB - Not outermost so should not commit changes
                    nested.commit();
                }
            }

            // Then
            try (TransactionContext context = external.begin()) {
                Assert.assertNull(context.get(external.getDefaultHandle(), KEY));
            }
        }
    }
}
