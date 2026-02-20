package DriverFactory;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverFactory {

    private static final Logger log = LogManager.getLogger(WebDriverFactory.class);

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> tlWait = new ThreadLocal<>();
    private static final ThreadLocal<Wait<WebDriver>> tlFluentWait = new ThreadLocal<>();
    public static WebDriver driver;
    private static final long WAIT_TIMEOUT_SECONDS = 15;
    private static final long POLLING_TIMEOUT_MILLIS = 500;

    public static final String DOWNLOAD_PATH = System.getProperty("user.home") + File.separator + "Downloads";

    /**
     * Initializes the WebDriver based on the browser name.
     * @param browser The name of the browser (chrome, firefox, safari, edge)
     * @return WebDriver instance
     */
    public WebDriver init_driver(String browser) {
        browser = browser.toLowerCase().trim();
        log.info("Initializing WebDriver for browser: [{}]", browser);
        if (driver == null) {
            try {
                switch (browser) {

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver(chromeOptions());
                        break;
                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driver = new FirefoxDriver();
                        break;
                    case "safari":
                        driver = new SafariDriver();
                        break;
                    case "edge":
                        WebDriverManager.edgedriver().setup();
                        driver = new EdgeDriver();
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported browser: " + browser);
                }
            } catch (Exception e) {
                throw new RuntimeException("Driver initialization failed for: " + browser, e);
            }
            tlDriver.set(driver);
            setupDriverDefaults(driver);
            setupWaitInstances(driver);
        }
        return driver;
    }


    private void setupDriverDefaults(WebDriver driver) {
        log.debug("Setting up driver defaults: maximized window, cookies deleted.");
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
    }

    private void setupWaitInstances(WebDriver driver) {
        log.debug("Configuring wait instances (Timeout: {}s, Polling: {}ms)", WAIT_TIMEOUT_SECONDS, POLLING_TIMEOUT_MILLIS);
        tlWait.set(new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS)));
        tlFluentWait.set(new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(WAIT_TIMEOUT_SECONDS))
                .pollingEvery(Duration.ofMillis(POLLING_TIMEOUT_MILLIS))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class));
    }

    public static synchronized WebDriver getDriver() {
        return tlDriver.get();
    }

    public static synchronized WebDriverWait getWait() {
        return tlWait.get();
    }

    public static synchronized Wait<WebDriver> getFluentWait() {
        return tlFluentWait.get();
    }

    public ChromeOptions chromeOptions() {
        log.debug("Configuring ChromeOptions...");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.addArguments("--start-maximized", "--incognito", "--remote-allow-origins=*");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("download.default_directory", DOWNLOAD_PATH);

        chromeOptions.setExperimentalOption("prefs", prefs);
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

        return chromeOptions;
    }

    public void pageRefresh() {
        log.info("Refreshing current page...");
        getDriver().navigate().refresh();
    }

    public void navigateURL(String url) {
        log.info("Navigating to URL: [{}]", url);
        getDriver().navigate().to(url);
    }
}