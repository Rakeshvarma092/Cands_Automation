package utilities;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.github.javafaker.Faker;

import DriverFactory.WebDriverFactory;

/**
 * Enhanced DriverUtils providing thread-safe, logged, and optimized WebDriver operations.
 */
public class DriverUtils extends WebDriverFactory {

    private static final Logger log = LogManager.getLogger(DriverUtils.class);
    private static final Set<String> GENERATED_COMPANY_NAMES = new HashSet<>();
    private static final Set<String> GENERATED_RANDOM_NAMES = new HashSet<>();
    private static final Set<String> GENERATED_PHONE_NUMBERS = new HashSet<>();

    private static final String USER_DIR_TESTDATA =
            System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testData";

    // ---------------------- WAIT HELPERS ----------------------

    public static void waitUntilClickable(WebElement element) {
        log.info("Waiting for element to be clickable: {}", element);
        getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitUntilVisible(WebElement element) {
        log.info("Waiting for element to be visible: {}", element);
        getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitUntilVisible(By locator) {
        log.info("Waiting for element located by {} to be visible", locator);
        getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitUntilAllElementsVisible(List<WebElement> elements) {
        log.info("Waiting for all elements to be visible. Count: {}", elements.size());
        getWait().until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public static void waitUntilNotVisible(WebElement element) {
        log.info("Waiting for element to be invisible: {}", element);
        getWait().until(ExpectedConditions.invisibilityOf(element));
    }

    // ---------------------- BASIC ACTIONS ----------------------

    public static void sendKeys(WebElement element, CharSequence value, boolean clickElement) {
        waitUntilVisible(element);
        if (clickElement) {
            log.info("Clicking element before sending keys");
            element.click();
        }
        element.clear();
        if (value != null) {
            log.info("Sending keys: '{}' to element", value);
            element.sendKeys(value);
        }
    }

    public void selectValueInSelector(WebElement element, String value) {
        waitUntilVisible(element);
        if (value != null) {
            log.info("Selecting visible text: '{}' in dropdown", value);
            new Select(element).selectByVisibleText(value);
        }
    }

    public static void waitAndClick(WebElement element) {
        try {
            waitUntilClickable(element);
            log.info("Clicking element: {}", element);
            element.click();
        } catch (Exception e) {
            log.warn("Standard click failed, attempting jClick for element: {}. Error: {}", element, e.getMessage());
            jClick(element);
        }
    }

    public static void waitAndClick(By locator) {
        try {
            WebElement element = getDriver().findElement(locator);
            waitUntilClickable(element);
            log.info("Clicking element located by: {}", locator);
            element.click();
        } catch (Exception e) {
            log.warn("Click failed for locator: {}. Error: {}", locator, e.getMessage());
            try {
                WebElement element = getDriver().findElement(locator);
                jClick(element);
            } catch (Exception ex) {
                log.error("jClick also failed for locator: {}", locator);
                throw ex;
            }
        }
    }

    public static void customDropDownSelection(WebElement dropdown, WebElement option) {
        log.info("Selecting option from custom dropdown");
        waitAndClick(dropdown);
        waitAndClick(option);
    }

    public static void customDropDownSelection(WebElement dropdown, By option) {
        log.info("Selecting option (by locator) from custom dropdown");
        waitAndClick(dropdown);
        waitAndClick(option);
    }

    public void scrollIntoView(By locator) {
        log.info("Scrolling element located by {} into view", locator);
        scrollIntoView(getDriver().findElement(locator));
    }

    public void scrollIntoView(WebElement element) {
        log.info("Scrolling element into view");
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public static void jClick(WebElement element) {
        log.info("Performing Javascript click on element");
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
    }

    public static void hardWait(int timeMs) {
        log.debug("Performing hard wait for {}ms", timeMs);
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Hard wait interrupted: {}", e.getMessage());
        }
    }

    // ---------------------- DATE UTILITIES ----------------------

    /**
     * Gets current date/time formatted with given pattern.
     * Supports patterns like "yyyy-MM-dd", "HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss"
     */
    public String getCurrentDate(String pattern) {
        log.info("Getting current date with pattern: {}", pattern);
        try {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("Failed to format current date with pattern {}: {}", pattern, e.getMessage());
            return LocalDate.now().toString(); // Fallback
        }
    }

    /**
     * Modernized date addition/subtraction using java.time.
     */
    public static String getCurrentDatePlusOrMinus(String pattern, int count) {
        log.info("Calculating date with offset: {} days, pattern: {}", count, pattern);
        try {
            LocalDateTime dateTime = LocalDateTime.now().plusDays(count);
            return dateTime.format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("Failed to calculate date with pattern {}: {}", pattern, e.getMessage());
            return LocalDate.now().plusDays(count).toString();
        }
    }

    // ---------------------- ALERT & WINDOWS ----------------------

    public String getAlertText() {
        log.info("Switching to alert and getting text");
        getWait().until(ExpectedConditions.alertIsPresent());
        return getDriver().switchTo().alert().getText();
    }

    public Set<String> getWindowHandles() {
        log.info("Getting all window handles");
        hardWait(2000);
        return getDriver().getWindowHandles();
    }

    // ---------------------- TEXT / ELEMENT HELPERS ----------------------

    public String getText(WebElement element) {
        waitUntilVisible(element);
        String text = element.getText();
        log.debug("Extracted text: '{}' from element: {}", text, element);
        return text;
    }

    public String getText(By locator) {
        waitUntilVisible(locator);
        String text = getDriver().findElement(locator).getText();
        log.debug("Extracted text: '{}' from locator: {}", text, locator);
        return text;
    }

    public List<WebElement> findElements(By locator) {
        log.debug("Finding elements by locator: {}", locator);
        return getDriver().findElements(locator);
    }

    public WebElement findElement(By locator) {
        log.debug("Finding single element by locator: {}", locator);
        return getDriver().findElement(locator);
    }

    public static void switchToFrame(WebElement frameElement) {
        log.info("Switching to frame: {}", frameElement);
        getWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
    }

    public void switchToDefaultFrame() {
        log.info("Switching back to default content");
        getDriver().switchTo().defaultContent();
    }

    public String getAttributeValues(WebElement element, String attribute) {
        waitUntilVisible(element);
        String value = element.getAttribute(attribute);
        log.debug("Attribute '{}' value for element {}: {}", attribute, element, value);
        return value;
    }

    // ---------------------- RANDOM UTILITIES ----------------------

    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString().split("-")[0];
        log.debug("Generated random string: {}", uuid);
        return uuid;
    }

    public int generateRandomNumber(int max, int min) {
        int num = new Random().nextInt(max - min + 1) + min;
        log.debug("Generated random number between {} and {}: {}", min, max, num);
        return num;
    }

    // ---------------------- FILE DOWNLOAD UTILITIES ----------------------

    public boolean isFileDownloaded(String fileName, String downloadDir) {
        String filesDirectory = System.getProperty("user.home") + File.separator + downloadDir;
        log.info("Checking if file '{}' is downloaded in: {}", fileName, filesDirectory);

        try {
            return getWait().until(d -> {
                File folder = new File(filesDirectory);
                File[] allFiles = folder.listFiles();
                if (allFiles == null) return false;

                return Arrays.stream(allFiles)
                        .anyMatch(file -> file.getName().contains(fileName));
            });
        } catch (Exception e) {
            log.warn("File download check failed or timed out: {}", e.getMessage());
            return false;
        }
    }

    public boolean deleteDownloadedFile(String fileName, String downloadDir) {
        String filesDirectory = System.getProperty("user.home") + File.separator + downloadDir;
        log.info("Attempting to delete downloaded file '{}' from: {}", fileName, filesDirectory);

        try {
            return getWait().until(d -> {
                File folder = new File(filesDirectory);
                File[] allFiles = folder.listFiles();
                if (allFiles == null) return false;

                for (File file : allFiles) {
                    if (file.getName().contains(fileName)) {
                        boolean deleted = file.delete();
                        if (deleted) log.info("Successfully deleted file: {}", file.getName());
                        return deleted;
                    }
                }
                return false;
            });
        } catch (Exception e) {
            log.error("Failed to delete downloaded file: {}", e.getMessage());
            return false;
        }
    }

    public static void verifyFile(String fileName) {
        File file = new File(USER_DIR_TESTDATA + File.separator + fileName);
        if (file.exists()) {
            log.info("Verified file exists: {}", file.getAbsolutePath());
        } else {
            log.error("File NOT found: {}", file.getAbsolutePath());
        }
    }

    // ---------------------- ENCRYPTION ----------------------

    public String rsaEncryption(String pubKey, String text) throws Exception {
        log.info("Performing RSA encryption");
        try {
            PublicKey publicKey = loadPublicKey(pubKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA Encryption failed: {}", e.getMessage());
            throw e;
        }
    }

    private PublicKey loadPublicKey(String key) throws Exception {
        byte[] publicKeyBytes = Base64.decodeBase64(key);
        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return publicKeyFactory.generatePublic(publicKeySpec);
    }

    // ---------------------- WAITS / ACTIONS ----------------------

    public static void fluentWait(WebElement element) {
        log.info("Performing fluent wait for element visibility: {}", element);
        getFluentWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static void keyBoardActions(String action) {
        log.info("Performing keyboard action: CTRL + {}", action);
        new Actions(getDriver())
                .keyDown(Keys.CONTROL)
                .sendKeys(action)
                .keyUp(Keys.CONTROL)
                .perform();
    }

    public static void tabButton() {
        log.info("Pressing TAB and ENTER");
        new Actions(getDriver()).sendKeys(Keys.TAB, Keys.RETURN).perform();
    }

    public static void escButton() {
        log.info("Pressing ESCAPE and ENTER");
        new Actions(getDriver()).sendKeys(Keys.ESCAPE, Keys.RETURN).perform();
    }

    public static void moveToElement(WebElement ele) {
        log.info("Moving mouse to element: {}", ele);
        new Actions(getDriver()).moveToElement(ele).perform();
    }

    public static void pressEnter() throws AWTException {
        log.info("Robot: Pressing ENTER");
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    // ---------------------- COLLECTION UTILITIES ----------------------

    public static Set<String> getText(List<WebElement> elements) {
        log.debug("Extracting text from list of elements. Count: {}", elements.size());
        Set<String> data = new HashSet<>();
        for (WebElement element : elements) {
            data.add(element.getText());
        }
        return data;
    }

    // ---------------------- RANDOM NAME / PHONE GENERATION ----------------------

    public static String generateName() {
        String name = new Faker().name().firstName();
        while (!GENERATED_RANDOM_NAMES.add(name)) {
            name = new Faker().name().firstName();
        }
        log.debug("Generated unique name: {}", name);
        return name;
    }

    public static String generateValidName() {
        String name = new Faker().company().name();
        while (!GENERATED_COMPANY_NAMES.add(name)) {
            name = new Faker().company().name();
        }
        String formattedName = name.replace("and", "Alias").replace("LLC", "Organization");
        log.debug("Generated unique valid company name: {}", formattedName);
        return formattedName;
    }

    public static String generateValidPhoneNumber() {
        String phoneNumber;
        Random random = new Random();
        while (true) {
            int firstDigit = random.nextInt(4) + 6; // 6-9
            long remainingDigits = (long) (random.nextDouble() * 1_000_000_000L);
            phoneNumber = String.format("%d%09d", firstDigit, remainingDigits);
            if (GENERATED_PHONE_NUMBERS.add(phoneNumber)) break;
        }
        log.debug("Generated unique phone number: {}", phoneNumber);
        return phoneNumber;
    }
}