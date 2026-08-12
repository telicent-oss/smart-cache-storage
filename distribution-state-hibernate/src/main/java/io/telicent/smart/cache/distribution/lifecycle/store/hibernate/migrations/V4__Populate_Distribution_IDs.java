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
package io.telicent.smart.cache.distribution.lifecycle.store.hibernate.migrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.telicent.smart.cache.distribution.lifecycle.events.LifecycleAction;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class V4__Populate_Distribution_IDs extends BaseJavaMigration {

    private final ObjectMapper json = new ObjectMapper();

    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            try (PreparedStatement update = context.getConnection()
                                                   .prepareStatement(
                                                           "UPDATE LIFECYCLE_ACTIONS SET distributionId=? WHERE id=?")) {
                try (ResultSet results = statement.executeQuery("SELECT id, action FROM LIFECYCLE_ACTIONS")) {
                    while (results.next()) {
                        LifecycleAction action =
                                this.json.readValue(results.getBytes("action"), LifecycleAction.class);
                        update.clearParameters();
                        update.setString(1, action.getDistributionId());
                        update.setLong(2, results.getLong("id"));
                        update.execute();
                    }
                }
            }
        }
    }
}
