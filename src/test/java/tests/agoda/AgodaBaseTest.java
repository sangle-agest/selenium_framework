package tests.agoda;

import core.config.ConfigManager;
import core.driver.BrowserManager;
import core.reporting.ScreenshotHandler;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Base test class specifically for Agoda test automation
 * Contains Agoda-specific setup, teardown, and common functionality
 */
public class AgodaBaseTest {
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final ConfigManager config = ConfigManager.getInstance();
    protected static final String AGODA_BASE_URL = "https://www.agoda.com/";
    
    @BeforeSuite(alwaysRun = true)
    @Step("Setting up Agoda test suite")
    public void suiteSetup() {
        LogUtils.logSection("SUITE SETUP");
        logger.info("Starting Agoda test suite execution");
        
        // Log environment information
        LogUtils.logEnvironmentInfo("Java Version", System.getProperty("java.version"));
        LogUtils.logEnvironmentInfo("OS", System.getProperty("os.name"));
        LogUtils.logEnvironmentInfo("OS Version", System.getProperty("os.version"));
        LogUtils.logEnvironmentInfo("User", System.getProperty("user.name"));
        LogUtils.logEnvironmentInfo("Browser", config.getBrowser());
        LogUtils.logEnvironmentInfo("Headless", String.valueOf(config.isHeadless()));
        LogUtils.logEnvironmentInfo("Base URL", config.getBaseUrl());
        
        // Initialize screenshot handler
        ScreenshotHandler.cleanupOldScreenshots(7);
        
        logger.info("Agoda suite setup completed");
    }
    
    @BeforeClass(alwaysRun = true)
    @Step("Setting up Agoda test class")
    public void classSetup() {
        LogUtils.logSection("CLASS SETUP: " + this.getClass().getSimpleName());
        logger.info("Setting up test class: {}", this.getClass().getSimpleName());
    }
    
    @BeforeMethod(alwaysRun = true)
    @Step("Setting up Agoda test")
    public void testSetup(java.lang.reflect.Method method) {
        LogUtils.logTestSetup(this.getClass().getSimpleName(), method.getName());
        logger.info("Setting up test: {}.{}", this.getClass().getSimpleName(), method.getName());
        
        try {
            // Configure and open browser for Agoda
            BrowserManager.configure();
            BrowserManager.openBrowserWithoutUrl();
            logger.info("Browser opened successfully for test: {}", method.getName());
            
            // Navigate directly to Agoda homepage
            navigateToAgoda();
            
        } catch (Exception e) {
            logger.error("Failed to open browser for test {}: {}", method.getName(), e.getMessage(), e);
            throw new RuntimeException("Browser setup failed", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    @Step("Tearing down Agoda test")
    public void testTeardown(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        logger.info("Tearing down test: {}.{}", this.getClass().getSimpleName(), methodName);
        
        try {
            // Take screenshot on failure
            if (result.getStatus() == ITestResult.FAILURE) {
                ScreenshotHandler.takeFailureScreenshot(methodName, result.getThrowable());
            }
            
            // Close browser
            BrowserManager.closeBrowser();
            logger.info("Browser closed successfully for test: {}", methodName);
            
        } catch (Exception e) {
            logger.error("Error during test teardown: {}", e.getMessage(), e);
        } finally {
            LogUtils.logTestTeardown();
        }
    }
    
    @AfterClass(alwaysRun = true)
    @Step("Tearing down Agoda test class")
    public void classTeardown() {
        logger.info("Tearing down test class: {}", this.getClass().getSimpleName());
        LogUtils.logSection("CLASS TEARDOWN: " + this.getClass().getSimpleName());
    }
    
    @AfterSuite(alwaysRun = true)
    @Step("Tearing down Agoda test suite")
    public void suiteTeardown() {
        LogUtils.logSection("SUITE TEARDOWN");
        logger.info("Agoda test suite execution completed");
        
        // Final cleanup
        BrowserManager.closeBrowser();
        
        logger.info("Agoda suite teardown completed");
    }
    
    /**
     * Navigate to Agoda homepage using Selenide
     */
    @Step("Navigating to Agoda homepage")
    protected void navigateToAgoda() {
        LogUtils.logPageAction("Navigating to Agoda: " + AGODA_BASE_URL);
        com.codeborne.selenide.Selenide.open(AGODA_BASE_URL);
        waitForAgodaPageLoad();
    }
    
    /**
     * Wait for Agoda page to load completely
     */
    @Step("Waiting for Agoda page to load")
    protected void waitForAgodaPageLoad() {
        LogUtils.logPageAction("Waiting for Agoda page to load");
        try {
            Thread.sleep(3000); // Simple wait for now
            LogUtils.logTestStep("Agoda page loaded successfully");
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for Agoda page to load", e);
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Get the Agoda base URL
     */
    protected String getAgodaBaseUrl() {
        return AGODA_BASE_URL;
    }
}