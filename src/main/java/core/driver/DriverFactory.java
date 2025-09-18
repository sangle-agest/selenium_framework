package core.driver;

import core.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverFactory creates and configures WebDriver instances
 * Supports local and remote execution for Chrome, Firefox, and Edge browsers
 */
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Create WebDriver instance based on configuration
     * @return Configured WebDriver instance
     */
    public static WebDriver createDriver() {
        String browser = config.getBrowser().toLowerCase();
        boolean isRemote = config.isRemoteExecution();
        
        logger.info("Creating {} driver for {} execution", browser, isRemote ? "remote" : "local");
        
        WebDriver driver;
        if (isRemote) {
            driver = createRemoteDriver(browser);
        } else {
            driver = createLocalDriver(browser);
        }
        
        configureDriver(driver);
        return driver;
    }

    /**
     * Create local WebDriver instance
     * @param browser Browser name (chrome, firefox, edge)
     * @return WebDriver instance
     */
    private static WebDriver createLocalDriver(String browser) {
        return switch (browser) {
            case "chrome" -> new ChromeDriver(getChromeOptions());
            case "firefox" -> new FirefoxDriver(getFirefoxOptions());
            case "edge" -> new EdgeDriver(getEdgeOptions());
            default -> {
                logger.warn("Unsupported browser: {}. Defaulting to Chrome", browser);
                yield new ChromeDriver(getChromeOptions());
            }
        };
    }

    /**
     * Create remote WebDriver instance
     * @param browser Browser name (chrome, firefox, edge)
     * @return RemoteWebDriver instance
     */
    private static WebDriver createRemoteDriver(String browser) {
        try {
            URL remoteUrl = new URL(config.getRemoteUrl());
            return switch (browser) {
                case "chrome" -> new RemoteWebDriver(remoteUrl, getChromeOptions());
                case "firefox" -> new RemoteWebDriver(remoteUrl, getFirefoxOptions());
                case "edge" -> new RemoteWebDriver(remoteUrl, getEdgeOptions());
                default -> {
                    logger.warn("Unsupported browser: {}. Defaulting to Chrome", browser);
                    yield new RemoteWebDriver(remoteUrl, getChromeOptions());
                }
            };
        } catch (MalformedURLException e) {
            logger.error("Invalid remote URL: {}", config.getRemoteUrl(), e);
            throw new RuntimeException("Failed to create remote driver", e);
        }
    }

    /**
     * Get Chrome browser options
     * @return ChromeOptions configured based on settings
     */
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        
        // Performance and stability options
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--disable-extensions",
            "--disable-infobars",
            "--disable-notifications",
            "--disable-popup-blocking",
            "--remote-allow-origins=*"
        );
        
        if (config.shouldMaximizeBrowser()) {
            options.addArguments("--start-maximized");
        }
        
        // Download preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", config.getDownloadPath());
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);
        options.setExperimentalOption("prefs", prefs);
        
        logger.debug("Chrome options configured: headless={}, maximize={}", 
                    config.isHeadless(), config.shouldMaximizeBrowser());
        
        return options;
    }

    /**
     * Get Firefox browser options
     * @return FirefoxOptions configured based on settings
     */
    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        // Performance options
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        
        // Download preferences
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", config.getDownloadPath());
        options.addPreference("browser.helperApps.neverAsk.saveToDisk", 
                              "application/pdf,application/zip,text/csv,application/vnd.ms-excel");
        
        logger.debug("Firefox options configured: headless={}", config.isHeadless());
        
        return options;
    }

    /**
     * Get Edge browser options
     * @return EdgeOptions configured based on settings
     */
    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        
        // Performance and stability options
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--disable-extensions",
            "--disable-infobars",
            "--remote-allow-origins=*"
        );
        
        if (config.shouldMaximizeBrowser()) {
            options.addArguments("--start-maximized");
        }
        
        // Download preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", config.getDownloadPath());
        prefs.put("download.prompt_for_download", false);
        options.setExperimentalOption("prefs", prefs);
        
        logger.debug("Edge options configured: headless={}, maximize={}", 
                    config.isHeadless(), config.shouldMaximizeBrowser());
        
        return options;
    }

    /**
     * Configure WebDriver with timeouts and other settings
     * @param driver WebDriver instance to configure
     */
    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(config.getImplicitWait()))
            .pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout() / 1000))
            .scriptTimeout(Duration.ofSeconds(config.getScriptTimeout() / 1000));
        
        if (config.shouldMaximizeBrowser() && !config.isHeadless()) {
            driver.manage().window().maximize();
        }
        
        logger.info("Driver configured with timeouts - implicit: {}s, pageLoad: {}ms, script: {}ms",
                   config.getImplicitWait(), 
                   config.getPageLoadTimeout(), 
                   config.getScriptTimeout());
    }
}