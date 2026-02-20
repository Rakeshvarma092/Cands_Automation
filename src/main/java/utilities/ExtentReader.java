package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for reading and updating extent properties.
 * Enhanced with thread-safe lazy initialization and proper resource management.
 */
public class ExtentReader {

    private static final Logger log = LogManager.getLogger(ExtentReader.class);
    private static volatile Properties properties;
    private static final String PROPERTIES_PATH = "./src/test/resources/extent.properties";

    /**
     * Private constructor to prevent instantiation as the class provides static utility methods.
     * Properties are loaded lazily when needed.
     */
    public ExtentReader() {
        // Initialization is now managed by getProperties()
    }

    /**
     * Thread-safe lazy initialization of properties using double-checked locking.
     * @return The loaded Properties object.
     */
    private static Properties getProperties() {
        if (properties == null) {
            synchronized (ExtentReader.class) {
                if (properties == null) {
                    properties = new Properties();
                    File file = new File(PROPERTIES_PATH);
                    if (file.exists()) {
                        log.debug("Loading extent properties from: {}", file.getAbsolutePath());
                        try (FileInputStream ip = new FileInputStream(file)) {
                            properties.load(ip);
                        } catch (IOException e) {
                            log.error("Failed to load extent properties: {}", e.getMessage());
                        }
                    } else {
                        log.warn("Extent properties file not found at: {}", PROPERTIES_PATH);
                    }
                }
            }
        }
        return properties;
    }

    /**
     * Updates a property value in the extent.properties file.
     *
     * @param key   The property key to update.
     * @param value The new value for the key.
     */
    public static void updateExtentPropertiesValue(String key, String value) {
        log.info("Updating extent property: {} = {}", key, value);
        Properties props = getProperties();
        props.setProperty(key, value);

        File file = new File(PROPERTIES_PATH);
        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, "Updated via ExtentReader");
            log.debug("Successfully updated extent properties file");
        } catch (IOException e) {
            log.error("Failed to update extent properties file {}: {}", PROPERTIES_PATH, e.getMessage());
        }
    }
}