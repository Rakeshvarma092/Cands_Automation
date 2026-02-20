package stepDefinitions;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import DriverFactory.WebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import utilities.ConfigReader;
import utilities.ExtentReader;
import utilities.Log;


/**
 * Cucumber hooks for test lifecycle management.
 * Handles browser initialization, reporting, screenshots, and cleanup.
 */
public class ApplicationHooks extends WebDriverFactory {

    private static final Logger log = LogManager.getLogger(ApplicationHooks.class);

    public static ExtentTest test;
    public static ExtentReports extent = new ExtentReports();
    public static ExtentSparkReporter spark;
    public static Scenario scenario;
    public static String scenarioName;

    private WebDriverFactory driverFactory;
    private ConfigReader configReader;

    private static final String SCREENSHOTS_DIR = "." + File.separator + "Screenshots";
    private static final String EXTENT_REPORTS_DIR = "." + File.separator + "ExtentReports-Verbose";

    @Before(order = 0)
    public void initialization(Scenario scenario) {
        Log.startTestCase(scenario.getName());
        ApplicationHooks.scenario = scenario;

        String dateStamp = formatDate("MM-dd-yyyy");
        spark = new ExtentSparkReporter(
                EXTENT_REPORTS_DIR + File.separator + "AutomationReport-" + dateStamp + File.separator + "Automation-Report.html");

        configReader = new ConfigReader();
        configReader.init_Prop();
        driverFactory = new WebDriverFactory();

        extent.attachReporter(spark);
        test = extent.createTest(scenario.getName());

        try {
            ExtentReader.updateExtentPropertiesValue("basefolder.name", "Reports/reports-" + dateStamp + "/reports");
        } catch (RuntimeException e) {
            log.warn("Could not update extent properties: {}", e.getMessage());
        }

        deleteFiles("Reports");
        deleteFiles("Screenshots");
        scenarioName = scenario.getSourceTagNames().toString().trim();
        log.info("Initialization complete for scenario: {}", scenario.getName());
    }

    @Before("@Browser")
    public void launchBrowser() throws IOException {
        log.info("Launching browser for scenario: {}", scenario.getName());
        driverFactory.init_driver(configReader.getBrowserConfiguration());
    }

    @After(order = 0)
    public void setExtent() {
        extent.flush();
        Log.endTestCase(scenario.getName());
    }

    @After(order = 2)
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && getDriver() != null) {
            try {
                String screenshotName = scenario.getName().replaceAll(" ", "_");
                byte[] sourcePath = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(sourcePath, "image/png", screenshotName);
                log.debug("Attached inline screenshot for failed scenario: {}", screenshotName);
            } catch (WebDriverException e) {
                log.error("Failed to capture inline screenshot: {}", e.getMessage());
            }
        }
    }

    @After(order = 1)
    public void saveScreenshot(Scenario scenario) {
        if (scenario.isFailed() && getDriver() != null) {
            try {
                String dateStamp = formatDate("MM-dd-yyyy-HH-mm-ss");
                String screenshotName = scenario.getName().replaceAll(" ", "_");
                Screenshot screenshot = new AShot()
                        .shootingStrategy(ShootingStrategies.viewportPasting(1000))
                        .takeScreenshot(getDriver());

                File screenshotFile = new File(SCREENSHOTS_DIR + File.separator + screenshotName + "-" + dateStamp + ".jpg");
                ImageIO.write(screenshot.getImage(), "jpg", screenshotFile);
                log.info("Screenshot saved: {}", screenshotFile.getAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to save screenshot: {}", e.getMessage());
            }
        }
    }

    /**
     * Deletes files older than 1 day in the given directory.
     */
    public void deleteFiles(String directoryName) {
        long cutoffMs = 24 * 60 * 60 * 1000L; // 1 day in milliseconds
        File directory = new File("." + File.separator + directoryName);
        File[] files = directory.listFiles();

        if (files != null) {
            long now = System.currentTimeMillis();
            for (File file : files) {
                if ((now - file.lastModified()) > cutoffMs) {
                    try {
                        FileUtils.forceDelete(file);
                        log.debug("Deleted old file: {}", file.getName());
                    } catch (IOException e) {
                        log.warn("Failed to delete file {}: {}", file.getName(), e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Formats the current date/time using java.time.
     */
    private String formatDate(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
    /**
     * API test lifecycle management.
     */
    @Before
    public void beforeScenario(Scenario scenario) {
        Log.startTestCase(scenario.getName());
        Log.info("Starting API Scenario: {}", scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        Log.info("Completed API Scenario: {} - Result: {}", scenario.getName(), scenario.getStatus());
//        ScenarioContext.clear();
        Log.endTestCase(scenario.getName());
    }
}