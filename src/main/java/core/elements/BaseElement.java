package core.elements;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;

/**
 * BaseElement wraps SelenideElement and provides common functionality
 * with logging and Allure step annotations
 */
public class BaseElement {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final SelenideElement element;
    protected final String elementName;

    /**
     * Constructor for BaseElement
     * @param element SelenideElement to wrap
     * @param elementName Name for logging and reporting
     */
    public BaseElement(SelenideElement element, String elementName) {
        this.element = element;
        this.elementName = elementName != null ? elementName : "Element";
    }

    /**
     * Constructor for BaseElement with default name
     * @param element SelenideElement to wrap
     */
    public BaseElement(SelenideElement element) {
        this(element, "Element");
    }

    /**
     * Click on the element
     * @return This BaseElement for method chaining
     */
    @Step("Click on element")
    public BaseElement click() {
        logger.info("Clicking on [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).click();
            logger.debug("Successfully clicked on [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to click on [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to click on " + elementName, e);
        }
        return this;
    }

    /**
     * Click with specific options
     * @param options Click options
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement click(ClickOptions options) {
        logger.info("Clicking on [{}] with options", elementName);
        try {
            element.shouldBe(Condition.visible).click(options);
            logger.debug("Successfully clicked on [{}] with options", elementName);
        } catch (Exception e) {
            logger.error("Failed to click on [{}] with options: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to click on " + elementName, e);
        }
        return this;
    }

    /**
     * Double click on the element
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement doubleClick() {
        logger.info("Double clicking on [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).doubleClick();
            logger.debug("Successfully double clicked on [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to double click on [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to double click on " + elementName, e);
        }
        return this;
    }

    /**
     * Right click on the element
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement rightClick() {
        logger.info("Right clicking on [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).contextClick();
            logger.debug("Successfully right clicked on [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to right click on [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to right click on " + elementName, e);
        }
        return this;
    }

    /**
     * Hover over the element
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement hover() {
        logger.info("Hovering over [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).hover();
            logger.debug("Successfully hovered over [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to hover over [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to hover over " + elementName, e);
        }
        return this;
    }

    /**
     * Get text from the element
     * @return Element text
     */
    @Step("Get element text")
    public String getText() {
        logger.info("Getting text from [{}]", elementName);
        try {
            String text = element.shouldBe(Condition.visible).getText();
            logger.debug("Text from [{}]: '{}'", elementName, text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get text from " + elementName, e);
        }
    }

    /**
     * Get inner text from the element
     * @return Element inner text
     */
    @Step("Perform element action")
    public String getInnerText() {
        logger.info("Getting inner text from [{}]", elementName);
        try {
            String text = element.shouldBe(Condition.visible).innerText();
            logger.debug("Inner text from [{}]: '{}'", elementName, text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get inner text from [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get inner text from " + elementName, e);
        }
    }

    /**
     * Get attribute value from the element
     * @param attributeName Attribute name
     * @return Attribute value
     */
    @Step("Perform element action")
    public String getAttribute(String attributeName) {
        logger.info("Getting attribute '{}' from [{}]", attributeName, elementName);
        try {
            String value = element.shouldBe(Condition.visible).getAttribute(attributeName);
            logger.debug("Attribute '{}' from [{}]: '{}'", attributeName, elementName, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute '{}' from [{}]: {}", attributeName, elementName, e.getMessage());
            throw new RuntimeException("Failed to get attribute from " + elementName, e);
        }
    }

    /**
     * Get value from the element
     * @return Element value
     */
    @Step("Perform element action")
    public String getValue() {
        logger.info("Getting value from [{}]", elementName);
        try {
            String value = element.shouldBe(Condition.visible).getValue();
            logger.debug("Value from [{}]: '{}'", elementName, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get value from [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get value from " + elementName, e);
        }
    }

    /**
     * Check if element is displayed
     * @return True if displayed, false otherwise
     */
    @Step("Perform element action")
    public boolean isDisplayed() {
        logger.debug("Checking if [{}] is displayed", elementName);
        try {
            boolean displayed = element.isDisplayed();
            logger.debug("[{}] is displayed: {}", elementName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.debug("[{}] is not displayed: {}", elementName, e.getMessage());
            return false;
        }
    }

    /**
     * Check if element is visible
     * @return True if visible, false otherwise
     */
    @Step("Perform element action")
    public boolean isVisible() {
        logger.debug("Checking if [{}] is visible", elementName);
        try {
            boolean visible = element.isDisplayed();
            logger.debug("[{}] is visible: {}", elementName, visible);
            return visible;
        } catch (Exception e) {
            logger.debug("[{}] is not visible: {}", elementName, e.getMessage());
            return false;
        }
    }

    /**
     * Check if element exists in DOM
     * @return True if exists, false otherwise
     */
    @Step("Perform element action")
    public boolean exists() {
        logger.debug("Checking if [{}] exists", elementName);
        try {
            boolean exists = element.exists();
            logger.debug("[{}] exists: {}", elementName, exists);
            return exists;
        } catch (Exception e) {
            logger.debug("[{}] does not exist: {}", elementName, e.getMessage());
            return false;
        }
    }

    /**
     * Check if element is enabled
     * @return True if enabled, false otherwise
     */
    @Step("Perform element action")
    public boolean isEnabled() {
        logger.debug("Checking if [{}] is enabled", elementName);
        try {
            boolean enabled = element.isEnabled();
            logger.debug("[{}] is enabled: {}", elementName, enabled);
            return enabled;
        } catch (Exception e) {
            logger.debug("[{}] is not enabled: {}", elementName, e.getMessage());
            return false;
        }
    }

    /**
     * Wait for element to be visible
     * @return This BaseElement for method chaining
     */
    @Step("Wait for element to be visible")
    public BaseElement waitForVisible() {
        logger.info("Waiting for [{}] to be visible", elementName);
        try {
            element.shouldBe(Condition.visible);
            logger.debug("[{}] is now visible", elementName);
        } catch (Exception e) {
            logger.error("Failed waiting for [{}] to be visible: {}", elementName, e.getMessage());
            throw new RuntimeException("Element " + elementName + " did not become visible", e);
        }
        return this;
    }

    /**
     * Wait for element to be visible with timeout
     * @param timeout Timeout duration
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement waitForVisible(Duration timeout) {
        logger.info("Waiting for [{}] to be visible with timeout: {}", elementName, timeout);
        try {
            element.shouldBe(Condition.visible, timeout);
            logger.debug("[{}] is now visible", elementName);
        } catch (Exception e) {
            logger.error("Failed waiting for [{}] to be visible within {}: {}", elementName, timeout, e.getMessage());
            throw new RuntimeException("Element " + elementName + " did not become visible within " + timeout, e);
        }
        return this;
    }

    /**
     * Wait for element to be clickable
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement waitForClickable() {
        logger.info("Waiting for [{}] to be clickable", elementName);
        try {
            element.shouldBe(Condition.visible);
            element.shouldBe(Condition.enabled);
            logger.debug("[{}] is now clickable", elementName);
        } catch (Exception e) {
            logger.error("Failed waiting for [{}] to be clickable: {}", elementName, e.getMessage());
            throw new RuntimeException("Element " + elementName + " did not become clickable", e);
        }
        return this;
    }

    /**
     * Wait for element to be enabled
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement waitForEnabled() {
        logger.info("Waiting for [{}] to be enabled", elementName);
        try {
            element.shouldBe(Condition.enabled);
            logger.debug("[{}] is now enabled", elementName);
        } catch (Exception e) {
            logger.error("Failed waiting for [{}] to be enabled: {}", elementName, e.getMessage());
            throw new RuntimeException("Element " + elementName + " did not become enabled", e);
        }
        return this;
    }

    /**
     * Wait for element to disappear
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement waitForDisappear() {
        logger.info("Waiting for [{}] to disappear", elementName);
        try {
            element.shouldNotBe(Condition.visible);
            logger.debug("[{}] has disappeared", elementName);
        } catch (Exception e) {
            logger.error("Failed waiting for [{}] to disappear: {}", elementName, e.getMessage());
            throw new RuntimeException("Element " + elementName + " did not disappear", e);
        }
        return this;
    }

    /**
     * Wait for element to disappear with timeout
     * @param timeout Timeout duration
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement waitForDisappear(Duration timeout) {
        logger.info("Waiting for [{}] to disappear with timeout: {}", elementName, timeout);
        try {
            element.shouldNotBe(Condition.visible, timeout);
            logger.debug("[{}] has disappeared", elementName);
        } catch (Exception e) {
            logger.error("Failed waiting for [{}] to disappear within {}: {}", elementName, timeout, e.getMessage());
            throw new RuntimeException("Element " + elementName + " did not disappear within " + timeout, e);
        }
        return this;
    }

    /**
     * Scroll to element
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement scrollTo() {
        logger.info("Scrolling to [{}]", elementName);
        try {
            element.scrollTo();
            logger.debug("Successfully scrolled to [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to scroll to [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to scroll to " + elementName, e);
        }
        return this;
    }

    /**
     * Get the underlying SelenideElement
     * @return SelenideElement
     */
    public SelenideElement getElement() {
        return element;
    }

    /**
     * Drag and drop this element to target element
     * @param targetElement Target element to drop to
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement dragAndDropTo(SelenideElement targetElement) {
        logger.info("Dragging [{}] to target element", elementName);
        try {
            Actions actions = new Actions(Selenide.webdriver().driver().getWebDriver());
            actions.dragAndDrop(element.shouldBe(Condition.visible), targetElement.shouldBe(Condition.visible))
                   .perform();
            logger.debug("Successfully dragged [{}] to target element", elementName);
        } catch (Exception e) {
            logger.error("Failed to drag [{}] to target: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to drag " + elementName + " to target", e);
        }
        return this;
    }

    /**
     * Drag and drop this element to target BaseElement
     * @param targetElement Target BaseElement to drop to
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement dragAndDropTo(BaseElement targetElement) {
        logger.info("Dragging [{}] to [{}]", elementName, targetElement.getElementName());
        try {
            Actions actions = new Actions(Selenide.webdriver().driver().getWebDriver());
            actions.dragAndDrop(element.shouldBe(Condition.visible), 
                               targetElement.getSelenideElement().shouldBe(Condition.visible))
                   .perform();
            logger.debug("Successfully dragged [{}] to [{}]", elementName, targetElement.getElementName());
        } catch (Exception e) {
            logger.error("Failed to drag [{}] to [{}]: {}", elementName, targetElement.getElementName(), e.getMessage());
            throw new RuntimeException("Failed to drag " + elementName + " to " + targetElement.getElementName(), e);
        }
        return this;
    }

    /**
     * Send keyboard keys to element
     * @param keys Keys to send
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement sendKeys(CharSequence... keys) {
        logger.info("Sending keys to [{}]: {}", elementName, java.util.Arrays.toString(keys));
        try {
            element.shouldBe(Condition.visible).sendKeys(keys);
            logger.debug("Successfully sent keys to [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to send keys to [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to send keys to " + elementName, e);
        }
        return this;
    }

    /**
     * Press key on element
     * @param key Key to press
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement pressKey(Keys key) {
        logger.info("Pressing key on [{}]: {}", elementName, key);
        try {
            element.shouldBe(Condition.visible).sendKeys(key);
            logger.debug("Successfully pressed key on [{}]: {}", elementName, key);
        } catch (Exception e) {
            logger.error("Failed to press key on [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to press key on " + elementName, e);
        }
        return this;
    }

    /**
     * Upload file to element (for file input elements)
     * @param filePath Path to file to upload
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement uploadFile(String filePath) {
        logger.info("Uploading file to [{}]: {}", elementName, filePath);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + filePath);
            }
            element.shouldBe(Condition.visible).uploadFile(file);
            logger.debug("Successfully uploaded file to [{}]: {}", elementName, filePath);
        } catch (Exception e) {
            logger.error("Failed to upload file to [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to upload file to " + elementName, e);
        }
        return this;
    }

    /**
     * Select option by text (for select elements)
     * @param optionText Option text to select
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement selectOption(String optionText) {
        logger.info("Selecting option by text in [{}]: {}", elementName, optionText);
        try {
            element.shouldBe(Condition.visible).selectOption(optionText);
            logger.debug("Successfully selected option in [{}]: {}", elementName, optionText);
        } catch (Exception e) {
            logger.error("Failed to select option in [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to select option in " + elementName, e);
        }
        return this;
    }

    /**
     * Select option by value (for select elements)
     * @param value Option value to select
     * @return This BaseElement for method chaining
     */
    @Step("Perform element action")
    public BaseElement selectOptionByValue(String value) {
        logger.info("Selecting option by value in [{}]: {}", elementName, value);
        try {
            element.shouldBe(Condition.visible).selectOptionByValue(value);
            logger.debug("Successfully selected option by value in [{}]: {}", elementName, value);
        } catch (Exception e) {
            logger.error("Failed to select option by value in [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to select option by value in " + elementName, e);
        }
        return this;
    }

    /**
     * Get CSS property value from element
     * @param propertyName CSS property name
     * @return CSS property value
     */
    @Step("Perform element action")
    public String getCssValue(String propertyName) {
        logger.info("Getting CSS property [{}] from [{}]", propertyName, elementName);
        try {
            String value = element.shouldBe(Condition.visible).getCssValue(propertyName);
            logger.debug("CSS property [{}] from [{}]: {}", propertyName, elementName, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get CSS property [{}] from [{}]: {}", propertyName, elementName, e.getMessage());
            throw new RuntimeException("Failed to get CSS property " + propertyName + " from " + elementName, e);
        }
    }

    /**
     * Get element location
     * @return Element location as Point
     */
    @Step("Perform element action")
    public Point getLocation() {
        logger.info("Getting location of [{}]", elementName);
        try {
            Point location = element.shouldBe(Condition.visible).getLocation();
            logger.debug("Location of [{}]: x={}, y={}", elementName, location.x, location.y);
            return location;
        } catch (Exception e) {
            logger.error("Failed to get location of [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get location of " + elementName, e);
        }
    }

    /**
     * Get element size
     * @return Element size as Dimension
     */
    @Step("Perform element action")
    public Dimension getSize() {
        logger.info("Getting size of [{}]", elementName);
        try {
            Dimension size = element.shouldBe(Condition.visible).getSize();
            logger.debug("Size of [{}]: width={}, height={}", elementName, size.width, size.height);
            return size;
        } catch (Exception e) {
            logger.error("Failed to get size of [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get size of " + elementName, e);
        }
    }

    /**
     * Check if element is selected (for checkboxes, radio buttons, options)
     * @return True if selected, false otherwise
     */
    @Step("Perform element action")
    public boolean isSelected() {
        logger.debug("Checking if [{}] is selected", elementName);
        try {
            boolean selected = element.isSelected();
            logger.debug("[{}] is selected: {}", elementName, selected);
            return selected;
        } catch (Exception e) {
            logger.debug("[{}] selection state could not be determined: {}", elementName, e.getMessage());
            return false;
        }
    }

    /**
     * Execute JavaScript on element
     * @param script JavaScript code to execute
     * @param arguments Additional arguments for the script
     * @return Script execution result
     */
    @Step("Perform element action")
    public Object executeScript(String script, Object... arguments) {
        logger.info("Executing JavaScript on [{}]: {}", elementName, script);
        try {
            Object result = Selenide.executeJavaScript(script, element, arguments);
            logger.debug("JavaScript execution result on [{}]: {}", elementName, result);
            return result;
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript on [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to execute JavaScript on " + elementName, e);
        }
    }

    /**
     * Get underlying SelenideElement
     * @return SelenideElement
     */
    public SelenideElement getSelenideElement() {
        return element;
    }

    /**
     * Get element name
     * @return Element name
     */
    public String getElementName() {
        return elementName;
    }
}