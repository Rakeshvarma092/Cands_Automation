package utilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;

/**
 * Utility class for fetching SMS using Appium on Android devices.
 * Enhanced with proper resource management and logging.
 */
public class OTPSteps {

    private static final Logger log = LogManager.getLogger(OTPSteps.class);
    private AppiumDriver androidDriver;

    /**
     * Connects to the device, fetches the last 3 SMS, and cleans up.
     * @return String representation of the SMS list.
     * @throws MalformedURLException if the Appium server URL is invalid.
     */
    public String fetchSMS() throws MalformedURLException {
        log.info("Attempting to fetch SMS from Android device");
        try {
            setupDriver();

            HashMap<String, Integer> scriptArgs = new HashMap<>();
            scriptArgs.put("max", 3);

            log.debug("Executing mobile: listSms script");
            Object result = androidDriver.executeScript("mobile: listSms", scriptArgs);

            String smsData = result != null ? result.toString() : "No SMS found";
            log.info("Successfully retrieved SMS data");
            return smsData;

        } catch (MalformedURLException e) {
            log.error("Failed to connect to Appium server: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while fetching SMS: {}", e.getMessage());
            return "Error: " + e.getMessage();
        } finally {
            tearDown();
        }
    }

    /**
     * Initializes the Appium driver with required capabilities.
     */
    public void setupDriver() throws MalformedURLException {
        log.debug("Setting up Android Appium driver capabilities");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("autoGrantPermissions", true);

        // Hardcoded device details preserved as per original code, but logged for visibility
        String deviceId = "8653505c";
        capabilities.setCapability("appium:deviceName", deviceId);
        capabilities.setCapability("appium:udid", deviceId);

        String serverUrl = "http://127.0.0.1:4723/";
        log.info("Connecting to Appium server at {} for device {}", serverUrl, deviceId);

        androidDriver = new AppiumDriver(new URL(serverUrl), capabilities);
        androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }

    /**
     * Safely quits the Appium driver session.
     */
    public void tearDown() {
        if (androidDriver != null) {
            log.debug("Tearing down Appium driver session");
            androidDriver.quit();
            androidDriver = null;
        }
    }
}