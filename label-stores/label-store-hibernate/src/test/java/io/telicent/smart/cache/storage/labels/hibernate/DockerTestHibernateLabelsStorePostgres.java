/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.postgres.PostgresConfiguration;
import io.telicent.smart.cache.storage.labels.AbstractLabelStoreTests;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.Properties;

public class DockerTestHibernateLabelsStorePostgres extends AbstractLabelStoreTests {
    private PostgreSQLContainer<?> postgres;

    @BeforeMethod
    public void setup() {
        this.postgres = new PostgreSQLContainer<>("postgres:15-alpine");
        postgres.start();
    }

    @AfterMethod
    public void teardown() {
        if (this.postgres != null) {
            this.postgres.stop();
            this.postgres.close();
            this.postgres = null;
        }
    }

    @Override
    protected LabelsStore newStore() {
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
}
