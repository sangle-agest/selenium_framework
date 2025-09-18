package core.reporting;

import core.config.ConfigManager;
import core.utils.BrowserUtils;
import core.utils.DateTimeUtils;
import core.utils.LogUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * AllureListener handles Allure reporting events and attachments
 */
public class AllureListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(AllureListener.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = getTestName(result);
        String className = result.getTestClass().getName();
        
        LogUtils.logTestSetup(className, testName);
        logger.info("Starting test: {}.{}", className, testName);
        
        // Add test information to Allure
        Allure.epic("Automation Tests");
        Allure.feature(className);
        Allure.story(testName);
        
        // Add environment information
        addEnvironmentInfo();
        
        // Mark test start time
        result.setAttribute("startTime", System.currentTimeMillis());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = getTestName(result);
        long duration = calculateDuration(result);
        
        LogUtils.logTestResult(testName, "PASS", duration);
        logger.info("Test passed: {} (Duration: {}ms)", testName, duration);
        
        // Take screenshot on success if configured
        if (config.isScreenshotOnSuccess()) {
            attachScreenshot("Test Passed Screenshot");
        }
        
        LogUtils.logTestTeardown();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = getTestName(result);
        long duration = calculateDuration(result);
        Throwable throwable = result.getThrowable();
        
        LogUtils.logTestResult(testName, "FAIL", duration);
        LogUtils.logError("Test failed: " + testName, throwable);
        logger.error("Test failed: {} (Duration: {}ms)", testName, duration, throwable);
        
        // Attach failure information
        attachScreenshot("Failure Screenshot");
        attachPageSource("Page Source at Failure");
        attachBrowserLogs("Browser Console Logs");
        attachFailureDetails(throwable);
        
        LogUtils.logTestTeardown();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = getTestName(result);
        long duration = calculateDuration(result);
        
        LogUtils.logTestResult(testName, "SKIP", duration);
        logger.warn("Test skipped: {} (Duration: {}ms)", testName, duration);
        
        if (result.getThrowable() != null) {
            LogUtils.logError("Test skipped due to: " + result.getThrowable().getMessage(), result.getThrowable());
            attachFailureDetails(result.getThrowable());
        }
        
        LogUtils.logTestTeardown();
    }

    /**
     * Get test name from test result
     * @param result Test result
     * @return Test name
     */
    private String getTestName(ITestResult result) {
        return result.getMethod().getMethodName();
    }

    /**
     * Calculate test duration
     * @param result Test result
     * @return Duration in milliseconds
     */
    private long calculateDuration(ITestResult result) {
        Long startTime = (Long) result.getAttribute("startTime");
        if (startTime != null) {
            return System.currentTimeMillis() - startTime;
        }
        return result.getEndMillis() - result.getStartMillis();
    }

    /**
     * Add environment information to Allure
     */
    private void addEnvironmentInfo() {
        try {
            // Browser information
            Allure.parameter("Browser", config.getBrowser());
            Allure.parameter("Headless", String.valueOf(config.isHeadless()));
            Allure.parameter("Remote Execution", String.valueOf(config.isRemoteExecution()));
            
            if (config.isRemoteExecution()) {
                Allure.parameter("Remote URL", config.getRemoteUrl());
            }
            
            // System information
            Allure.parameter("OS", System.getProperty("os.name"));
            Allure.parameter("OS Version", System.getProperty("os.version"));
            Allure.parameter("Java Version", System.getProperty("java.version"));
            Allure.parameter("User", System.getProperty("user.name"));
            
            // Test configuration
            Allure.parameter("Base URL", config.getBaseUrl());
            Allure.parameter("Timeout", String.valueOf(config.getTimeout()));
            Allure.parameter("Test Execution Time", DateTimeUtils.getCurrentDateTime());
            
            logger.debug("Environment information added to Allure");
        } catch (Exception e) {
            logger.warn("Failed to add environment information to Allure: {}", e.getMessage());
        }
    }

    /**
     * Attach screenshot to Allure report
     * @param name Screenshot name
     */
    @Attachment(value = "{name}", type = "image/png")
    public byte[] attachScreenshot(String name) {
        try {
            byte[] screenshot = BrowserUtils.takeScreenshot();
            logger.debug("Screenshot attached to Allure: {}", name);
            return screenshot;
        } catch (Exception e) {
            logger.warn("Failed to attach screenshot to Allure: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Attach page source to Allure report
     * @param name Attachment name
     */
    @Attachment(value = "{name}", type = "text/html")
    public String attachPageSource(String name) {
        try {
            String pageSource = (String) BrowserUtils.executeJavaScript("return document.documentElement.outerHTML;");
            logger.debug("Page source attached to Allure: {}", name);
            return pageSource;
        } catch (Exception e) {
            logger.warn("Failed to attach page source to Allure: {}", e.getMessage());
            return "Failed to capture page source: " + e.getMessage();
        }
    }

    /**
     * Attach browser console logs to Allure report
     * @param name Attachment name
     */
    @Attachment(value = "{name}", type = "text/plain")
    public String attachBrowserLogs(String name) {
        try {
            // Note: Browser console logs retrieval depends on browser capabilities
            // This is a placeholder implementation
            String logs = "Browser console logs not available or not implemented for current browser configuration.";
            logger.debug("Browser logs attached to Allure: {}", name);
            return logs;
        } catch (Exception e) {
            logger.warn("Failed to attach browser logs to Allure: {}", e.getMessage());
            return "Failed to capture browser logs: " + e.getMessage();
        }
    }

    /**
     * Attach failure details to Allure report
     * @param throwable Exception that caused the failure
     */
    @Attachment(value = "Failure Details", type = "text/plain")
    public String attachFailureDetails(Throwable throwable) {
        try {
            StringBuilder details = new StringBuilder();
            details.append("Exception: ").append(throwable.getClass().getSimpleName()).append("\n");
            details.append("Message: ").append(throwable.getMessage()).append("\n");
            details.append("Timestamp: ").append(DateTimeUtils.getCurrentDateTime()).append("\n");
            details.append("Browser: ").append(config.getBrowser()).append("\n");
            details.append("Current URL: ");
            
            try {
                details.append(BrowserUtils.getCurrentUrl());
            } catch (Exception e) {
                details.append("Unable to get current URL: ").append(e.getMessage());
            }
            
            details.append("\n\nStack Trace:\n");
            for (StackTraceElement element : throwable.getStackTrace()) {
                details.append(element.toString()).append("\n");
            }
            
            logger.debug("Failure details attached to Allure");
            return details.toString();
        } catch (Exception e) {
            logger.warn("Failed to attach failure details to Allure: {}", e.getMessage());
            return "Failed to capture failure details: " + e.getMessage();
        }
    }

    /**
     * Attach custom text to Allure report
     * @param name Attachment name
     * @param content Text content
     */
    public static void attachText(String name, String content) {
        try {
            Allure.addAttachment(name, "text/plain", content);
            logger.debug("Text attached to Allure: {}", name);
        } catch (Exception e) {
            logger.warn("Failed to attach text to Allure: {}", e.getMessage());
        }
    }

    /**
     * Attach JSON to Allure report
     * @param name Attachment name
     * @param jsonContent JSON content
     */
    public static void attachJson(String name, String jsonContent) {
        try {
            Allure.addAttachment(name, "application/json", jsonContent);
            logger.debug("JSON attached to Allure: {}", name);
        } catch (Exception e) {
            logger.warn("Failed to attach JSON to Allure: {}", e.getMessage());
        }
    }

    /**
     * Attach file to Allure report
     * @param name Attachment name
     * @param filePath Path to file
     */
    public static void attachFile(String name, String filePath) {
        try {
            Allure.addAttachment(name, new ByteArrayInputStream(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath))));
            logger.debug("File attached to Allure: {} ({})", name, filePath);
        } catch (Exception e) {
            logger.warn("Failed to attach file to Allure: {}", e.getMessage());
        }
    }

    /**
     * Add step to Allure report
     * @param stepName Step name
     * @param status Step status
     */
    public static void addStep(String stepName, Status status) {
        try {
            Allure.step(stepName, status);
            logger.debug("Step added to Allure: {} - {}", stepName, status);
        } catch (Exception e) {
            logger.warn("Failed to add step to Allure: {}", e.getMessage());
        }
    }

    /**
     * Add link to Allure report
     * @param name Link name
     * @param url Link URL
     */
    public static void addLink(String name, String url) {
        try {
            Allure.link(name, url);
            logger.debug("Link added to Allure: {} - {}", name, url);
        } catch (Exception e) {
            logger.warn("Failed to add link to Allure: {}", e.getMessage());
        }
    }
}