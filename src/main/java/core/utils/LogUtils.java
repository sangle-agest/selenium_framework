package core.utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * LogUtils provides utility methods for enhanced logging operations
 */
public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * Log test step with INFO level
     * @param message Log message
     */
    @Step("Test Step: {message}")
    public static void logTestStep(String message) {
        logger.info("TEST STEP: {}", message);
    }

    /**
     * Log test step with custom level
     * @param message Log message
     * @param level Log level (INFO, DEBUG, WARN, ERROR)
     */
    @Step("Test Step: {message}")
    public static void logTestStep(String message, String level) {
        switch (level.toUpperCase()) {
            case "DEBUG":
                logger.debug("TEST STEP: {}", message);
                break;
            case "WARN":
                logger.warn("TEST STEP: {}", message);
                break;
            case "ERROR":
                logger.error("TEST STEP: {}", message);
                break;
            case "INFO":
            default:
                logger.info("TEST STEP: {}", message);
                break;
        }
    }

    /**
     * Log test assertion
     * @param assertion Assertion description
     * @param result Assertion result
     */
    @Step("Assertion: {assertion} = {result}")
    public static void logAssertion(String assertion, boolean result) {
        if (result) {
            logger.info("ASSERTION PASSED: {}", assertion);
        } else {
            logger.error("ASSERTION FAILED: {}", assertion);
        }
    }

    /**
     * Log verification step with INFO level
     * @param message Verification message
     */
    @Step("Verification Step: {message}")
    public static void logVerificationStep(String message) {
        logger.info("VERIFICATION STEP: {}", message);
    }

    /**
     * Log test data
     * @param dataName Data name
     * @param dataValue Data value
     */
    @Step("Test Data: {dataName} = {dataValue}")
    public static void logTestData(String dataName, Object dataValue) {
        logger.info("TEST DATA: {} = {}", dataName, dataValue);
    }

    /**
     * Log API request
     * @param method HTTP method
     * @param url Request URL
     * @param payload Request payload (optional)
     */
    @Step("API Request: {method} {url}")
    public static void logApiRequest(String method, String url, String payload) {
        logger.info("API REQUEST: {} {}", method, url);
        if (payload != null && !payload.isEmpty()) {
            logger.debug("Request Payload: {}", payload);
        }
    }

    /**
     * Log API response
     * @param statusCode Response status code
     * @param responseBody Response body (optional)
     */
    @Step("API Response: Status {statusCode}")
    public static void logApiResponse(int statusCode, String responseBody) {
        logger.info("API RESPONSE: Status {}", statusCode);
        if (responseBody != null && !responseBody.isEmpty()) {
            logger.debug("Response Body: {}", responseBody);
        }
    }

    /**
     * Log performance metrics
     * @param operation Operation name
     * @param duration Duration in milliseconds
     */
    @Step("Performance: {operation} took {duration}ms")
    public static void logPerformance(String operation, long duration) {
        logger.info("PERFORMANCE: {} took {}ms", operation, duration);
        
        // Add warning for slow operations
        if (duration > 5000) {
            logger.warn("SLOW OPERATION: {} took {}ms (>5s)", operation, duration);
        }
    }

    /**
     * Log browser action
     * @param action Action description
     * @param element Element description (optional)
     */
    @Step("Browser Action: {action}")
    public static void logBrowserAction(String action, String element) {
        if (element != null && !element.isEmpty()) {
            logger.info("BROWSER ACTION: {} on [{}]", action, element);
        } else {
            logger.info("BROWSER ACTION: {}", action);
        }
    }

    /**
     * Log test result
     * @param testName Test name
     * @param result Test result (PASS/FAIL)
     * @param duration Test duration in milliseconds
     */
    @Step("Test Result: {testName} = {result}")
    public static void logTestResult(String testName, String result, long duration) {
        String message = String.format("TEST RESULT: %s = %s (Duration: %dms)", testName, result, duration);
        
        if ("PASS".equalsIgnoreCase(result) || "PASSED".equalsIgnoreCase(result)) {
            logger.info(message);
        } else {
            logger.error(message);
        }
    }

    /**
     * Log environment information
     * @param key Environment key
     * @param value Environment value
     */
    public static void logEnvironmentInfo(String key, String value) {
        logger.info("ENVIRONMENT: {} = {}", key, value);
    }

    /**
     * Log all system properties
     */
    @Step("Log system properties")
    public static void logSystemProperties() {
        logger.info("=== SYSTEM PROPERTIES ===");
        System.getProperties().forEach((key, value) -> 
            logger.info("System Property: {} = {}", key, value));
        logger.info("=== END SYSTEM PROPERTIES ===");
    }

    /**
     * Log separator for test sections
     * @param sectionName Section name
     */
    @Step("Section: {sectionName}")
    public static void logSection(String sectionName) {
        String separator = "=".repeat(50);
        logger.info(separator);
        logger.info("SECTION: {}", sectionName);
        logger.info(separator);
    }

    /**
     * Log test setup information
     * @param testClass Test class name
     * @param testMethod Test method name
     */
    @Step("Test Setup: {testClass}.{testMethod}")
    public static void logTestSetup(String testClass, String testMethod) {
        logger.info("=== TEST SETUP ===");
        logger.info("Test Class: {}", testClass);
        logger.info("Test Method: {}", testMethod);
        logger.info("Start Time: {}", DateTimeUtils.getCurrentDateTime());
        
        // Add to MDC for correlation
        MDC.put("testClass", testClass);
        MDC.put("testMethod", testMethod);
    }

    /**
     * Log test teardown
     */
    @Step("Test Teardown")
    public static void logTestTeardown() {
        logger.info("=== TEST TEARDOWN ===");
        logger.info("End Time: {}", DateTimeUtils.getCurrentDateTime());
        
        // Clear MDC
        MDC.clear();
    }

    /**
     * Log error with stack trace
     * @param message Error message
     * @param throwable Exception/Throwable
     */
    @Step("Error: {message}")
    public static void logError(String message, Throwable throwable) {
        logger.error("ERROR: {}", message, throwable);
    }

    /**
     * Log warning
     * @param message Warning message
     */
    @Step("Warning: {message}")
    public static void logWarning(String message) {
        logger.warn("WARNING: {}", message);
    }

    /**
     * Log page action
     * @param action Page action description
     */
    @Step("Page Action: {action}")
    public static void logPageAction(String action) {
        logger.info("PAGE ACTION: {}", action);
    }

    /**
     * Log debug information
     * @param message Debug message
     */
    public static void logDebug(String message) {
        logger.debug("DEBUG: {}", message);
    }

    /**
     * Log general information
     * @param message Info message
     */
    public static void logInfo(String message) {
        logger.info("{}", message);
    }

    /**
     * Attach text log to Allure report
     * @param logContent Log content
     * @param attachmentName Attachment name
     * @return Log content as byte array
     */
    @Attachment(value = "{attachmentName}", type = "text/plain")
    public static byte[] attachLogToAllure(String logContent, String attachmentName) {
        logger.debug("Attaching log to Allure: {}", attachmentName);
        return logContent.getBytes();
    }

    /**
     * Create a formatted log message
     * @param template Message template
     * @param args Arguments to substitute
     * @return Formatted message
     */
    public static String formatMessage(String template, Object... args) {
        try {
            return String.format(template, args);
        } catch (Exception e) {
            logger.warn("Failed to format message template: {}", template);
            return template;
        }
    }

    /**
     * Log with timestamp
     * @param message Message to log
     */
    @Step("Timestamped Log: {message}")
    public static void logWithTimestamp(String message) {
        String timestampedMessage = String.format("[%s] %s", DateTimeUtils.getCurrentDateTime(), message);
        logger.info(timestampedMessage);
    }

    /**
     * Log execution time of an operation
     * @param operationName Operation name
     * @param startTime Start time in milliseconds
     */
    @Step("Execution Time: {operationName}")
    public static void logExecutionTime(String operationName, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logPerformance(operationName, duration);
    }

    /**
     * Log method entry
     * @param className Class name
     * @param methodName Method name
     * @param args Method arguments
     */
    public static void logMethodEntry(String className, String methodName, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug("ENTERING: {}.{}({})", className, methodName, 
                        args.length > 0 ? String.join(", ", 
                        java.util.Arrays.stream(args).map(String::valueOf).toArray(String[]::new)) : "");
        }
    }

    /**
     * Log method exit
     * @param className Class name
     * @param methodName Method name
     * @param result Method result
     */
    public static void logMethodExit(String className, String methodName, Object result) {
        if (logger.isDebugEnabled()) {
            logger.debug("EXITING: {}.{} -> {}", className, methodName, 
                        result != null ? result.toString() : "void");
        }
    }
}