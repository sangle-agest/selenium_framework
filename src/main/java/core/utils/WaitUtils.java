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
}