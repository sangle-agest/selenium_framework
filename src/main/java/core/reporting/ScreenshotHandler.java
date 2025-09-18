package core.reporting;

import core.config.ConfigManager;
import core.utils.BrowserUtils;
import core.utils.DateTimeUtils;
import core.utils.FileUtil;
import core.utils.LogUtils;
import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

/**
 * ScreenshotHandler manages screenshot capture and storage
 */
public class ScreenshotHandler {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotHandler.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String SCREENSHOT_DIR = "target/screenshots";

    /**
     * Take screenshot and save to file
     * @param testName Test name for screenshot filename
     * @return Path to saved screenshot
     */
    public static String takeScreenshot(String testName) {
        return takeScreenshot(testName, "screenshot");
    }

    /**
     * Take screenshot with custom prefix
     * @param testName Test name for screenshot filename
     * @param prefix Filename prefix
     * @return Path to saved screenshot
     */
    public static String takeScreenshot(String testName, String prefix) {
        try {
            // Create screenshots directory if it doesn't exist
            FileUtil.createDirectory(SCREENSHOT_DIR);
            
            // Generate filename
            String timestamp = DateTimeUtils.getTimestamp();
            String sanitizedTestName = sanitizeFileName(testName);
            String fileName = String.format("%s_%s_%s.png", prefix, sanitizedTestName, timestamp);
            String filePath = Paths.get(SCREENSHOT_DIR, fileName).toString();
            
            // Take screenshot using BrowserUtils
            String screenshotPath = BrowserUtils.takeScreenshotToFile();
            
            if (screenshotPath != null) {
                // Move screenshot to our desired location with custom name
                FileUtil.moveFile(screenshotPath, filePath);
                
                LogUtils.logTestStep("Screenshot saved: " + filePath);
                logger.info("Screenshot saved: {}", filePath);
                return filePath;
            } else {
                logger.warn("Failed to take screenshot for test: {}", testName);
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Error taking screenshot for test {}: {}", testName, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Take screenshot on test failure
     * @param testName Test name
     * @param throwable Exception that caused failure
     * @return Path to saved screenshot
     */
    public static String takeFailureScreenshot(String testName, Throwable throwable) {
        try {
            String screenshotPath = takeScreenshot(testName, "failure");
            
            if (screenshotPath != null) {
                // Also save failure details as text file
                String failureDetailsPath = screenshotPath.replace(".png", "_details.txt");
                String failureDetails = createFailureDetails(testName, throwable);
                FileUtil.writeStringToFile(failureDetailsPath, failureDetails);
                
                LogUtils.logTestStep("Failure screenshot and details saved: " + screenshotPath);
                logger.info("Failure screenshot and details saved: {}", screenshotPath);
            }
            
            return screenshotPath;
            
        } catch (Exception e) {
            logger.error("Error taking failure screenshot for test {}: {}", testName, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Take screenshot for Allure attachment
     * @param attachmentName Name for the attachment
     * @return Screenshot as byte array
     */
    @Attachment(value = "{attachmentName}", type = "image/png")
    public static byte[] takeScreenshotForAllure(String attachmentName) {
        try {
            byte[] screenshot = BrowserUtils.takeScreenshot();
            logger.debug("Screenshot taken for Allure attachment: {}", attachmentName);
            return screenshot;
        } catch (Exception e) {
            logger.warn("Failed to take screenshot for Allure: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Take screenshot on test step
     * @param stepName Step name
     * @return Path to saved screenshot
     */
    public static String takeStepScreenshot(String stepName) {
        try {
            String screenshotPath = takeScreenshot(stepName, "step");
            
            if (screenshotPath != null) {
                LogUtils.logTestStep("Step screenshot saved: " + screenshotPath);
                logger.debug("Step screenshot saved: {}", screenshotPath);
            }
            
            return screenshotPath;
            
        } catch (Exception e) {
            logger.error("Error taking step screenshot for {}: {}", stepName, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Take screenshot before and after an action
     * @param actionName Action name
     * @param action Runnable action to execute
     * @return Array of screenshot paths [before, after]
     */
    public static String[] takeBeforeAfterScreenshots(String actionName, Runnable action) {
        String beforePath = null;
        String afterPath = null;
        
        try {
            // Take before screenshot
            beforePath = takeScreenshot(actionName, "before");
            
            // Execute action
            action.run();
            
            // Take after screenshot
            afterPath = takeScreenshot(actionName, "after");
            
            LogUtils.logTestStep("Before/After screenshots taken for: " + actionName);
            logger.debug("Before/After screenshots taken for: {}", actionName);
            
        } catch (Exception e) {
            logger.error("Error taking before/after screenshots for {}: {}", actionName, e.getMessage(), e);
        }
        
        return new String[]{beforePath, afterPath};
    }

    /**
     * Clean up old screenshots
     * @param daysOld Number of days to keep screenshots
     */
    public static void cleanupOldScreenshots(int daysOld) {
        try {
            File screenshotDir = new File(SCREENSHOT_DIR);
            
            if (!screenshotDir.exists()) {
                return;
            }
            
            long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60L * 60L * 1000L);
            File[] files = screenshotDir.listFiles();
            
            if (files != null) {
                int deletedCount = 0;
                for (File file : files) {
                    if (file.isFile() && file.lastModified() < cutoffTime) {
                        if (file.delete()) {
                            deletedCount++;
                        }
                    }
                }
                
                logger.info("Cleaned up {} old screenshots (older than {} days)", deletedCount, daysOld);
            }
            
        } catch (Exception e) {
            logger.error("Error cleaning up old screenshots: {}", e.getMessage(), e);
        }
    }

    /**
     * Get screenshots directory path
     * @return Screenshots directory path
     */
    public static String getScreenshotDirectory() {
        return SCREENSHOT_DIR;
    }

    /**
     * Check if screenshot taking is enabled
     * @return True if screenshots should be taken
     */
    public static boolean isScreenshotEnabled() {
        return config.isScreenshotOnFailure() || config.isScreenshotOnSuccess();
    }

    /**
     * Sanitize filename by removing invalid characters
     * @param fileName Original filename
     * @return Sanitized filename
     */
    private static String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return "unknown";
        }
        
        // Replace invalid characters with underscore
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_")
                      .replaceAll("_{2,}", "_")  // Replace multiple underscores with single
                      .replaceAll("^_|_$", ""); // Remove leading/trailing underscores
    }

    /**
     * Create failure details text
     * @param testName Test name
     * @param throwable Exception
     * @return Failure details as string
     */
    private static String createFailureDetails(String testName, Throwable throwable) {
        StringBuilder details = new StringBuilder();
        
        details.append("FAILURE DETAILS\n");
        details.append("================\n\n");
        details.append("Test Name: ").append(testName).append("\n");
        details.append("Timestamp: ").append(DateTimeUtils.getCurrentDateTime()).append("\n");
        details.append("Browser: ").append(config.getBrowser()).append("\n");
        details.append("Headless: ").append(config.isHeadless()).append("\n");
        
        try {
            details.append("Current URL: ").append(BrowserUtils.getCurrentUrl()).append("\n");
            details.append("Page Title: ").append(BrowserUtils.getPageTitle()).append("\n");
        } catch (Exception e) {
            details.append("Current URL: Unable to retrieve (").append(e.getMessage()).append(")\n");
            details.append("Page Title: Unable to retrieve\n");
        }
        
        details.append("\nEXCEPTION DETAILS\n");
        details.append("=================\n");
        details.append("Exception Type: ").append(throwable.getClass().getSimpleName()).append("\n");
        details.append("Message: ").append(throwable.getMessage()).append("\n\n");
        
        details.append("STACK TRACE\n");
        details.append("===========\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            details.append(element.toString()).append("\n");
        }
        
        // Add cause if present
        Throwable cause = throwable.getCause();
        if (cause != null) {
            details.append("\nCaused by: ").append(cause.getClass().getSimpleName());
            details.append(": ").append(cause.getMessage()).append("\n");
            for (StackTraceElement element : cause.getStackTrace()) {
                details.append("\tat ").append(element.toString()).append("\n");
            }
        }
        
        return details.toString();
    }

    /**
     * Take full page screenshot (if supported by browser)
     * @param testName Test name
     * @return Path to saved screenshot
     */
    public static String takeFullPageScreenshot(String testName) {
        try {
            // This is a placeholder for full page screenshot functionality
            // Implementation would depend on specific browser capabilities
            logger.info("Taking full page screenshot for test: {}", testName);
            
            // For now, use regular screenshot
            return takeScreenshot(testName, "fullpage");
            
        } catch (Exception e) {
            logger.error("Error taking full page screenshot for test {}: {}", testName, e.getMessage(), e);
            return takeScreenshot(testName, "fullpage_fallback");
        }
    }
}