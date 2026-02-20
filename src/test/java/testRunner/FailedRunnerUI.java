package testRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * Re-runs failed Cucumber scenarios listed in testData/rerun.txt.
 * Only scenarios tagged with @Branch are included.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"@testData/rerun.txt"},
        glue = {"stepDefinitions"},
        tags = "@Branch",
        plugin = {"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        monochrome = true
)
public class FailedRunnerUI {
}