package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Singleton-based ConfigReader to handle property file interactions.
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static volatile Properties properties;
    private static final String CONFIG_PATH = "./src/test/resources/config.properties";

    /**
     * Initializes and loads properties from the configuration file.
     * Uses synchronized block to ensure thread safety during initialization.
     * @return Loaded Properties object
     */
    public Properties init_Prop() {
        if (properties == null) {
            synchronized (ConfigReader.class) {
                if (properties == null) {
                    properties = new Properties();
                    log.info("Loading configuration from: {}", CONFIG_PATH);
                    try (FileInputStream ip = new FileInputStream(CONFIG_PATH)) {
                        properties.load(ip);
                    } catch (IOException e) {
                        log.fatal("Could not load configuration file at {}. Error: {}", CONFIG_PATH, e.getMessage());
                        throw new RuntimeException("Configuration initialization failed", e);
                    }
                }
            }
        }
        return properties;
    }

    /**
     * Internal helper to retrieve property values with logging.
     */
    private String getProperty(String key) {
        if (properties == null) {
            init_Prop();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property key '{}' not found in configuration", key);
        }
        return value;
    }

    public String getBrowserConfiguration() {
        return getProperty("browser");
    }

    public String getAccountSID() {
        return getProperty("AccountSID");
    }

    public String getAuthToken() {
        return getProperty("AuthToken");
    }

    public String getNumber() {
        return getProperty("PhoneNumber");
    }

    public String getUrlDetails() {
        String env = getProperty("environment");
        return env != null ? getProperty(env + "_URL") : null;
    }

    public String getUserDetails() {
        String env = getProperty("environment");
        return env != null ? getProperty(env + "_userDetails") : null;
    }

    public String getConnectionString() {
        return getProperty("connectionString");
    }

    public String getDatabaseName() {
        return getProperty("databaseName");
    }

    public String getCollectionName() {
        return getProperty("collectionName");
    }

    public String getUserId() {
        return getProperty("userId");
    }

    public String getCollectionField() {
        return getProperty("collectionField");
    }

    public String getEmailUserId() {
        return getProperty("userId");
    }

    public String getEMailUserPassword() {
        return getProperty("emailPassword");
    }

    /**
     * Retrieves the maximum retry count for failed tests.
     * Defaults to 1 if the property is missing or invalid.
     */
    public int getMaxRetryCount() {
        String countStr = getProperty("maxRetryCount");
        if (countStr != null) {
            try {
                return Integer.parseInt(countStr);
            } catch (NumberFormatException e) {
                log.warn("Invalid maxRetryCount '{}' in configuration. Defaulting to 1.", countStr);
            }
        }
        return 1;
    }
}