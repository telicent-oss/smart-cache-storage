/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.telicent.smart.cache.configuration.Configurator;
import lombok.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * A representation of basic Mongo configuration which consists of a configured {@link MongoClient} and a database
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class MongoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfiguration.class);

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
    /**
     * Constant error message suffix used when the provided Mongo Connection string cannot be parsed successfully
     */
    static final String INVALID_URL_SUFFIX =
            "Note that the required Mongo connection string parameters vary depending on provided parameters, try providing all the requested parameters in your connection string and/or correcting any reported syntax errors.  See https://www.mongodb.com/docs/manual/reference/connection-string-options/#std-label-connections-connection-options for more details.";
    /**
     * Constant error message prefix used when the provided {@code MONGO_URL} connection string cannot be parsed
     * successfully
     */
    static final String INVALID_URL_PREFIX = "Invalid MONGO_URL:";

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
     * @throws MongoException           Thrown if there is a problem with the provided Mongo Configuration
     * @throws IllegalArgumentException Thrown if there is insufficient configuration provided to attempt to connect to
     *                                  Mongo
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
     * @throws MongoException           Thrown if there is a problem with the provided Mongo Configuration
     * @throws IllegalArgumentException Thrown if there is insufficient configuration provided to attempt to connect to
     *                                  Mongo
     */
    public static MongoConfiguration fromConfigurator(String defaultUrl, String defaultDatabase,
                                                      String defaultAuthDatabase, String defaultUsername,
                                                      String defaultPassword) {
        // Get the Mongo URL, i.e. connection string, if available
        String mongoUrl = Configurator.get(new String[] { MONGO_URL }, defaultUrl);
        if (StringUtils.isNotBlank(mongoUrl)) {
            // Start building the client settings
            ConnectionString connectionString = parseConnectionString(mongoUrl);
            LOGGER.info("Configuring from MONGO_URL: {}", sanitiseMongoUrl(mongoUrl, connectionString));
            MongoClientSettings.Builder clientSettings =
                    MongoClientSettings.builder().applyConnectionString(connectionString);

            // Get the rest of the Mongo settings, some of which are optional
            // Note that the database could be supplied in the MONGO_URL in which case we use that as a default unless
            // MONGO_DATABASE was explicitly specified
            String mongoDatabase = Configurator.get(new String[] { MONGO_DATABASE },
                                                    StringUtils.isNotBlank(connectionString.getDatabase()) ?
                                                    connectionString.getDatabase() : defaultDatabase);
            if (StringUtils.isNotBlank(connectionString.getDatabase())) {
                warnIfOverridingUrl(connectionString.getDatabase(), mongoDatabase, MONGO_DATABASE, false);
            }

            // Apply authentication settings if explicitly configured
            // They could have already been supplied in the MONGO_URL in which case these variables SHOULD NOT be set as
            // these potentially override some configuration that could be provided in the connection string
            String mongoUser = Configurator.get(new String[] { MONGO_USER }, defaultUsername);
            String mongoPassword = Configurator.get(new String[] { MONGO_PASSWORD }, defaultPassword);
            // In particular here we ensure that if the MONGO_URL already had an authSource present we use that as a
            // default in preference to our usual default UNLESS they set MONGO_AUTH_DATABASE to override what's in
            // their MONGO_URL
            String mongoAuthDatabase = Configurator.get(new String[] { MONGO_AUTH_DATABASE, MONGO_AUTHZ_DATABASE },
                                                        connectionString.getCredential() != null ?
                                                        connectionString.getCredential().getSource() :
                                                        defaultAuthDatabase);
            if (StringUtils.isNotBlank(mongoUser) && StringUtils.isNotBlank(mongoPassword)) {
                // Enable authentication if supplied with a username and password
                LOGGER.info(
                        "Configuring Mongo Authentication from MONGO_USER and MONGO_PASSWORD with user '{}' and authentication source '{}'",
                        mongoUser, mongoAuthDatabase);
                if (connectionString.getCredential() != null) {
                    warnIfOverridingUrl(connectionString.getCredential().getUserName(), mongoUser, MONGO_USER, false);
                    warnIfOverridingUrl(connectionString.getCredential().getPassword() != null ?
                                        new String(connectionString.getCredential().getPassword()) : null,
                                        mongoPassword, MONGO_PASSWORD, true);
                    warnIfOverridingUrl(connectionString.getCredential().getSource(), mongoAuthDatabase,
                                        MONGO_AUTH_DATABASE, false);
                }
                clientSettings.credential(
                        MongoCredential.createCredential(mongoUser, mongoAuthDatabase, mongoPassword.toCharArray()));
            } else if (connectionString.getCredential() != null) {
                LOGGER.info(
                        "Configured Mongo Authentication from MONGO_URL with user '{}' and authentication source '{}'",
                        connectionString.getCredential().getUserName(), connectionString.getCredential().getSource());
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

    private static ConnectionString parseConnectionString(String mongoUrl) {
        try {
            return new ConnectionString(mongoUrl);
        } catch (Throwable e) {
            throw new MongoException(String.format("%s %s. %s", INVALID_URL_PREFIX, e.getMessage(), INVALID_URL_SUFFIX),
                                     e);
        }
    }

    private static void warnIfOverridingUrl(String urlValue, String configValue, String configVariable,
                                            boolean redactValues) {
        if (StringUtils.isNotBlank(urlValue) && !Objects.equals(urlValue, configValue)) {
            LOGGER.warn("Configuration variable {} provides value '{}' which overrides Connection URL value '{}'",
                        configVariable,
                        redactValues ? redact(configValue, configValue, "<config-password>") : configValue,
                        redactValues ? redact(urlValue, urlValue, "<url-password>") : urlValue);
        }
    }

    /**
     * Sanitises a Mongo Connection String URL to redact any passwords
     *
     * @param rawUrl           Raw Connection String URL
     * @param connectionString The parsed Connection String URL
     */
    public static String sanitiseMongoUrl(String rawUrl, ConnectionString connectionString) {
        String output = rawUrl;
        if (connectionString.getPassword() != null && ArrayUtils.isNotEmpty(connectionString.getPassword())) {
            output = redact(output, connectionString.getPassword(), "<password>");
        }
        if (StringUtils.isNotBlank(connectionString.getProxyPassword())) {
            output = redact(output, connectionString.getProxyPassword(), "<proxy-password>");
        }
        return output;
    }

    private static String redact(String input, char[] valueToRedact, String redactionPlaceholder) {
        return redact(input, new String(valueToRedact), redactionPlaceholder);
    }

    private static String redact(String input, String valueToRedact, String redactionPlaceholder) {
        return input.replace(valueToRedact, redactionPlaceholder);
    }
}
