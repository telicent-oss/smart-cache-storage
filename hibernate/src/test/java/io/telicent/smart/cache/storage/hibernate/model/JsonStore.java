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
package io.telicent.smart.cache.storage.hibernate.model;

import io.telicent.smart.cache.storage.hibernate.AbstractHibernateStorage;
import io.telicent.smart.cache.storage.hibernate.TransactionContext;
import io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class JsonStore extends AbstractHibernateStorage {


    public static final String JSON_PERSISTENCE_UNIT = "hibernate-json-example";

    /**
     * Creates a new hibernate backed store
     *
     * @param dbProperties    Database Connection properties, this should contain at least a
     *                        {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_URL} property to provide a JDBC connection
     *                        to the database as well as any other relevant properties e.g.
     *                        {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_USER},
     *                        {@value HibernateConfiguration#HIBERNATE_DIALECT} etc.
     */
    public JsonStore(Properties dbProperties) {
        super(dbProperties, JSON_PERSISTENCE_UNIT);
    }

    public JsonHolder save(JsonHolder json) {
        if (json.getUuid() == null) {
            json.setUuid(UUID.randomUUID().toString());
        }

        try (TransactionContext transaction = this.begin()) {
            JsonHolder saved = this.getOrCreateByNaturalId(transaction, json.getUuid(), JsonHolder.class, () -> json);
            saved.setJson(json.getJson());
            transaction.getEntityManager().merge(saved);
            transaction.commit();
            return saved;
        }
    }

    public JsonHolder load(String id) {
        try (TransactionContext transaction = this.begin()) {
            return transaction.getSession().bySimpleNaturalId(JsonHolder.class).load(id);
        }
    }

    public List<JsonHolder> all() {
        try (TransactionContext transaction = this.begin()) {
            return this.loadAll(transaction, JsonHolder.class);
        }
    }

    public boolean delete(String id) {
        try (TransactionContext transaction = this.begin()) {
            JsonHolder json = transaction.getSession().bySimpleNaturalId(JsonHolder.class).load(id);
            if (json != null) {
                transaction.getEntityManager().remove(json);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        }
    }
}
