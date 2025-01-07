/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.postgres.PostgresConfiguration;
import io.telicent.smart.cache.storage.hibernate.model.OrderManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.Properties;

/**
 * Tests against containerised Postgres
 */
public class DockerTestOrderManagerPostgres extends AbstractOrderManagerTests {

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
    protected OrderManager createOrderManager() {
        Properties props = PostgresConfiguration.prepareConnectionProperties(DatabaseConfiguration.builder()
                                                                                                  .hostname(
                                                                                                          this.postgres.getHost())
                                                                                                  .port(this.postgres.getMappedPort(
                                                                                                          PostgresConfiguration.DEFAULT_POSTGRES_PORT))
                                                                                                  .database(
                                                                                                          this.postgres.getDatabaseName())
                                                                                                  .username(
                                                                                                          this.postgres.getUsername())
                                                                                                  .password(
                                                                                                          this.postgres.getPassword())
                                                                                                  .build());
        props.put(JpaConfiguration.JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION, "create");
        return new OrderManager(props);
    }
}
