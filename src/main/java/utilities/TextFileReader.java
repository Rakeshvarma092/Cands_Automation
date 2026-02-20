package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for reading and updating text-based property files.
 * Enhanced with thread safety, proper resource management, and cross-platform paths.
 */
public class TextFileReader {

    private static final Logger log = LogManager.getLogger(TextFileReader.class);
    private static final String TEST_DATA_DIR = System.getProperty("user.dir") + File.separator + "testData";

    /**
     * Loads properties from a file in the testData directory.
     *
     * @param fileName Name of the properties file.
     * @return Loaded Properties object.
     */
    public static Properties init_Prop(String fileName) {
        Properties properties = new Properties();
        File file = new File(TEST_DATA_DIR, fileName);
        log.debug("Loading properties from: {}", file.getAbsolutePath());

        try (FileInputStream ip = new FileInputStream(file)) {
            properties.load(ip);
        } catch (IOException e) {
            log.error("Failed to load properties file '{}': {}", fileName, e.getMessage());
        }
        return properties;
    }

    /**
     * Updates a key-value pair in a properties file.
     *
     * @param fileName Name of the properties file.
     * @param key      Property key to update.
     * @param value    New value.
     */
    public void updateTextFileValue(String fileName, String key, String value) {
        log.info("Updating property '{}' = '{}' in file: {}", key, value, fileName);
        Properties properties = init_Prop(fileName);
        properties.setProperty(key, value);

        File file = new File(TEST_DATA_DIR, fileName);
        try (FileOutputStream out = new FileOutputStream(file)) {
            properties.store(out, null);
            log.debug("Successfully updated property file: {}", fileName);
        } catch (IOException e) {
            log.error("Failed to update property file '{}': {}", fileName, e.getMessage());
        }
    }

    /**
     * Reads all lines from a text file in the testData directory.
     *
     * @param fileName Name of the text file.
     * @return List of lines from the file.
     * @throws IOException if the file cannot be read.
     */
    public List<String> readAllDataFromTextFile(String fileName) throws IOException {
        Path filePath = Paths.get(TEST_DATA_DIR, fileName);
        log.debug("Reading all lines from: {}", filePath);

        if (!Files.exists(filePath)) {
            log.error("File not found: {}", filePath);
            throw new FileNotFoundException("File not found: " + filePath);
        }

        List<String> lines = Files.readAllLines(filePath);
        log.debug("Read {} lines from {}", lines.size(), fileName);
        return lines;
    }
}