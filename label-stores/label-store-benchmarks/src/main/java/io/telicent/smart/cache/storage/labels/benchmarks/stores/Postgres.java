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
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.postgres.PostgresConfiguration;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.labels.hibernate.HibernateLabelsStore;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Properties;

/**
 * Test the {@link HibernateLabelsStore} with a Postgres backend
 */
public class Postgres implements StoreImplementation {

    private PostgreSQLContainer<?> postgres;

    @Override
    public LabelsStore newStore() {
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
