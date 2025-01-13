/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.telicent.smart.cache.configuration.Configurator;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * A representation of basic Mongo configuration which consists of a configured {@link MongoClient} and a database
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class MongoConfiguration {

    /**
     * A configuration variable used to specify the URL of the Mongo server to connect to
     */
    public static final String MONGO_URL = "MONGO_URL";
    /**
     * A configuration variable used to specify the database that is connected to on the Mongo server
     */
    public static final String MONGO_DATABASE = "MONGO_DATABASE";
    /**
     * A configuration variable used to specify the authentication/authorization database that is used on the Mongo
     * server
     */
    public static final String MONGO_AUTH_DATABASE = "MONGO_AUTH_DATABASE";
    /**
     * Alternative configuration variable used to specify the authentication/authorization that is used on the Mongo
     * server
     */
    public static final String MONGO_AUTHZ_DATABASE = "MONGO_AUTHZ_DATABASE";
    /**
     * Default Mongo authentication/authorization database
     */
    public static final String DEFAULT_AUTH_DATABASE = "admin";
    /**
     * A configuration variable used to specify the username for authenticating to the Mongo server
     */
    public static final String MONGO_USER = "MONGO_USER";
    /**
     * A configuration variable used to specify the password for authenticating to the Mongo server
     */
    public static final String MONGO_PASSWORD = "MONGO_PASSWORD";

    @NonNull
    private final MongoClient client;
    @NonNull
    private final String database;

    /**
     * Gets the Mongo configuration based upon using the {@link Configurator} API to retrieve the configuration based
     * upon the keys defined as constants on this class i.e.
     * <ul>
     *     <li>{@value MONGO_URL} for the connection URL.</li>
     *     <li>{@value MONGO_DATABASE} for the database to connect to on the Mongo server.</li>
     *     <li>{@value MONGO_AUTH_DATABASE} for the authentication database used to authenticate to the Mongo
     *     Server.</li>
     *     <li>{@value MONGO_USER} for the username to authenticate to the Mongo Server with.</li>
     *     <li>{@value MONGO_PASSWORD} for the password to authenticate to the Mongo Server with.</li>
     * </ul>
     *
     * @return Mongo configuration
     */
    public static MongoConfiguration fromConfigurator() {
        return fromConfigurator(null, null, DEFAULT_AUTH_DATABASE, null, null);
    }

    /**
     * Gets the Mongo configuration based upon using the {@link Configurator} API to retrieve the configuration based
     * upon the keys defined as constants on this class i.e.
     * <ul>
     *     <li>{@value MONGO_URL} for the connection URL.</li>
     *     <li>{@value MONGO_DATABASE} for the database to connect to on the Mongo server.</li>
     *     <li>{@value MONGO_AUTH_DATABASE} for the authentication database used to authenticate to the Mongo
     *     Server.</li>
     *     <li>{@value MONGO_USER} for the username to authenticate to the Mongo Server with.</li>
     *     <li>{@value MONGO_PASSWORD} for the password to authenticate to the Mongo Server with.</li>
     * </ul>
     * <p>
     * The default values supplied are used if no configuration is available from the configuration API.
     * </p>
     *
     * @param defaultUrl          Default URL
     * @param defaultDatabase     Default Database
     * @param defaultAuthDatabase Default auth database
     * @param defaultPassword     Default password
     * @return Mongo configuration
     */
    public static MongoConfiguration fromConfigurator(String defaultUrl, String defaultDatabase,
                                                      String defaultAuthDatabase,
                                                      String defaultUsername, String defaultPassword) {
        // Get the Mongo URL, i.e. connection string, if available
        String mongoUrl = Configurator.get(new String[] { MONGO_URL }, defaultUrl);
        if (StringUtils.isNotBlank(mongoUrl)) {
            // Get the rest of the Mongo settings, some of which are optional
            String mongoDatabase = Configurator.get(new String[] { MONGO_DATABASE }, defaultDatabase);
            String mongoUser = Configurator.get(new String[] { MONGO_USER }, defaultUsername);
            String mongoPassword = Configurator.get(new String[] { MONGO_PASSWORD }, defaultPassword);
            String mongoAuthDatabase =
                    Configurator.get(new String[] { MONGO_AUTH_DATABASE, MONGO_AUTHZ_DATABASE }, defaultAuthDatabase);

            // Start building the client settings
            MongoClientSettings.Builder clientSettings =
                    MongoClientSettings.builder().applyConnectionString(new ConnectionString(mongoUrl));
            if (StringUtils.isNotBlank(mongoUser) && StringUtils.isNotBlank(mongoPassword)) {
                // Enable authentication if supplied with a username and password
                clientSettings.credential(
                        MongoCredential.createCredential(mongoUser, mongoAuthDatabase, mongoPassword.toCharArray()));
            }

            // Create the prepare configuration
            return MongoConfiguration.builder()
                                     .client(MongoClients.create(clientSettings.build()))
                                     .database(mongoDatabase)
                                     .build();
        } else {
            throw new IllegalArgumentException("Mongo URL not configured");
        }
    }
}
