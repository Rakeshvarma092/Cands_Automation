package testRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * Primary Cucumber test runner.
 * Runs scenarios tagged with @Branch1 and writes failed scenarios to testData/rerun.txt.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"stepDefinitions"},
        tags = "@Branch21",
        plugin = {"pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "rerun:testData/rerun.txt"},
        monochrome = true
)
public class TestRunnerUI {
}