package utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG retry analyzer to automatically retry failed tests.
 * The retry limit is configurable via config.properties.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private final int maxRetryCount;

    public RetryAnalyzer() {
        this.maxRetryCount = new ConfigReader().getMaxRetryCount();
    }

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            if (count < maxRetryCount) {
                count++;
                Log.info("Retrying test: {} in context: {} | Attempt {} of {}",
                        result.getName(),
                        result.getTestContext().getName(),
                        count,
                        maxRetryCount);
                return true;
            } else {
                Log.warn("Maximum retry attempts ({}) reached for test: {}. Marking as failed.",
                        maxRetryCount, result.getName());
            }
        }
        return false;
    }
}