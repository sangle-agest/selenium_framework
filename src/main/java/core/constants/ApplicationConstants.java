package core.constants;

import core.config.ConfigManager;

/**
 * ApplicationConstants provides centralized access to all application constants
 * including URLs, timeouts, and other configuration values.
 * This class acts as a bridge between ConfigManager and the application code,
 * providing easy access to commonly used configuration values.
 */
public final class ApplicationConstants {
    
    private static final ConfigManager config = ConfigManager.getInstance();
    
    /**
     * Get property value with fallback to default
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default
     */
    public static String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }
    
    // Private constructor to prevent instantiation
    private ApplicationConstants() {
        throw new UnsupportedOperationException("ApplicationConstants is a utility class and cannot be instantiated");
    }
    
    /**
     * Application URLs
     */
    public static final class URLs {
        private URLs() {} // Prevent instantiation
        
        public static final String BASE_URL = config.getBaseUrl();
        public static final String AGODA_URL = config.getAgodaUrl();
    }
    
    /**
     * Timeout Constants (in milliseconds)
     */
    public static final class Timeouts {
        private Timeouts() {} // Prevent instantiation
        
        public static final long DEFAULT_TIMEOUT = config.getTimeout();
        public static final long PAGE_LOAD_TIMEOUT = config.getPageLoadTimeout();
        public static final long SCRIPT_TIMEOUT = config.getScriptTimeout();
        public static final int IMPLICIT_WAIT = config.getImplicitWait();
    }
    
    /**
     * Browser Configuration Constants
     */
    public static final class Browser {
        private Browser() {} // Prevent instantiation
        
        public static final String BROWSER_TYPE = config.getBrowser();
        public static final boolean IS_HEADLESS = config.isHeadless();
        public static final boolean SHOULD_MAXIMIZE = config.shouldMaximizeBrowser();
    }
    
    /**
     * Test Configuration Constants
     */
    public static final class TestConfig {
        private TestConfig() {} // Prevent instantiation
        
        public static final boolean SCREENSHOT_ON_FAILURE = config.isScreenshotOnFailure();
        public static final boolean SCREENSHOT_ON_SUCCESS = config.isScreenshotOnSuccess();
        public static final String TEST_DATA_PATH = config.getTestDataPath();
        public static final String DOWNLOAD_PATH = config.getDownloadPath();
        public static final String ALLURE_RESULTS_DIR = config.getAllureResultsDirectory();
    }
    
    /**
     * Remote Execution Constants
     */
    public static final class Remote {
        private Remote() {} // Prevent instantiation
        
        public static final boolean IS_REMOTE_EXECUTION = config.isRemoteExecution();
        public static final String REMOTE_URL = config.getRemoteUrl();
    }
}