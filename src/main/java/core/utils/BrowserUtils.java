package core.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * BrowserUtils provides utility methods for browser operations
 */
public class BrowserUtils {
    private static final Logger logger = LoggerFactory.getLogger(BrowserUtils.class);

    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    @Step("Navigate to URL: {url}")
    public static void navigateToUrl(String url) {
        logger.info("Navigating to URL: {}", url);
        try {
            Selenide.open(url);
            logger.debug("Successfully navigated to: {}", url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL {}: {}", url, e.getMessage());
            throw new RuntimeException("Failed to navigate to URL: " + url, e);
        }
    }

    /**
     * Get current URL
     * @return Current browser URL
     */
    @Step("Get current URL")
    public static String getCurrentUrl() {
        try {
            String url = Selenide.webdriver().driver().url();
            logger.debug("Current URL: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("Failed to get current URL: {}", e.getMessage());
            throw new RuntimeException("Failed to get current URL", e);
        }
    }

    /**
     * Get page title
     * @return Current page title
     */
    @Step("Get page title")
    public static String getPageTitle() {
        try {
            String title = Selenide.title();
            logger.debug("Page title: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to get page title: {}", e.getMessage());
            throw new RuntimeException("Failed to get page title", e);
        }
    }

    /**
     * Refresh current page
     */
    @Step("Refresh page")
    public static void refreshPage() {
        logger.info("Refreshing page");
        try {
            Selenide.refresh();
            logger.debug("Page refreshed successfully");
        } catch (Exception e) {
            logger.error("Failed to refresh page: {}", e.getMessage());
            throw new RuntimeException("Failed to refresh page", e);
        }
    }

    /**
     * Navigate back in browser history
     */
    @Step("Navigate back")
    public static void navigateBack() {
        logger.info("Navigating back");
        try {
            Selenide.back();
            logger.debug("Navigated back successfully");
        } catch (Exception e) {
            logger.error("Failed to navigate back: {}", e.getMessage());
            throw new RuntimeException("Failed to navigate back", e);
        }
    }

    /**
     * Navigate forward in browser history
     */
    @Step("Navigate forward")
    public static void navigateForward() {
        logger.info("Navigating forward");
        try {
            Selenide.forward();
            logger.debug("Navigated forward successfully");
        } catch (Exception e) {
            logger.error("Failed to navigate forward: {}", e.getMessage());
            throw new RuntimeException("Failed to navigate forward", e);
        }
    }

    /**
     * Maximize browser window
     */
    @Step("Maximize browser window")
    public static void maximizeWindow() {
        logger.info("Maximizing browser window");
        try {
            WebDriverRunner.getWebDriver().manage().window().maximize();
            logger.debug("Browser window maximized");
        } catch (Exception e) {
            logger.error("Failed to maximize window: {}", e.getMessage());
            throw new RuntimeException("Failed to maximize window", e);
        }
    }

    /**
     * Switch to window by index
     * @param windowIndex Window index (0-based)
     */
    @Step("Switch to window by index: {windowIndex}")
    public static void switchToWindow(int windowIndex) {
        logger.info("Switching to window by index: {}", windowIndex);
        try {
            Selenide.switchTo().window(windowIndex);
            logger.debug("Switched to window index: {}", windowIndex);
        } catch (Exception e) {
            logger.error("Failed to switch to window index {}: {}", windowIndex, e.getMessage());
            throw new RuntimeException("Failed to switch to window", e);
        }
    }

    /**
     * Switch to window by title
     * @param windowTitle Window title
     */
    @Step("Switch to window by title: {windowTitle}")
    public static void switchToWindowByTitle(String windowTitle) {
        logger.info("Switching to window by title: {}", windowTitle);
        try {
            Selenide.switchTo().window(windowTitle);
            logger.debug("Switched to window with title: {}", windowTitle);
        } catch (Exception e) {
            logger.error("Failed to switch to window with title {}: {}", windowTitle, e.getMessage());
            throw new RuntimeException("Failed to switch to window", e);
        }
    }

    /**
     * Get all window handles
     * @return Set of window handles
     */
    @Step("Get all window handles")
    public static Set<String> getAllWindowHandles() {
        try {
            Set<String> handles = WebDriverRunner.getWebDriver().getWindowHandles();
            logger.debug("Found {} window handles", handles.size());
            return handles;
        } catch (Exception e) {
            logger.error("Failed to get window handles: {}", e.getMessage());
            throw new RuntimeException("Failed to get window handles", e);
        }
    }

    /**
     * Close current window
     */
    @Step("Close current window")
    public static void closeCurrentWindow() {
        logger.info("Closing current window");
        try {
            Selenide.closeWindow();
            logger.debug("Current window closed");
        } catch (Exception e) {
            logger.error("Failed to close current window: {}", e.getMessage());
            throw new RuntimeException("Failed to close current window", e);
        }
    }

    /**
     * Close all windows and quit driver
     */
    @Step("Close all windows and quit driver")
    public static void quitDriver() {
        logger.info("Closing all windows and quitting driver");
        try {
            Selenide.closeWebDriver();
            logger.debug("All windows closed and driver quit");
        } catch (Exception e) {
            logger.error("Failed to quit driver: {}", e.getMessage());
        }
    }

    /**
     * Clear all cookies
     */
    @Step("Clear all cookies")
    public static void clearCookies() {
        logger.info("Clearing all cookies");
        try {
            Selenide.clearBrowserCookies();
            logger.debug("All cookies cleared");
        } catch (Exception e) {
            logger.error("Failed to clear cookies: {}", e.getMessage());
        }
    }

    /**
     * Clear local storage
     */
    @Step("Clear local storage")
    public static void clearLocalStorage() {
        logger.info("Clearing local storage");
        try {
            Selenide.localStorage().clear();
            logger.debug("Local storage cleared");
        } catch (Exception e) {
            logger.error("Failed to clear local storage: {}", e.getMessage());
        }
    }

    /**
     * Clear session storage
     */
    @Step("Clear session storage")
    public static void clearSessionStorage() {
        logger.info("Clearing session storage");
        try {
            Selenide.sessionStorage().clear();
            logger.debug("Session storage cleared");
        } catch (Exception e) {
            logger.error("Failed to clear session storage: {}", e.getMessage());
        }
    }

    /**
     * Take screenshot
     * @return Screenshot as byte array for Allure attachment
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] takeScreenshot() {
        logger.info("Taking screenshot");
        try {
            WebDriver driver = WebDriverRunner.getWebDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            logger.debug("Screenshot taken successfully");
            return screenshot;
        } catch (Exception e) {
            logger.error("Failed to take screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Take screenshot and save to file
     * @return File path of saved screenshot
     */
    @Step("Take screenshot and save to file")
    public static String takeScreenshotToFile() {
        logger.info("Taking screenshot and saving to file");
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "screenshot_" + timestamp;  // Remove .png extension here
            
            String filePath = Selenide.screenshot(fileName);
            logger.debug("Raw Selenide screenshot path: {}", filePath);
            
            // Convert file URI to actual file path if needed
            if (filePath != null && filePath.startsWith("file://")) {
                filePath = filePath.replace("file://", "");
                logger.debug("Converted file URI path to: {}", filePath);
            }
            
            logger.debug("Final screenshot path: {}", filePath);
            return filePath;
        } catch (Exception e) {
            logger.error("Failed to save screenshot to file: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Execute JavaScript
     * @param script JavaScript code to execute
     * @return Result of script execution
     */
    @Step("Execute JavaScript")
    public static Object executeJavaScript(String script) {
        logger.info("Executing JavaScript: {}", script);
        try {
            Object result = Selenide.executeJavaScript(script);
            logger.debug("JavaScript executed successfully");
            return result;
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to execute JavaScript", e);
        }
    }

    /**
     * Execute JavaScript with arguments
     * @param script JavaScript code to execute
     * @param args Arguments to pass to script
     * @return Result of script execution
     */
    @Step("Execute JavaScript with arguments")
    public static Object executeJavaScript(String script, Object... args) {
        logger.info("Executing JavaScript with arguments: {}", script);
        try {
            Object result = Selenide.executeJavaScript(script, args);
            logger.debug("JavaScript executed successfully with arguments");
            return result;
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript with arguments: {}", e.getMessage());
            throw new RuntimeException("Failed to execute JavaScript", e);
        }
    }

    /**
     * Scroll to top of page
     */
    @Step("Scroll to top of page")
    public static void scrollToTop() {
        logger.info("Scrolling to top of page");
        executeJavaScript("window.scrollTo(0, 0);");
    }

    /**
     * Scroll to bottom of page
     */
    @Step("Scroll to bottom of page")
    public static void scrollToBottom() {
        logger.info("Scrolling to bottom of page");
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Zoom page
     * @param zoomLevel Zoom level (e.g., 0.5 for 50%, 1.5 for 150%)
     */
    @Step("Zoom page to level: {zoomLevel}")
    public static void zoomPage(double zoomLevel) {
        logger.info("Zooming page to level: {}", zoomLevel);
        executeJavaScript("document.body.style.zoom='" + zoomLevel + "'");
    }

    /**
     * Get page load state
     * @return Page load state (loading, interactive, complete)
     */
    @Step("Get page load state")
    public static String getPageLoadState() {
        logger.debug("Getting page load state");
        try {
            return (String) executeJavaScript("return document.readyState;");
        } catch (Exception e) {
            logger.error("Failed to get page load state: {}", e.getMessage());
            return "unknown";
        }
    }

    /**
     * Wait for page to be fully loaded
     */
    @Step("Wait for page to be fully loaded")
    public static void waitForPageLoad() {
        logger.info("Waiting for page to be fully loaded");
        WaitUtils.waitForPageLoad();
    }
}