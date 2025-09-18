package core.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Utility class for handling multiple windows and tabs
 */
public class WindowUtils {
    private static final Logger logger = LoggerFactory.getLogger(WindowUtils.class);

    /**
     * Switch to a new window/tab
     * @return Window handle of the new window
     */
    @Step("Switch to new window")
    public static String switchToNewWindow() {
        logger.info("Switching to new window");
        try {
            Set<String> allWindows = getWebDriver().getWindowHandles();
            String currentWindow = getWebDriver().getWindowHandle();
            
            for (String window : allWindows) {
                if (!window.equals(currentWindow)) {
                    getWebDriver().switchTo().window(window);
                    logger.debug("Switched to new window: {}", window);
                    return window;
                }
            }
            
            logger.warn("No new window found to switch to");
            return currentWindow;
        } catch (Exception e) {
            logger.error("Failed to switch to new window: {}", e.getMessage());
            throw new RuntimeException("Failed to switch to new window", e);
        }
    }

    /**
     * Switch to window by handle
     * @param windowHandle Window handle to switch to
     */
    @Step("Switch to window: {windowHandle}")
    public static void switchToWindow(String windowHandle) {
        logger.info("Switching to window: {}", windowHandle);
        try {
            getWebDriver().switchTo().window(windowHandle);
            logger.debug("Successfully switched to window: {}", windowHandle);
        } catch (Exception e) {
            logger.error("Failed to switch to window {}: {}", windowHandle, e.getMessage());
            throw new RuntimeException("Failed to switch to window " + windowHandle, e);
        }
    }

    /**
     * Switch to window by index (0-based)
     * @param windowIndex Index of the window to switch to
     */
    @Step("Switch to window by index: {windowIndex}")
    public static void switchToWindowByIndex(int windowIndex) {
        logger.info("Switching to window by index: {}", windowIndex);
        try {
            Set<String> allWindows = getWebDriver().getWindowHandles();
            if (windowIndex >= allWindows.size()) {
                throw new IndexOutOfBoundsException("Window index " + windowIndex + " is out of bounds. Only " + allWindows.size() + " windows available.");
            }
            
            String[] windowArray = allWindows.toArray(new String[0]);
            switchToWindow(windowArray[windowIndex]);
            logger.debug("Successfully switched to window at index: {}", windowIndex);
        } catch (Exception e) {
            logger.error("Failed to switch to window by index {}: {}", windowIndex, e.getMessage());
            throw new RuntimeException("Failed to switch to window by index " + windowIndex, e);
        }
    }

    /**
     * Switch to window by title
     * @param title Title of the window to switch to
     * @return True if window was found and switched to
     */
    @Step("Switch to window by title: {title}")
    public static boolean switchToWindowByTitle(String title) {
        logger.info("Switching to window by title: {}", title);
        try {
            Set<String> allWindows = getWebDriver().getWindowHandles();
            String currentWindow = getWebDriver().getWindowHandle();
            
            for (String window : allWindows) {
                getWebDriver().switchTo().window(window);
                if (getWebDriver().getTitle().equals(title)) {
                    logger.debug("Successfully switched to window with title: {}", title);
                    return true;
                }
            }
            
            // If not found, switch back to original window
            getWebDriver().switchTo().window(currentWindow);
            logger.warn("No window found with title: {}", title);
            return false;
        } catch (Exception e) {
            logger.error("Failed to switch to window by title {}: {}", title, e.getMessage());
            throw new RuntimeException("Failed to switch to window by title " + title, e);
        }
    }

    /**
     * Switch to window by URL
     * @param url URL of the window to switch to
     * @return True if window was found and switched to
     */
    @Step("Switch to window by URL: {url}")
    public static boolean switchToWindowByUrl(String url) {
        logger.info("Switching to window by URL: {}", url);
        try {
            Set<String> allWindows = getWebDriver().getWindowHandles();
            String currentWindow = getWebDriver().getWindowHandle();
            
            for (String window : allWindows) {
                getWebDriver().switchTo().window(window);
                if (getWebDriver().getCurrentUrl().contains(url)) {
                    logger.debug("Successfully switched to window with URL containing: {}", url);
                    return true;
                }
            }
            
            // If not found, switch back to original window
            getWebDriver().switchTo().window(currentWindow);
            logger.warn("No window found with URL containing: {}", url);
            return false;
        } catch (Exception e) {
            logger.error("Failed to switch to window by URL {}: {}", url, e.getMessage());
            throw new RuntimeException("Failed to switch to window by URL " + url, e);
        }
    }

    /**
     * Close current window and switch to previous window
     */
    @Step("Close current window and switch to previous")
    public static void closeCurrentWindow() {
        logger.info("Closing current window");
        try {
            Set<String> windowsBefore = getWebDriver().getWindowHandles();
            String currentWindow = getWebDriver().getWindowHandle();
            
            if (windowsBefore.size() <= 1) {
                logger.warn("Only one window open, cannot close current window");
                return;
            }
            
            getWebDriver().close();
            
            // Switch to remaining window
            Set<String> windowsAfter = getWebDriver().getWindowHandles();
            for (String window : windowsAfter) {
                if (!window.equals(currentWindow)) {
                    getWebDriver().switchTo().window(window);
                    break;
                }
            }
            
            logger.debug("Successfully closed window and switched to remaining window");
        } catch (Exception e) {
            logger.error("Failed to close current window: {}", e.getMessage());
            throw new RuntimeException("Failed to close current window", e);
        }
    }

    /**
     * Close all windows except the main window
     */
    @Step("Close all windows except main window")
    public static void closeAllExceptMainWindow() {
        logger.info("Closing all windows except main window");
        try {
            Set<String> allWindows = getWebDriver().getWindowHandles();
            String mainWindow = allWindows.iterator().next(); // First window is usually main
            
            for (String window : allWindows) {
                if (!window.equals(mainWindow)) {
                    getWebDriver().switchTo().window(window);
                    getWebDriver().close();
                }
            }
            
            // Switch back to main window
            getWebDriver().switchTo().window(mainWindow);
            logger.debug("Successfully closed all windows except main window");
        } catch (Exception e) {
            logger.error("Failed to close all windows except main: {}", e.getMessage());
            throw new RuntimeException("Failed to close all windows except main", e);
        }
    }

    /**
     * Get current window handle
     * @return Current window handle
     */
    @Step("Get current window handle")
    public static String getCurrentWindowHandle() {
        logger.info("Getting current window handle");
        try {
            String handle = getWebDriver().getWindowHandle();
            logger.debug("Current window handle: {}", handle);
            return handle;
        } catch (Exception e) {
            logger.error("Failed to get current window handle: {}", e.getMessage());
            throw new RuntimeException("Failed to get current window handle", e);
        }
    }

    /**
     * Get all window handles
     * @return Set of all window handles
     */
    @Step("Get all window handles")
    public static Set<String> getAllWindowHandles() {
        logger.info("Getting all window handles");
        try {
            Set<String> handles = getWebDriver().getWindowHandles();
            logger.debug("Found {} window handles", handles.size());
            return handles;
        } catch (Exception e) {
            logger.error("Failed to get all window handles: {}", e.getMessage());
            throw new RuntimeException("Failed to get all window handles", e);
        }
    }

    /**
     * Get number of open windows
     * @return Number of open windows
     */
    @Step("Get number of open windows")
    public static int getWindowCount() {
        logger.info("Getting number of open windows");
        try {
            int count = getWebDriver().getWindowHandles().size();
            logger.debug("Number of open windows: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get window count: {}", e.getMessage());
            throw new RuntimeException("Failed to get window count", e);
        }
    }

    /**
     * Get current window title
     * @return Current window title
     */
    @Step("Get current window title")
    public static String getCurrentWindowTitle() {
        logger.info("Getting current window title");
        try {
            String title = getWebDriver().getTitle();
            logger.debug("Current window title: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to get current window title: {}", e.getMessage());
            throw new RuntimeException("Failed to get current window title", e);
        }
    }

    /**
     * Get current window URL
     * @return Current window URL
     */
    @Step("Get current window URL")
    public static String getCurrentWindowUrl() {
        logger.info("Getting current window URL");
        try {
            String url = getWebDriver().getCurrentUrl();
            logger.debug("Current window URL: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("Failed to get current window URL: {}", e.getMessage());
            throw new RuntimeException("Failed to get current window URL", e);
        }
    }

    /**
     * Open new tab
     */
    @Step("Open new tab")
    public static void openNewTab() {
        logger.info("Opening new tab");
        try {
            Selenide.executeJavaScript("window.open('about:blank','_blank');");
            logger.debug("Successfully opened new tab");
        } catch (Exception e) {
            logger.error("Failed to open new tab: {}", e.getMessage());
            throw new RuntimeException("Failed to open new tab", e);
        }
    }

    /**
     * Open URL in new tab
     * @param url URL to open in new tab
     */
    @Step("Open URL in new tab: {url}")
    public static void openUrlInNewTab(String url) {
        logger.info("Opening URL in new tab: {}", url);
        try {
            Selenide.executeJavaScript("window.open(arguments[0],'_blank');", url);
            logger.debug("Successfully opened URL in new tab: {}", url);
        } catch (Exception e) {
            logger.error("Failed to open URL in new tab: {}", e.getMessage());
            throw new RuntimeException("Failed to open URL in new tab", e);
        }
    }

    /**
     * Get WebDriver instance
     * @return WebDriver instance
     */
    private static WebDriver getWebDriver() {
        return WebDriverRunner.getWebDriver();
    }
}