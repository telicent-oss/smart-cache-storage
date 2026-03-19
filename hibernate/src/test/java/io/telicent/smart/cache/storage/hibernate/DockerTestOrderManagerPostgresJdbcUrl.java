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

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.postgres.PostgresConfiguration;
import io.telicent.smart.cache.storage.hibernate.model.OrderManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.Properties;

/**
 * Tests against containerised Postgres configured using the JDBC URL
 */
public class DockerTestOrderManagerPostgresJdbcUrl extends AbstractOrderManagerTests {

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
                                                                                                  .jdbcUrl(this.postgres.getJdbcUrl())
                                                                                                  .username(
                                                                                                          this.postgres.getUsername())
                                                                                                  .password(
                                                                                                          this.postgres.getPassword())
                                                                                                  .build());
        return new OrderManager(props);
    }
}
