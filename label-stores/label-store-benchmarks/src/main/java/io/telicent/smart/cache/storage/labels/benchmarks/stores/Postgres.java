package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.postgres.PostgresConfiguration;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.hibernate.HibernateLabelsStore;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Properties;

public class Postgres implements StoreImplementation {

    private PostgreSQLContainer<?> postgres;

    @Override
    public DictionaryLabelsStore newStore() {
        Properties props = PostgresConfiguration.prepareConnectionProperties(DatabaseConfiguration.builder()
                                                                                                  .hostname(
                                                                                                          this.postgres.getHost())
                                                                                                  .port(this.postgres.getMappedPort(
                                                                                                          PostgresConfiguration.DEFAULT_PORT))
                                                                                                  .database(
                                                                                                          this.postgres.getDatabaseName())
                                                                                                  .username(
                                                                                                          this.postgres.getUsername())
                                                                                                  .password(
                                                                                                          this.postgres.getPassword())
                                                                                                  .build());

        return new HibernateLabelsStore(props);
    }

    @Override
    public void setup() {
        this.postgres = new PostgreSQLContainer<>("postgres:15-alpine");
        postgres.start();
    }

    @Override
    public void teardown() {
        if (this.postgres != null) {
            this.postgres.stop();
            this.postgres.close();
            this.postgres = null;
        }
    }
}
