package core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager handles configuration properties from config.properties file
 * and environment variables. Environment variables take precedence over properties file.
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    private static ConfigManager instance;

    private ConfigManager() {
        loadProperties();
    }

    /**
     * Get ConfigManager singleton instance
     * @return ConfigManager instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration loaded successfully from {}", CONFIG_FILE);
            } else {
                logger.warn("Configuration file {} not found in classpath", CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Error loading configuration file {}: {}", CONFIG_FILE, e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    /**
     * Get property value with environment variable override
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        // Environment variables take precedence
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null) {
            logger.debug("Using environment variable {} = {}", key.toUpperCase().replace(".", "_"), envValue);
            return envValue;
        }
        
        // System properties take second precedence
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            logger.debug("Using system property {} = {}", key, systemValue);
            return systemValue;
        }
        
        // Fall back to properties file
        String value = properties.getProperty(key);
        if (value != null) {
            logger.debug("Using config property {} = {}", key, value);
        }
        return value;
    }

    /**
     * Get property value with default fallback
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get boolean property value
     * @param key Property key
     * @return Boolean value or false if not found/invalid
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }

    /**
     * Get boolean property value with default
     * @param key Property key
     * @param defaultValue Default boolean value
     * @return Boolean value or default
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Get integer property value
     * @param key Property key
     * @return Integer value or 0 if not found/invalid
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : 0;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for property {}: {}", key, value);
            return 0;
        }
    }

    /**
     * Get integer property value with default
     * @param key Property key
     * @param defaultValue Default integer value
     * @return Integer value or default
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for property {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Get long property value
     * @param key Property key
     * @return Long value or 0 if not found/invalid
     */
    public long getLongProperty(String key) {
        String value = getProperty(key);
        try {
            return value != null ? Long.parseLong(value) : 0L;
        } catch (NumberFormatException e) {
            logger.warn("Invalid long value for property {}: {}", key, value);
            return 0L;
        }
    }

    /**
     * Get long property value with default
     * @param key Property key
     * @param defaultValue Default long value
     * @return Long value or default
     */
    public long getLongProperty(String key, long defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Long.parseLong(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid long value for property {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    // Browser Configuration
    public String getBrowser() { return getProperty("browser", "chrome"); }
    public boolean isHeadless() { return getBooleanProperty("headless", false); }
    public boolean shouldMaximizeBrowser() { return getBooleanProperty("browser.maximize", true); }

    // Timeout Configuration
    public long getTimeout() { return getLongProperty("timeout", 10000); }
    public long getPageLoadTimeout() { return getLongProperty("page.load.timeout", 30000); }
    public long getScriptTimeout() { return getLongProperty("script.timeout", 30000); }
    public int getImplicitWait() { return getIntProperty("implicit.wait", 5); }

    // Remote Configuration
    public boolean isRemoteExecution() { return getBooleanProperty("remote.execution", false); }
    public String getRemoteUrl() { return getProperty("remote.url", "http://localhost:4444/wd/hub"); }

    // Application Configuration
    public String getBaseUrl() { return getProperty("base.url", "https://example.com"); }

    // Screenshot Configuration
    public boolean isScreenshotOnFailure() { return getBooleanProperty("screenshot.on.failure", true); }
    public boolean isScreenshotOnSuccess() { return getBooleanProperty("screenshot.on.success", false); }

    // Logging Configuration
    public String getLogLevel() { return getProperty("log.level", "INFO"); }

    // Allure Configuration
    public String getAllureResultsDirectory() { return getProperty("allure.results.directory", "target/allure-results"); }

    // Test Data Configuration
    public String getTestDataPath() { return getProperty("test.data.path", "src/test/resources/testdata"); }

    // Downloads Configuration
    public String getDownloadPath() { return getProperty("download.path", "target/downloads"); }
}