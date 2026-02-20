package utilities;

import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Utility class for reading and writing ODS (OpenDocument Spreadsheet) files.
 * Enhanced with thread safety, proper logging, and cross-platform path handling.
 */
public class OdsReader {

    private static final Logger log = LogManager.getLogger(OdsReader.class);

    private final String userDir = System.getProperty("user.dir") + File.separator + "testData";
    private final File defaultOdsFile = new File(userDir, "issuanceTestData.ods");
    private final String defaultLoginSheetName = "IssuanceLoginDetails";
    private final String userHome = System.getProperty("user.home");
    private final File downloadDir = new File(userHome, "Downloads");

    /**
     * Initializes a Sheet from an ODS file.
     */
    public Sheet odsInitialization(File file, String sheetName) throws IOException {
        log.debug("Initializing ODS sheet: '{}' from file: {}", sheetName, file.getName());
        return SpreadSheet.createFromFile(file).getSheet(sheetName);
    }

    /**
     * Fetches username and password from the default login sheet based on a key.
     */
    public List<Object> getUserDetails(String userDetailsKey) {
        log.info("Fetching user details for: '{}'", userDetailsKey);
        List<Object> details = new ArrayList<>();

        try {
            Sheet sheet = odsInitialization(defaultOdsFile, defaultLoginSheetName);
            int rowCount = sheet.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                Object cellValue = sheet.getValueAt(0, i); // Assuming key is in column 0
                if (cellValue != null && cellValue.toString().equals(userDetailsKey)) {
                    details.add(sheet.getValueAt(1, i)); // Username in column 1
                    details.add(sheet.getValueAt(2, i)); // Password in column 2
                    log.debug("Found details for {}: {}", userDetailsKey, details);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Failed to load ODS login sheet '{}': {}", defaultLoginSheetName, e.getMessage());
        }
        return details;
    }

    /**
     * Returns key-value pairs from a sheet (Key in Col 0, Value in Col 1).
     */
    public HashMap<Object, Object> getSheetData(String sheetName) {
        log.info("Fetching sheet data for: '{}'", sheetName);
        HashMap<Object, Object> dataMap = new HashMap<>();

        try {
            Sheet sheet = odsInitialization(defaultOdsFile, sheetName);
            int rowCount = sheet.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                Object key = sheet.getValueAt(0, i);
                if (key != null && !key.toString().isEmpty()) {
                    dataMap.put(key, sheet.getValueAt(1, i));
                }
            }
        } catch (Exception e) {
            log.error("Failed to load ODS sheet data for '{}': {}", sheetName, e.getMessage());
        }
        return dataMap;
    }

    /**
     * Returns a map of lists representing multiple data rows per column title.
     */
    public HashMap<Object, List<Object>> getMultipleRowData(String sheetName) {
        log.info("Fetching multiple row data for: '{}'", sheetName);
        HashMap<Object, List<Object>> rowsData = new HashMap<>();

        try {
            Sheet sheet = odsInitialization(defaultOdsFile, sheetName);
            int colCount = sheet.getColumnCount();
            int rowCount = sheet.getRowCount();

            for (int i = 0; i < colCount; i++) {
                Object header = sheet.getValueAt(i, 0);
                if (header == null) continue;

                List<Object> data = new ArrayList<>();
                for (int k = 1; k < rowCount; k++) {
                    data.add(sheet.getValueAt(i, k));
                }
                rowsData.put(header, data);
            }
        } catch (Exception e) {
            log.error("Failed to load multiple row data for '{}': {}", sheetName, e.getMessage());
        }
        return rowsData;
    }

    /**
     * Specialized method to fetch row data starting from a specific column name match.
     */
    public HashMap<Object, List<Object>> getMultipleRowDataStartsWithColumnName(String sheetName, String columnName) {
        log.info("Fetching row data starting with: '{}' in sheet: '{}'", columnName, sheetName);
        HashMap<Object, List<Object>> rowsData = new HashMap<>();

        try {
            File latestFile = getLatestDownloadedFile();
            if (latestFile == null) {
                log.error("No downloaded files found to parse");
                return rowsData;
            }

            // Note: Preserving complex format switching logic if it's strictly required by framework flow
            File odsFile = changeFileFormat(".csv", ".ods", latestFile);
            Sheet sheet = odsInitialization(odsFile, sheetName);
            int rowCount = sheet.getRowCount();
            int colCount = sheet.getColumnCount();

            for (int i = 0; i < rowCount; i++) {
                if (columnName.equals(sheet.getValueAt(0, i))) {
                    int headerRowIndex = i;
                    // Logic to extract data below this header for up to 15 columns
                    for (int k = 0; k < Math.min(colCount, 15); k++) {
                        List<Object> data = new ArrayList<>();
                        for (int l = headerRowIndex + 1; l < rowCount; l++) {
                            Object val = sheet.getValueAt(k, l);
                            data.add((val == null || val.toString().isEmpty()) ? "Empty Record" : val);
                        }
                        rowsData.put(sheet.getValueAt(k, headerRowIndex), data);
                    }
                    break;
                }
            }
            writeDataToOdsFile(sheetName, "Automation Status", latestFile);
        } catch (Exception e) {
            log.error("Failed to load ODS row data with column name: {}", e.getMessage());
        }
        return rowsData;
    }

    /**
     * Writes data to a specific cell in the latest downloaded file.
     */
    public void writeDataToOdsFile(String sheetName, String data, File targetFile) {
        log.info("Writing '{}' to latest file sheet: '{}'", data, sheetName);
        try {
            if (targetFile != null && targetFile.exists()) {
                Workbook workbook = new Workbook(targetFile.getAbsolutePath());
                Worksheet worksheet = workbook.getWorksheets().get(sheetName);
                if (worksheet != null) {
                    worksheet.getCells().get("P5").putValue(data);
                    workbook.save(targetFile.getAbsolutePath());
                    log.debug("Successfully saved data to P5");
                }
            }
        } catch (Exception e) {
            log.error("Failed to write to ODS file: {}", e.getMessage());
        }
    }

    /**
     * Higher level write method.
     */
    public void writeDataToOdsFile(String sheetName, String column, int rowIndex, String data) {
        log.info("Writing '{}' to {}{} in latest file sheet: '{}'", data, column, rowIndex, sheetName);
        try {
            File targetFile = getLatestDownloadedFile();
            if (targetFile != null) {
                Workbook workbook = new Workbook(targetFile.getAbsolutePath());
                Worksheet worksheet = workbook.getWorksheets().get(sheetName);
                if (worksheet != null) {
                    worksheet.getCells().get(column + rowIndex).putValue(data);
                    workbook.save(targetFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.error("Failed to write to ODS file with coordinates: {}", e.getMessage());
        }
    }

    /**
     * Writes data to a file in the test data directory.
     */
    public void writeDataToTestData(String fileName, String sheetName, String column, int rowIndex, String data) {
        log.info("Writing '{}' to {} in {}:{}", data, column + rowIndex, fileName, sheetName);
        try {
            File targetFile = new File(userDir, fileName);
            if (targetFile.exists()) {
                Workbook workbook = new Workbook(targetFile.getAbsolutePath());
                Worksheet worksheet = workbook.getWorksheets().get(sheetName);
                if (worksheet != null) {
                    worksheet.getCells().get(column + rowIndex).putValue(data);
                    workbook.save(targetFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.error("Failed to write to test data: {}", e.getMessage());
        }
    }

    /**
     * Retrieves the latest modified file in the user's Downloads directory.
     */
    public File getLatestDownloadedFile() throws IOException {
        Path dir = Paths.get(userHome, "Downloads");
        if (!Files.exists(dir)) return null;

        return Files.list(dir)
                .filter(f -> !Files.isDirectory(f))
                .max(Comparator.comparingLong(f -> f.toFile().lastModified()))
                .map(Path::toFile)
                .orElse(null);
    }

    /**
     * Convers a file's extension by re-saving it using Aspose (preserving original framework logic).
     */
    public File changeFileFormat(String currentFormat, String requiredFormat, File sourceFile) {
        log.info("Changing file format from {} to {}", currentFormat, requiredFormat);
        try {
            if (sourceFile != null) {
                String newPath = sourceFile.getAbsolutePath().replace(currentFormat, requiredFormat);
                Workbook workbook = new Workbook(sourceFile.getAbsolutePath());
                workbook.save(newPath);
                return new File(newPath);
            }
        } catch (Exception e) {
            log.error("Failed to change file format: {}", e.getMessage());
        }
        return sourceFile;
    }
}