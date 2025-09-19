package tests;

import core.constants.ApplicationConstants;
import core.driver.BrowserManager;
import core.reporting.ScreenshotHandler;
import core.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * BaseTest provides common setup and teardown for all test classes
 */
public abstract class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Suite setup - runs once before all tests in the suite
     */
    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        LogUtils.logSection("SUITE SETUP");
        logger.info("Starting test suite execution");
        
        // Log environment information
        LogUtils.logEnvironmentInfo("Java Version", System.getProperty("java.version"));
        LogUtils.logEnvironmentInfo("OS", System.getProperty("os.name"));
        LogUtils.logEnvironmentInfo("OS Version", System.getProperty("os.version"));
        LogUtils.logEnvironmentInfo("User", System.getProperty("user.name"));
        LogUtils.logEnvironmentInfo("Browser", ApplicationConstants.Browser.BROWSER_TYPE);
        LogUtils.logEnvironmentInfo("Headless", String.valueOf(ApplicationConstants.Browser.IS_HEADLESS));
        LogUtils.logEnvironmentInfo("Base URL", ApplicationConstants.URLs.BASE_URL);
        
        // Clean up old screenshots
        ScreenshotHandler.cleanupOldScreenshots(7);
        
        logger.info("Suite setup completed");
    }

    /**
     * Test setup - runs before each test method
     */
    @BeforeMethod(alwaysRun = true)
    public void testSetup(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = this.getClass().getSimpleName();
        
        LogUtils.logTestSetup(className, testName);
        logger.info("Setting up test: {}.{}", className, testName);
        
        // Configure and open browser
        try {
            BrowserManager.configure();
            BrowserManager.openBrowser();
            logger.info("Browser opened successfully for test: {}", testName);
        } catch (Exception e) {
            logger.error("Failed to open browser for test {}: {}", testName, e.getMessage(), e);
            throw new RuntimeException("Browser setup failed", e);
        }
    }

    /**
     * Test teardown - runs after each test method
     */
    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = this.getClass().getSimpleName();
        
        logger.info("Tearing down test: {}.{}", className, testName);
        
        // Take screenshot based on test result
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                if (ApplicationConstants.TestConfig.SCREENSHOT_ON_FAILURE) {
                    ScreenshotHandler.takeFailureScreenshot(testName, result.getThrowable());
                }
                LogUtils.logTestResult(testName, "FAILED", 0);
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                if (ApplicationConstants.TestConfig.SCREENSHOT_ON_SUCCESS) {
                    ScreenshotHandler.takeScreenshot(testName, "success");
                }
                LogUtils.logTestResult(testName, "PASSED", 0);
            } else if (result.getStatus() == ITestResult.SKIP) {
                LogUtils.logTestResult(testName, "SKIPPED", 0);
            }
        } catch (Exception e) {
            logger.warn("Error during screenshot capture: {}", e.getMessage());
        }
        
        // Close browser
        try {
            BrowserManager.quitBrowser();
            logger.info("Browser closed successfully for test: {}", testName);
        } catch (Exception e) {
            logger.warn("Error closing browser for test {}: {}", testName, e.getMessage());
        }
        
        LogUtils.logTestTeardown();
    }

    /**
     * Class setup - runs before all test methods in the class
     */
    @BeforeClass(alwaysRun = true)
    public void classSetup() {
        String className = this.getClass().getSimpleName();
        LogUtils.logSection("CLASS SETUP: " + className);
        logger.info("Setting up test class: {}", className);
    }

    /**
     * Class teardown - runs after all test methods in the class
     */
    @AfterClass(alwaysRun = true)
    public void classTeardown() {
        String className = this.getClass().getSimpleName();
        logger.info("Tearing down test class: {}", className);
        LogUtils.logSection("CLASS TEARDOWN: " + className);
    }

    /**
     * Suite teardown - runs once after all tests in the suite
     */
    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        LogUtils.logSection("SUITE TEARDOWN");
        logger.info("Test suite execution completed");
        
        // Perform any final cleanup
        try {
            // Force close any remaining browser instances
            BrowserManager.quitBrowser();
        } catch (Exception e) {
            logger.warn("Error during final browser cleanup: {}", e.getMessage());
        }
        
        logger.info("Suite teardown completed");
    }

    /**
     * Get test parameter from TestNG context
     * @param parameterName Parameter name
     * @param defaultValue Default value if parameter not found
     * @return Parameter value
     */
    protected String getTestParameter(String parameterName, String defaultValue) {
        // This would be implemented with TestNG's ITestContext in real scenarios
        // For now, return config value or default
        String value = ApplicationConstants.getProperty(parameterName, defaultValue);
        LogUtils.logTestData(parameterName, value);
        return value;
    }

    /**
     * Navigate to application base URL
     */
    protected void navigateToApplication() {
        String baseUrl = ApplicationConstants.URLs.BASE_URL;
        LogUtils.logBrowserAction("Navigate to application", baseUrl);
        BrowserManager.openBrowser();
    }

    /**
     * Take screenshot during test execution
     * @param stepName Step name for screenshot
     */
    protected void takeScreenshot(String stepName) {
        if (ScreenshotHandler.isScreenshotEnabled()) {
            ScreenshotHandler.takeStepScreenshot(stepName);
        }
    }

    /**
     * Log test step
     * @param stepDescription Step description
     */
    protected void logStep(String stepDescription) {
        LogUtils.logTestStep(stepDescription);
    }

    /**
     * Log test data
     * @param dataName Data name
     * @param dataValue Data value
     */
    protected void logTestData(String dataName, Object dataValue) {
        LogUtils.logTestData(dataName, dataValue);
    }

    /**
     * Log assertion result
     * @param assertion Assertion description
     * @param result Assertion result
     */
    protected void logAssertion(String assertion, boolean result) {
        LogUtils.logAssertion(assertion, result);
    }

    /**
     * Sleep for specified duration (use sparingly, prefer waits)
     * @param milliseconds Duration in milliseconds
     */
    protected void sleep(long milliseconds) {
        LogUtils.logWarning("Using sleep for " + milliseconds + "ms - consider using proper waits");
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }
}