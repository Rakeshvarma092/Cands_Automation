package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Static utility class for application-wide logging using Log4j2.
 * Provides support for parameterized messages and exception logging.
 */
public class Log {

    private static final Logger log = LogManager.getLogger(Log.class);

    /**
     * Prints a visual marker at the start of a test case.
     */
    public static void startTestCase(String sTestCaseName) {
        log.info("****************************************************************************************");
        log.info("$$ START TEST CASE: {} $$", sTestCaseName);
        log.info("****************************************************************************************");
    }

    /**
     * Prints a visual marker at the end of a test case.
     */
    public static void endTestCase(String sTestCaseName) {
        log.info("--------------------------- END TEST CASE: {} ---------------------------", sTestCaseName);
        log.info(" ");
    }

    // --- INFO ---
    public static void info(String message) {
        log.info(message);
    }

    public static void info(String message, Object... params) {
        log.info(message, params);
    }

    // --- WARN ---
    public static void warn(String message) {
        log.warn(message);
    }

    public static void warn(String message, Object... params) {
        log.warn(message, params);
    }

    // --- ERROR ---
    public static void error(String message) {
        log.error(message);
    }

    public static void error(String message, Object... params) {
        log.error(message, params);
    }

    public static void error(String message, Throwable t) {
        log.error(message, t);
    }

    // --- FATAL ---
    public static void fatal(String message) {
        log.fatal(message);
    }

    public static void fatal(String message, Object... params) {
        log.fatal(message, params);
    }

    public static void fatal(String message, Throwable t) {
        log.fatal(message, t);
    }

    // --- DEBUG ---
    public static void debug(String message) {
        log.debug(message);
    }

    public static void debug(String message, Object... params) {
        log.debug(message, params);
    }
}