package core.driver;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import core.config.ConfigManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * BrowserManager configures Selenide settings and manages browser lifecycle
 */
public class BrowserManager {
    private static final Logger logger = LoggerFactory.getLogger(BrowserManager.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private static boolean isConfigured = false;

    /**
     * Configure Selenide settings based on configuration
     */
    public static synchronized void configure() {
        if (isConfigured) {
            logger.debug("Selenide already configured, skipping configuration");
            return;
        }

        logger.info("Configuring Selenide settings");

        // Basic browser configuration
        Configuration.browser = config.getBrowser();
        Configuration.headless = config.isHeadless();
        Configuration.browserSize = config.shouldMaximizeBrowser() ? "1920x1080" : "1366x768";

        // Timeout configuration
        Configuration.timeout = config.getTimeout();
        Configuration.pageLoadTimeout = config.getPageLoadTimeout();

        // Remote execution configuration
        if (config.isRemoteExecution()) {
            Configuration.remote = config.getRemoteUrl();
            logger.info("Configured for remote execution at: {}", config.getRemoteUrl());
        }

        // Screenshot configuration
        Configuration.screenshots = config.isScreenshotOnFailure();
        Configuration.savePageSource = config.isScreenshotOnFailure();
        
        // Reports directory
        Configuration.reportsFolder = "target/screenshots";
        
        // Downloads directory
        String downloadPath = config.getDownloadPath();
        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
            logger.info("Created download directory: {}", downloadPath);
        }
        Configuration.downloadsFolder = downloadPath;

        // Fast set value for better performance
        Configuration.fastSetValue = true;
        
        // Click via JavaScript for better compatibility
        Configuration.clickViaJs = false;

        // Add Allure Selenide listener
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
            .screenshots(true)
            .savePageSource(false)
            .includeSelenideSteps(true));

        isConfigured = true;
        
        logger.info("Selenide configured successfully - Browser: {}, Headless: {}, Remote: {}, Timeout: {}ms",
                   Configuration.browser,
                   Configuration.headless,
                   config.isRemoteExecution(),
                   Configuration.timeout);
    }

    /**
     * Open browser and navigate to base URL
     */
    public static void openBrowser() {
        configure();
        
        String baseUrl = config.getBaseUrl();
        logger.info("Opening browser and navigating to: {}", baseUrl);
        
        try {
            Selenide.open(baseUrl);
            logger.info("Browser opened successfully");
        } catch (Exception e) {
            logger.error("Failed to open browser: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to open browser", e);
        }
    }

    /**
     * Open browser without navigating to any URL
     */
    public static void openBrowserWithoutUrl() {
        configure();
        logger.info("Opening browser without URL");
        
        try {
            Selenide.open();
            logger.info("Browser opened successfully without URL");
        } catch (Exception e) {
            logger.error("Failed to open browser: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to open browser", e);
        }
    }

    /**
     * Close current browser window
     */
    public static void closeBrowser() {
        try {
            // Check if WebDriver is still active before attempting to close
            if (WebDriverRunner.hasWebDriverStarted() && WebDriverRunner.getWebDriver() != null) {
                // Check if the current window is still available
                try {
                    WebDriverRunner.getWebDriver().getWindowHandle();
                    Selenide.closeWindow();
                    logger.info("Browser window closed");
                } catch (org.openqa.selenium.NoSuchWindowException e) {
                    logger.debug("Browser window was already closed");
                }
            } else {
                logger.debug("WebDriver not active, no browser to close");
            }
        } catch (Exception e) {
            // Only log as debug to avoid cluttering logs with expected cleanup warnings
            logger.debug("Error closing browser window (this is usually harmless during teardown): {}", e.getMessage());
        }
    }

    /**
     * Close all browser windows and quit WebDriver
     */
    public static void quitBrowser() {
        try {
            // Check if WebDriver is still active before attempting to quit
            if (WebDriverRunner.hasWebDriverStarted() && WebDriverRunner.getWebDriver() != null) {
                Selenide.closeWebDriver();
                logger.info("All browser windows closed and WebDriver quit");
            } else {
                logger.debug("WebDriver not active, no browser to quit");
            }
        } catch (Exception e) {
            // Only log as debug to avoid cluttering logs with expected cleanup warnings
            logger.debug("Error quitting browser (this is usually harmless during teardown): {}", e.getMessage());
        }
    }

    /**
     * Refresh current page
     */
    public static void refreshPage() {
        try {
            Selenide.refresh();
            logger.info("Page refreshed");
        } catch (Exception e) {
            logger.error("Failed to refresh page: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to refresh page", e);
        }
    }

    /**
     * Navigate back in browser history
     */
    public static void navigateBack() {
        try {
            Selenide.back();
            logger.info("Navigated back");
        } catch (Exception e) {
            logger.error("Failed to navigate back: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to navigate back", e);
        }
    }

    /**
     * Navigate forward in browser history
     */
    public static void navigateForward() {
        try {
            Selenide.forward();
            logger.info("Navigated forward");
        } catch (Exception e) {
            logger.error("Failed to navigate forward: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to navigate forward", e);
        }
    }

    /**
     * Get current page URL
     * @return Current URL
     */
    public static String getCurrentUrl() {
        try {
            String url = Selenide.webdriver().driver().url();
            logger.debug("Current URL: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("Failed to get current URL: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get current URL", e);
        }
    }

    /**
     * Get current page title
     * @return Page title
     */
    public static String getPageTitle() {
        try {
            String title = Selenide.title();
            logger.debug("Page title: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to get page title: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get page title", e);
        }
    }

    /**
     * Clear all cookies
     */
    public static void clearCookies() {
        try {
            Selenide.clearBrowserCookies();
            logger.info("Browser cookies cleared");
        } catch (Exception e) {
            logger.warn("Failed to clear cookies: {}", e.getMessage());
        }
    }

    /**
     * Clear browser local storage
     */
    public static void clearLocalStorage() {
        try {
            Selenide.localStorage().clear();
            logger.info("Browser local storage cleared");
        } catch (Exception e) {
            logger.warn("Failed to clear local storage: {}", e.getMessage());
        }
    }

    /**
     * Clear browser session storage
     */
    public static void clearSessionStorage() {
        try {
            Selenide.sessionStorage().clear();
            logger.info("Browser session storage cleared");
        } catch (Exception e) {
            logger.warn("Failed to clear session storage: {}", e.getMessage());
        }
    }

    /**
     * Reset configuration flag for testing purposes
     */
    public static void resetConfiguration() {
        isConfigured = false;
        logger.debug("Configuration reset");
    }
}