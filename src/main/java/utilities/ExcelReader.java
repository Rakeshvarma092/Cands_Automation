package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utility class for reading and writing Excel files using Apache POI.
 */
public class ExcelReader {

    private static final Logger log = LogManager.getLogger(ExcelReader.class);

    private final File testDataDir = new File(System.getProperty("user.dir") + File.separator + "testData");
    private final File defaultExcelFile = new File(testDataDir, "login.xlsx");

    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private final String loginSheetName = "loginDetails";

    /**
     * Initializes the default workbook and returns the specified sheet.
     */
    public XSSFSheet excelInitialization(String sheetName) throws IOException {
        log.info("Initializing excel sheet: '{}' from default workbook", sheetName);
        if (workbook != null) {
            closeExcel();
        }
        FileInputStream fis = new FileInputStream(defaultExcelFile);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    /**
     * Initializes a specific workbook and returns the specified sheet.
     */
    public XSSFSheet excelInitialization(String workbookName, String sheetName) throws IOException {
        log.info("Initializing excel sheet: '{}' from workbook: '{}'", sheetName, workbookName);
        if (workbook != null) {
            closeExcel();
        }
        File file = new File(testDataDir, workbookName);
        FileInputStream fis = new FileInputStream(file);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    /**
     * Retrieves username and password for a given key from the default login sheet.
     */
    public List<String> getUserDetails(String userDetailsKey) throws IOException {
        log.info("Fetching user details for: '{}'", userDetailsKey);
        List<String> details = new ArrayList<>();

        XSSFSheet currentSheet = excelInitialization(loginSheetName);
        if (currentSheet == null) {
            log.warn("Login sheet '{}' not found", loginSheetName);
            return details;
        }

        for (Row row : currentSheet) {
            Cell keyCell = row.getCell(0);
            if (keyCell != null && keyCell.toString().equals(userDetailsKey)) {
                details.add(row.getCell(1) != null ? row.getCell(1).toString() : "");
                details.add(row.getCell(2) != null ? row.getCell(2).toString() : "");
                break;
            }
        }

        closeExcel();
        return details;
    }

    /**
     * Closes the current workbook session.
     */
    public void closeExcel() throws IOException {
        if (workbook != null) {
            log.debug("Closing excel workbook");
            workbook.close();
            workbook = null;
            sheet = null;
        }
    }

    public static int findColumnIndex(Sheet sheet, String columnName, int headerRowIndex) {
        Row headerRow = sheet.getRow(headerRowIndex);
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(columnName)) {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }

    /**
     * Writes data to a specific cell.
     */
    public void writeDataToExcel(String workbookName, String sheetName, int rowIndex, int cellIndex, String data) throws IOException {
        log.info("Writing '{}' to {}:{} [Row: {}, Col: {}]", data, workbookName, sheetName, rowIndex, cellIndex);
        XSSFSheet currentSheet = excelInitialization(workbookName, sheetName);

        Row row = currentSheet.getRow(rowIndex);
        if (row == null) {
            row = currentSheet.createRow(rowIndex);
        }

        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(data);

        File file = new File(testDataDir, workbookName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
            log.info("Data written successfully");
        } finally {
            closeExcel();
        }
    }

    /**
     * Writes data to a column identified by name.
     */
    public void writeDataToExcelFileWithColName(String workbookName, String sheetName, String colName, int colIndex, int rowIndex, String data) {
        log.info("Writing '{}' to col '{}' in {}:{}", data, colName, workbookName, sheetName);
        try {
            XSSFSheet currentSheet = excelInitialization(workbookName, sheetName);
            int columnIndex = findColumnIndex(currentSheet, colName, colIndex);

            // Using logic similar to original but with improvements
            int targetRowIndex = getEmptyCellInRow(currentSheet, rowIndex, columnIndex);

            Row row = currentSheet.getRow(targetRowIndex);
            if (row == null) {
                row = currentSheet.createRow(targetRowIndex);
            }

            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(data);

            File file = new File(testDataDir, workbookName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                log.info("Data written to column '{}' at row {}", colName, targetRowIndex);
            }
        } catch (Exception e) {
            log.error("Failed to write data to file {}: {}", workbookName, e.getMessage());
        } finally {
            try {
                closeExcel();
            } catch (IOException e) {
                log.warn("Error closing workbook: {}", e.getMessage());
            }
        }
    }

    public static int getEmptyCellInRow(Sheet sheet, int startRowIndex, int colIndex) {
        int lastRow = sheet.getLastRowNum();
        for (int i = startRowIndex; i <= lastRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) return i;

            Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell.getCellType() == CellType.BLANK) {
                return i;
            }
        }
        return lastRow + 1;
    }
}
