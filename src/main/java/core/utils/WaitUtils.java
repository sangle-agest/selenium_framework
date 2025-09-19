package core.utils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.BooleanSupplier;

/**
 * WaitUtils provides utility methods for various wait operations
 */
public class WaitUtils {
    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);

    /**
     * Wait for element to be visible
     * @param element Element to wait for
     * @return Element that is now visible
     */
    @Step("Wait for element to be visible")
    public static SelenideElement waitForVisible(SelenideElement element) {
        logger.debug("Waiting for element to be visible");
        try {
            element.shouldBe(Condition.visible);
            logger.debug("Element is now visible");
            return element;
        } catch (Exception e) {
            logger.error("Element did not become visible: {}", e.getMessage());
            throw new RuntimeException("Element did not become visible", e);
        }
    }

    /**
     * Wait for element to be visible with timeout
     * @param element Element to wait for
     * @param timeout Timeout duration
     * @return Element that is now visible
     */
    @Step("Wait for element to be visible with timeout")
    public static SelenideElement waitForVisible(SelenideElement element, Duration timeout) {
        logger.debug("Waiting for element to be visible with timeout: {}", timeout);
        try {
            element.shouldBe(Condition.visible, timeout);
            logger.debug("Element is now visible");
            return element;
        } catch (Exception e) {
            logger.error("Element did not become visible within {}: {}", timeout, e.getMessage());
            throw new RuntimeException("Element did not become visible within " + timeout, e);
        }
    }

    /**
     * Wait for element to be clickable
     * @param element Element to wait for
     * @return Element that is now clickable
     */
    @Step("Wait for element to be clickable")
    public static SelenideElement waitForClickable(SelenideElement element) {
        logger.debug("Waiting for element to be clickable");
        try {
            element.shouldBe(Condition.visible);
            element.shouldBe(Condition.enabled);
            logger.debug("Element is now clickable");
            return element;
        } catch (Exception e) {
            logger.error("Element did not become clickable: {}", e.getMessage());
            throw new RuntimeException("Element did not become clickable", e);
        }
    }

    /**
     * Wait for text to be present in element
     * @param element Element to wait for
     * @param text Text to wait for
     * @return Element containing the text
     */
    @Step("Wait for text '{text}' to be present in element")
    public static SelenideElement waitForText(SelenideElement element, String text) {
        logger.debug("Waiting for text '{}' to be present in element", text);
        try {
            element.shouldHave(Condition.text(text));
            logger.debug("Text '{}' is now present in element", text);
            return element;
        } catch (Exception e) {
            logger.error("Text '{}' did not appear in element: {}", text, e.getMessage());
            throw new RuntimeException("Text '" + text + "' did not appear in element", e);
        }
    }

    /**
     * Wait until condition is true
     * @param condition Condition to wait for
     * @param timeout Timeout duration
     * @param errorMessage Error message if condition fails
     */
    @Step("Wait until condition is true")
    public static void waitUntil(BooleanSupplier condition, Duration timeout, String errorMessage) {
        logger.debug("Waiting until condition is true with timeout: {}", timeout);
        long startTime = System.currentTimeMillis();
        long timeoutMs = timeout.toMillis();
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                if (condition.getAsBoolean()) {
                    logger.debug("Condition became true");
                    return;
                }
            } catch (Exception e) {
                logger.debug("Exception while checking condition: {}", e.getMessage());
            }
            
            try {
                Thread.sleep(100); // Poll every 100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Wait was interrupted", e);
            }
        }
        
        logger.error("Condition did not become true within {}: {}", timeout, errorMessage);
        throw new RuntimeException(errorMessage);
    }

    /**
     * Wait for custom condition to be true
     * @param condition Boolean supplier condition to wait for
     * @param timeoutSeconds Timeout in seconds
     * @return True if condition was met
     */
    @Step("Wait for custom condition")
    public static boolean waitForCondition(BooleanSupplier condition, int timeoutSeconds) {
        logger.debug("Waiting for custom condition with timeout: {} seconds", timeoutSeconds);
        try {
            waitUntil(condition, Duration.ofSeconds(timeoutSeconds), "Custom condition was not met");
            return true;
        } catch (Exception e) {
            logger.warn("Custom condition was not met within {} seconds: {}", timeoutSeconds, e.getMessage());
            return false;
        }
    }

    /**
     * Simple sleep method (use sparingly)
     * @param milliseconds Duration to sleep in milliseconds
     */
    @Step("Sleep for {milliseconds} milliseconds")
    public static void sleep(long milliseconds) {
        logger.debug("Sleeping for {} milliseconds", milliseconds);
        try {
            Thread.sleep(milliseconds);
            logger.debug("Sleep completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep was interrupted", e);
        }
    }

    /**
     * Wait for page to load completely
     */
    @Step("Wait for page to load completely")
    public static void waitForPageLoad() {
        logger.debug("Waiting for page to load completely");
        try {
            waitUntil(() -> {
                try {
                    String readyState = (String) BrowserUtils.executeJavaScript("return document.readyState");
                    return "complete".equals(readyState);
                } catch (Exception e) {
                    return false;
                }
            }, Duration.ofSeconds(30), "Page did not load completely");
            logger.debug("Page loaded completely");
        } catch (Exception e) {
            logger.error("Page did not load within timeout: {}", e.getMessage());
            throw new RuntimeException("Page did not load within timeout", e);
        }
    }

    /**
     * Wait for element text/value to change to expected value
     * @param element Element to monitor
     * @param expectedValue Expected value
     * @param timeoutSeconds Timeout in seconds
     * @return True if value changed as expected
     */
    @Step("Wait for element value to change to: {expectedValue}")
    public static boolean waitForElementValueChange(SelenideElement element, String expectedValue, int timeoutSeconds) {
        logger.debug("Waiting for element value to change to: {}", expectedValue);
        return waitForCondition(() -> {
            try {
                String currentValue = element.getText().trim();
                return expectedValue.equals(currentValue);
            } catch (Exception e) {
                logger.debug("Exception while checking element value: {}", e.getMessage());
                return false;
            }
        }, timeoutSeconds);
    }

    /**
     * Wait for numeric counter element to reach expected value
     * @param element Counter element to monitor
     * @param expectedValue Expected numeric value
     * @param timeoutSeconds Timeout in seconds
     * @return True if counter reached expected value
     */
    @Step("Wait for counter to reach value: {expectedValue}")
    public static boolean waitForCounterValue(SelenideElement element, int expectedValue, int timeoutSeconds) {
        logger.debug("Waiting for counter to reach value: {}", expectedValue);
        return waitForCondition(() -> {
            try {
                int currentValue = Integer.parseInt(element.getText().trim());
                return currentValue == expectedValue;
            } catch (Exception e) {
                logger.debug("Exception while checking counter value: {}", e.getMessage());
                return false;
            }
        }, timeoutSeconds);
    }

    /**
     * Wait for element to become interactable (visible and enabled)
     * @param element Element to wait for
     * @param timeoutSeconds Timeout in seconds
     * @return True if element became interactable
     */
    @Step("Wait for element to become interactable")
    public static boolean waitForElementInteractable(SelenideElement element, int timeoutSeconds) {
        logger.debug("Waiting for element to become interactable");
        return waitForCondition(() -> {
            try {
                return element.exists() && element.isDisplayed() && element.isEnabled();
            } catch (Exception e) {
                logger.debug("Exception while checking element interactability: {}", e.getMessage());
                return false;
            }
        }, timeoutSeconds);
    }

    /**
     * Wait for JavaScript condition to be true using custom script
     * @param jsCondition JavaScript condition that returns boolean
     * @param timeoutSeconds Timeout in seconds
     * @param description Description for logging
     * @return True if condition was met
     */
    @Step("Wait for JavaScript condition: {description}")
    public static boolean waitForJavaScriptCondition(String jsCondition, int timeoutSeconds, String description) {
        logger.debug("Waiting for JavaScript condition: {}", description);
        return waitForCondition(() -> {
            try {
                Object result = BrowserUtils.executeJavaScript(jsCondition);
                return Boolean.TRUE.equals(result);
            } catch (Exception e) {
                logger.debug("Exception while executing JavaScript condition: {}", e.getMessage());
                return false;
            }
        }, timeoutSeconds);
    }

    /**
     * Wait for DOM element to exist using XPath
     * @param xpath XPath expression
     * @param timeoutSeconds Timeout in seconds
     * @return True if element exists
     */
    @Step("Wait for DOM element to exist: {xpath}")
    public static boolean waitForDOMElement(String xpath, int timeoutSeconds) {
        logger.debug("Waiting for DOM element to exist: {}", xpath);
        String jsScript = "return document.evaluate(\"" + xpath + "\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue !== null;";
        return waitForJavaScriptCondition(jsScript, timeoutSeconds, "DOM element existence: " + xpath);
    }
}