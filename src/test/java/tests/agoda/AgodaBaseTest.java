package tests.agoda;

import core.constants.ApplicationConstants;
import core.driver.BrowserManager;
import core.reporting.ScreenshotHandler;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Base test class specifically for Agoda test automation
 * Contains Agoda-specific setup, teardown, and common functionality
 */
public class AgodaBaseTest {
    
    @BeforeSuite(alwaysRun = true)
    @Step("Setting up Agoda test suite")
    public void suiteSetup() {
        LogUtils.logSection("SUITE SETUP");
        LogUtils.logInfo("Starting Agoda test suite execution");
        
        // Log environment information
        LogUtils.logEnvironmentInfo("Java Version", System.getProperty("java.version"));
        LogUtils.logEnvironmentInfo("OS", System.getProperty("os.name"));
        LogUtils.logEnvironmentInfo("OS Version", System.getProperty("os.version"));
        LogUtils.logEnvironmentInfo("User", System.getProperty("user.name"));
        LogUtils.logEnvironmentInfo("Browser", ApplicationConstants.Browser.BROWSER_TYPE);
        LogUtils.logEnvironmentInfo("Headless", String.valueOf(ApplicationConstants.Browser.IS_HEADLESS));
        LogUtils.logEnvironmentInfo("Base URL", ApplicationConstants.URLs.BASE_URL);
        
        // Initialize screenshot handler
        ScreenshotHandler.cleanupOldScreenshots(7);
        
        LogUtils.logInfo("Agoda suite setup completed");
    }
    
    @BeforeClass(alwaysRun = true)
    @Step("Setting up Agoda test class")
    public void classSetup() {
        LogUtils.logSection("CLASS SETUP: " + this.getClass().getSimpleName());
        LogUtils.logInfo("Setting up test class: " + this.getClass().getSimpleName());
    }
    
    @BeforeMethod(alwaysRun = true)
    @Step("Setting up Agoda test")
    public void testSetup(java.lang.reflect.Method method) {
        LogUtils.logTestSetup(this.getClass().getSimpleName(), method.getName());
        LogUtils.logInfo("Setting up test: " + this.getClass().getSimpleName() + "." + method.getName());
        
        try {
            // Configure and open browser for Agoda
            BrowserManager.configure();
            BrowserManager.openBrowserWithoutUrl();
            LogUtils.logInfo("Browser opened successfully for test: " + method.getName());
            
            // Navigate directly to Agoda homepage
            navigateToAgoda();
            
        } catch (Exception e) {
            LogUtils.logError("Failed to open browser for test " + method.getName() + ": " + e.getMessage(), e);
            throw new RuntimeException("Browser setup failed", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    @Step("Tearing down Agoda test")
    public void testTeardown(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        LogUtils.logInfo("Tearing down test: " + this.getClass().getSimpleName() + "." + methodName);
        
        try {
            // Take screenshot on failure
            if (result.getStatus() == ITestResult.FAILURE) {
                ScreenshotHandler.takeFailureScreenshot(methodName, result.getThrowable());
            }
            
            // Close browser
            BrowserManager.closeBrowser();
            LogUtils.logInfo("Browser closed successfully for test: " + methodName);
            
        } catch (Exception e) {
            LogUtils.logError("Error during test teardown: " + e.getMessage(), e);
        } finally {
            LogUtils.logTestTeardown();
        }
    }
    
    @AfterClass(alwaysRun = true)
    @Step("Tearing down Agoda test class")
    public void classTeardown() {
        LogUtils.logInfo("Tearing down test class: " + this.getClass().getSimpleName());
        LogUtils.logSection("CLASS TEARDOWN: " + this.getClass().getSimpleName());
    }
    
    @AfterSuite(alwaysRun = true)
    @Step("Tearing down Agoda test suite")
    public void suiteTeardown() {
        LogUtils.logSection("SUITE TEARDOWN");
        LogUtils.logInfo("Agoda test suite execution completed");
        
        // Final cleanup - use quitBrowser instead of closeBrowser for suite teardown
        // This will only try to quit if the browser is still active
        BrowserManager.quitBrowser();
        
        LogUtils.logInfo("Agoda suite teardown completed");
    }
    
    /**
     * Navigate to Agoda homepage using Selenide
     */
    @Step("Navigating to Agoda homepage")
    protected void navigateToAgoda() {
        LogUtils.logPageAction("Navigating to Agoda: " + ApplicationConstants.URLs.AGODA_URL);
        com.codeborne.selenide.Selenide.open(ApplicationConstants.URLs.AGODA_URL);
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
            LogUtils.logError("Interrupted while waiting for Agoda page to load", e);
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Get the Agoda base URL
     */
    protected String getAgodaBaseUrl() {
        return ApplicationConstants.URLs.AGODA_URL;
    }
}