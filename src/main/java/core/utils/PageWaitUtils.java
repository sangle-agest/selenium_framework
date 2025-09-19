package core.utils;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PageWaitUtils provides common reusable wait methods for page interactions
 * These methods encapsulate common patterns used across different page objects
 */
public class PageWaitUtils {
    private static final Logger logger = LoggerFactory.getLogger(PageWaitUtils.class);
    
    // Default timeout for most operations
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int COUNTER_TIMEOUT_SECONDS = 3;

    /**
     * Wait for a popup or modal to become visible using a visibility check method
     * @param visibilityChecker Method that returns true when popup is visible
     * @param timeoutSeconds Timeout in seconds
     * @param popupName Name of the popup for logging
     * @return True if popup became visible
     */
    @Step("Wait for {popupName} popup to become visible")
    public static boolean waitForPopupVisible(java.util.function.BooleanSupplier visibilityChecker, 
                                            int timeoutSeconds, String popupName) {
        logger.debug("Waiting for {} popup to become visible", popupName);
        boolean isVisible = WaitUtils.waitForCondition(visibilityChecker, timeoutSeconds);
        
        if (isVisible) {
            LogUtils.logVerificationStep("✓ " + popupName + " popup is now visible");
        } else {
            LogUtils.logTestStep("ERROR: " + popupName + " popup did not become visible after waiting");
        }
        
        return isVisible;
    }

    /**
     * Wait for a popup to become visible with default timeout
     * @param visibilityChecker Method that returns true when popup is visible
     * @param popupName Name of the popup for logging
     * @return True if popup became visible
     */
    @Step("Wait for {popupName} popup to become visible")
    public static boolean waitForPopupVisible(java.util.function.BooleanSupplier visibilityChecker, String popupName) {
        return waitForPopupVisible(visibilityChecker, DEFAULT_TIMEOUT_SECONDS, popupName);
    }

    /**
     * Smart counter update - waits for counter element to reach target value
     * @param counterElement The counter element to monitor
     * @param currentValue Current counter value
     * @param targetValue Target counter value
     * @param incrementButton Button to click for incrementing
     * @param decrementButton Button to click for decrementing
     * @param counterName Name of the counter for logging
     * @return Final counter value achieved
     */
    @Step("Update {counterName} from {currentValue} to {targetValue}")
    public static int updateCounterSmart(SelenideElement counterElement, int currentValue, int targetValue,
                                       core.elements.Button incrementButton, core.elements.Button decrementButton,
                                       String counterName) {
        logger.debug("Updating {} counter from {} to {}", counterName, currentValue, targetValue);
        
        int current = currentValue;
        
        // Increment counter
        while (current < targetValue) {
            incrementButton.click();
            final int expectedValue = current + 1;
            
            boolean updated = WaitUtils.waitForCounterValue(counterElement, expectedValue, COUNTER_TIMEOUT_SECONDS);
            
            if (updated) {
                current = expectedValue;
                LogUtils.logVerificationStep("✓ " + counterName + " incremented to: " + current);
            } else {
                LogUtils.logTestStep("Warning: " + counterName + " count did not update as expected");
                break;
            }
        }
        
        // Decrement counter
        while (current > targetValue) {
            decrementButton.click();
            final int expectedValue = current - 1;
            
            boolean updated = WaitUtils.waitForCounterValue(counterElement, expectedValue, COUNTER_TIMEOUT_SECONDS);
            
            if (updated) {
                current = expectedValue;
                LogUtils.logVerificationStep("✓ " + counterName + " decremented to: " + current);
            } else {
                LogUtils.logTestStep("Warning: " + counterName + " count did not update as expected");
                break;
            }
        }
        
        LogUtils.logTestStep("Final " + counterName + " value: " + current + " (target was: " + targetValue + ")");
        return current;
    }

    /**
     * Wait for date selection to complete using JavaScript DOM check
     * @param dateString Date in YYYY-MM-DD format
     * @param containerXPath XPath of the date picker container
     * @return True if date was successfully selected
     */
    @Step("Wait for date selection: {dateString}")
    public static boolean waitForDateSelection(String dateString, String containerXPath) {
        logger.debug("Waiting for date selection: {}", dateString);
        
        // Execute JavaScript to click the date
        String jsScript = "document.evaluate(\"" + containerXPath + "//span[@data-selenium-date='" + dateString + "']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue?.click();";
        BrowserUtils.executeJavaScript(jsScript);
        
        // Wait for the date selection to take effect
        String verificationScript = "return document.evaluate(\"" + containerXPath + "//span[@data-selenium-date='" + dateString + "']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue !== null;";
        boolean dateSelected = WaitUtils.waitForJavaScriptCondition(verificationScript, 3, "Date selection for " + dateString);
        
        if (dateSelected) {
            LogUtils.logVerificationStep("✓ Successfully selected date: " + dateString);
        } else {
            LogUtils.logTestStep("⚠ Date element not found for: " + dateString);
        }
        
        return dateSelected;
    }

    /**
     * Wait for element to be ready for interaction (visible and enabled)
     * @param element Element to wait for
     * @param elementName Name for logging
     * @param timeoutSeconds Timeout in seconds
     * @return True if element is ready
     */
    @Step("Wait for {elementName} to be ready for interaction")
    public static boolean waitForElementReady(SelenideElement element, String elementName, int timeoutSeconds) {
        logger.debug("Waiting for {} to be ready for interaction", elementName);
        
        boolean isReady = WaitUtils.waitForElementInteractable(element, timeoutSeconds);
        
        if (isReady) {
            LogUtils.logVerificationStep("✓ " + elementName + " is ready for interaction");
        } else {
            LogUtils.logTestStep("⚠ " + elementName + " is not ready for interaction");
        }
        
        return isReady;
    }

    /**
     * Wait for element to be ready with default timeout
     * @param element Element to wait for
     * @param elementName Name for logging
     * @return True if element is ready
     */
    @Step("Wait for {elementName} to be ready for interaction")
    public static boolean waitForElementReady(SelenideElement element, String elementName) {
        return waitForElementReady(element, elementName, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Smart wait for autocomplete selection - waits for first option and clicks it
     * @param firstOption First autocomplete option element
     * @param searchTerm The search term that was entered
     * @return True if autocomplete option was selected
     */
    @Step("Wait for autocomplete selection: {searchTerm}")
    public static boolean waitForAutocompleteAndSelect(SelenideElement firstOption, String searchTerm) {
        logger.debug("Waiting for autocomplete options for: {}", searchTerm);
        
        // Wait for autocomplete option to be visible and clickable
        boolean optionReady = waitForElementReady(firstOption, "First autocomplete option", DEFAULT_TIMEOUT_SECONDS);
        
        if (optionReady) {
            firstOption.click();
            LogUtils.logVerificationStep("✓ Selected first autocomplete option for: " + searchTerm);
            return true;
        } else {
            LogUtils.logTestStep("⚠ Autocomplete options not available for: " + searchTerm);
            return false;
        }
    }
}