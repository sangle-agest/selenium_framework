package core.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for JavaScript operations
 */
public class JavaScriptUtils {
    private static final Logger logger = LoggerFactory.getLogger(JavaScriptUtils.class);

    /**
     * Execute JavaScript code
     * @param script JavaScript code to execute
     * @param arguments Arguments to pass to the script
     * @return Result of script execution
     */
    @Step("Execute JavaScript: {script}")
    public static Object executeScript(String script, Object... arguments) {
        logger.info("Executing JavaScript: {}", script);
        try {
            Object result = Selenide.executeJavaScript(script, arguments);
            logger.debug("JavaScript execution result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to execute JavaScript", e);
        }
    }

    /**
     * Scroll to top of page
     */
    @Step("Scroll to top of page")
    public static void scrollToTop() {
        logger.info("Scrolling to top of page");
        try {
            executeScript("window.scrollTo(0, 0);");
            logger.debug("Successfully scrolled to top of page");
        } catch (Exception e) {
            logger.error("Failed to scroll to top: {}", e.getMessage());
            throw new RuntimeException("Failed to scroll to top", e);
        }
    }

    /**
     * Scroll to bottom of page
     */
    @Step("Scroll to bottom of page")
    public static void scrollToBottom() {
        logger.info("Scrolling to bottom of page");
        try {
            executeScript("window.scrollTo(0, document.body.scrollHeight);");
            logger.debug("Successfully scrolled to bottom of page");
        } catch (Exception e) {
            logger.error("Failed to scroll to bottom: {}", e.getMessage());
            throw new RuntimeException("Failed to scroll to bottom", e);
        }
    }

    /**
     * Scroll by specific pixels
     * @param x Horizontal scroll amount
     * @param y Vertical scroll amount
     */
    @Step("Scroll by pixels: x={x}, y={y}")
    public static void scrollByPixels(int x, int y) {
        logger.info("Scrolling by pixels: x={}, y={}", x, y);
        try {
            executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
            logger.debug("Successfully scrolled by pixels: x={}, y={}", x, y);
        } catch (Exception e) {
            logger.error("Failed to scroll by pixels: {}", e.getMessage());
            throw new RuntimeException("Failed to scroll by pixels", e);
        }
    }

    /**
     * Scroll element into view
     * @param element Element to scroll into view
     */
    @Step("Scroll element into view")
    public static void scrollIntoView(SelenideElement element) {
        logger.info("Scrolling element into view");
        try {
            executeScript("arguments[0].scrollIntoView(true);", element);
            logger.debug("Successfully scrolled element into view");
        } catch (Exception e) {
            logger.error("Failed to scroll element into view: {}", e.getMessage());
            throw new RuntimeException("Failed to scroll element into view", e);
        }
    }

    /**
     * Highlight element with colored border
     * @param element Element to highlight
     * @param color Border color (e.g., "red", "#ff0000")
     */
    @Step("Highlight element with color: {color}")
    public static void highlightElement(SelenideElement element, String color) {
        logger.info("Highlighting element with color: {}", color);
        try {
            executeScript("arguments[0].style.border='3px solid " + color + "';", element);
            logger.debug("Successfully highlighted element with color: {}", color);
        } catch (Exception e) {
            logger.error("Failed to highlight element: {}", e.getMessage());
            throw new RuntimeException("Failed to highlight element", e);
        }
    }

    /**
     * Remove highlight from element
     * @param element Element to remove highlight from
     */
    @Step("Remove highlight from element")
    public static void removeHighlight(SelenideElement element) {
        logger.info("Removing highlight from element");
        try {
            executeScript("arguments[0].style.border='';", element);
            logger.debug("Successfully removed highlight from element");
        } catch (Exception e) {
            logger.error("Failed to remove highlight: {}", e.getMessage());
            throw new RuntimeException("Failed to remove highlight", e);
        }
    }

    /**
     * Click element using JavaScript
     * @param element Element to click
     */
    @Step("Click element using JavaScript")
    public static void clickElement(SelenideElement element) {
        logger.info("Clicking element using JavaScript");
        try {
            executeScript("arguments[0].click();", element);
            logger.debug("Successfully clicked element using JavaScript");
        } catch (Exception e) {
            logger.error("Failed to click element using JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to click element using JavaScript", e);
        }
    }

    /**
     * Set element value using JavaScript
     * @param element Element to set value for
     * @param value Value to set
     */
    @Step("Set element value using JavaScript: {value}")
    public static void setElementValue(SelenideElement element, String value) {
        logger.info("Setting element value using JavaScript: {}", value);
        try {
            executeScript("arguments[0].value = arguments[1];", element, value);
            logger.debug("Successfully set element value using JavaScript: {}", value);
        } catch (Exception e) {
            logger.error("Failed to set element value using JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to set element value using JavaScript", e);
        }
    }

    /**
     * Get element value using JavaScript
     * @param element Element to get value from
     * @return Element value
     */
    @Step("Get element value using JavaScript")
    public static String getElementValue(SelenideElement element) {
        logger.info("Getting element value using JavaScript");
        try {
            String value = (String) executeScript("return arguments[0].value;", element);
            logger.debug("Element value: {}", value);
            return value != null ? value : "";
        } catch (Exception e) {
            logger.error("Failed to get element value using JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to get element value using JavaScript", e);
        }
    }

    /**
     * Check if element is visible using JavaScript
     * @param element Element to check
     * @return True if element is visible
     */
    @Step("Check if element is visible using JavaScript")
    public static boolean isElementVisible(SelenideElement element) {
        logger.info("Checking if element is visible using JavaScript");
        try {
            Boolean visible = (Boolean) executeScript(
                "var elem = arguments[0];" +
                "return elem.offsetWidth > 0 && elem.offsetHeight > 0 && " +
                "window.getComputedStyle(elem).visibility !== 'hidden' && " +
                "window.getComputedStyle(elem).display !== 'none';", element);
            logger.debug("Element visible: {}", visible);
            return visible != null ? visible : false;
        } catch (Exception e) {
            logger.error("Failed to check element visibility using JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to check element visibility using JavaScript", e);
        }
    }

    /**
     * Get page title using JavaScript
     * @return Page title
     */
    @Step("Get page title using JavaScript")
    public static String getPageTitle() {
        logger.info("Getting page title using JavaScript");
        try {
            String title = (String) executeScript("return document.title;");
            logger.debug("Page title: {}", title);
            return title != null ? title : "";
        } catch (Exception e) {
            logger.error("Failed to get page title using JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to get page title using JavaScript", e);
        }
    }

    /**
     * Get page URL using JavaScript
     * @return Page URL
     */
    @Step("Get page URL using JavaScript")
    public static String getPageUrl() {
        logger.info("Getting page URL using JavaScript");
        try {
            String url = (String) executeScript("return window.location.href;");
            logger.debug("Page URL: {}", url);
            return url != null ? url : "";
        } catch (Exception e) {
            logger.error("Failed to get page URL using JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to get page URL using JavaScript", e);
        }
    }

    /**
     * Refresh page using JavaScript
     */
    @Step("Refresh page using JavaScript")
    public static void refreshPage() {
        logger.info("Refreshing page using JavaScript");
        try {
            executeScript("window.location.reload();");
            logger.debug("Successfully refreshed page using JavaScript");
        } catch (Exception e) {
            logger.error("Failed to refresh page using JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to refresh page using JavaScript", e);
        }
    }

    /**
     * Navigate to URL using JavaScript
     * @param url URL to navigate to
     */
    @Step("Navigate to URL using JavaScript: {url}")
    public static void navigateToUrl(String url) {
        logger.info("Navigating to URL using JavaScript: {}", url);
        try {
            executeScript("window.location.href = arguments[0];", url);
            logger.debug("Successfully navigated to URL using JavaScript: {}", url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL using JavaScript: {}", e.getMessage());
            throw new RuntimeException("Failed to navigate to URL using JavaScript", e);
        }
    }

    /**
     * Get document ready state
     * @return Document ready state (loading, interactive, complete)
     */
    @Step("Get document ready state")
    public static String getDocumentReadyState() {
        logger.info("Getting document ready state");
        try {
            String state = (String) executeScript("return document.readyState;");
            logger.debug("Document ready state: {}", state);
            return state != null ? state : "unknown";
        } catch (Exception e) {
            logger.error("Failed to get document ready state: {}", e.getMessage());
            throw new RuntimeException("Failed to get document ready state", e);
        }
    }

    /**
     * Wait for page to be fully loaded
     */
    @Step("Wait for page to be fully loaded")
    public static void waitForPageLoad() {
        logger.info("Waiting for page to be fully loaded");
        try {
            WaitUtils.waitForCondition(() -> "complete".equals(getDocumentReadyState()), 30);
            logger.debug("Page is fully loaded");
        } catch (Exception e) {
            logger.error("Failed to wait for page load: {}", e.getMessage());
            throw new RuntimeException("Failed to wait for page load", e);
        }
    }

    /**
     * Remove element from DOM
     * @param element Element to remove
     */
    @Step("Remove element from DOM")
    public static void removeElement(SelenideElement element) {
        logger.info("Removing element from DOM");
        try {
            executeScript("arguments[0].remove();", element);
            logger.debug("Successfully removed element from DOM");
        } catch (Exception e) {
            logger.error("Failed to remove element from DOM: {}", e.getMessage());
            throw new RuntimeException("Failed to remove element from DOM", e);
        }
    }

    /**
     * Change element attribute
     * @param element Element to modify
     * @param attribute Attribute name
     * @param value New attribute value
     */
    @Step("Change element attribute {attribute} to {value}")
    public static void changeElementAttribute(SelenideElement element, String attribute, String value) {
        logger.info("Changing element attribute {} to {}", attribute, value);
        try {
            executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attribute, value);
            logger.debug("Successfully changed element attribute {} to {}", attribute, value);
        } catch (Exception e) {
            logger.error("Failed to change element attribute: {}", e.getMessage());
            throw new RuntimeException("Failed to change element attribute", e);
        }
    }

    /**
     * Get element attribute using JavaScript
     * @param element Element to get attribute from
     * @param attribute Attribute name
     * @return Attribute value
     */
    @Step("Get element attribute {attribute}")
    public static String getElementAttribute(SelenideElement element, String attribute) {
        logger.info("Getting element attribute: {}", attribute);
        try {
            String value = (String) executeScript("return arguments[0].getAttribute(arguments[1]);", element, attribute);
            logger.debug("Element attribute {}: {}", attribute, value);
            return value != null ? value : "";
        } catch (Exception e) {
            logger.error("Failed to get element attribute: {}", e.getMessage());
            throw new RuntimeException("Failed to get element attribute", e);
        }
    }

    /**
     * Trigger custom JavaScript event on element
     * @param element Element to trigger event on
     * @param eventType Event type (e.g., "change", "click", "focus")
     */
    @Step("Trigger {eventType} event on element")
    public static void triggerEvent(SelenideElement element, String eventType) {
        logger.info("Triggering {} event on element", eventType);
        try {
            executeScript(
                "var event = new Event(arguments[1], {bubbles: true, cancelable: true});" +
                "arguments[0].dispatchEvent(event);", element, eventType);
            logger.debug("Successfully triggered {} event on element", eventType);
        } catch (Exception e) {
            logger.error("Failed to trigger event: {}", e.getMessage());
            throw new RuntimeException("Failed to trigger event", e);
        }
    }
}