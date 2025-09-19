package core.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

/**
 * TextBox element wrapper extending BaseElement
 */
public class TextBox extends BaseElement {

    /**
     * Constructor for TextBox element
     * @param element SelenideElement to wrap
     * @param elementName Name for logging and reporting
     */
    public TextBox(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    /**
     * Constructor for TextBox element with default name
     * @param element SelenideElement to wrap
     */
    public TextBox(SelenideElement element) {
        super(element, "TextBox");
    }

    /**
     * Type text into the textbox
     * @param text Text to type
     * @return This TextBox for method chaining
     */
    @Step("Type text into textbox")
    public TextBox type(String text) {
        logger.info("Typing '{}' into textbox [{}]", text, elementName);
        try {
            element.shouldBe(Condition.visible).sendKeys(text);
            logger.debug("Successfully typed text into textbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to type text into textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to type text into " + elementName, e);
        }
        return this;
    }

    /**
     * Send keys to the textbox
     * @param text Text to send
     * @return This TextBox for method chaining
     */
    @Step("Send keys to textbox")
    public TextBox sendKeys(String text) {
        logger.info("Sending keys '{}' to textbox [{}]", text, elementName);
        try {
            element.shouldBe(Condition.visible).sendKeys(text);
            logger.debug("Successfully sent keys to textbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to send keys to textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to send keys to " + elementName, e);
        }
        return this;
    }

    /**
     * Set value in the textbox (clears existing text first)
     * @param text Text to set
     * @return This TextBox for method chaining
     */
        @Step("Type text into textbox")
    public TextBox setValue(String text) {
        logger.info("Setting value '{}' in textbox [{}]", text, elementName);
        try {
            element.shouldBe(Condition.visible).setValue(text);
            logger.debug("Successfully set value in textbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to set value in textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to set value in " + elementName, e);
        }
        return this;
    }

    /**
     * Clear the textbox
     * @return This TextBox for method chaining
     */
    @Step("Clear textbox")
    public TextBox clear() {
        logger.info("Clearing textbox [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).clear();
            logger.debug("Successfully cleared textbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to clear textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to clear " + elementName, e);
        }
        return this;
    }

    /**
     * Clear textbox and set new value
     * @param text Text to set
     * @return This TextBox for method chaining
     */
    @Step("Clear and set value in textbox")
    public TextBox clearAndType(String text) {
        logger.info("Clearing and typing '{}' into textbox [{}]", text, elementName);
        clear();
        type(text);
        return this;
    }

    /**
     * Press Enter key
     * @return This TextBox for method chaining
     */
    @Step("Press Enter in textbox")
    public TextBox pressEnter() {
        logger.info("Pressing Enter in textbox [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).sendKeys(Keys.ENTER);
            logger.debug("Successfully pressed Enter in textbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to press Enter in textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to press Enter in " + elementName, e);
        }
        return this;
    }

    /**
     * Press Tab key
     * @return This TextBox for method chaining
     */
    @Step("Press Tab in textbox")
    public TextBox pressTab() {
        logger.info("Pressing Tab in textbox [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).sendKeys(Keys.TAB);
            logger.debug("Successfully pressed Tab in textbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to press Tab in textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to press Tab in " + elementName, e);
        }
        return this;
    }

    /**
     * Press Escape key
     * @return This TextBox for method chaining
     */
    @Step("Press Escape in textbox")
    public TextBox pressEscape() {
        logger.info("Pressing Escape in textbox [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).sendKeys(Keys.ESCAPE);
            logger.debug("Successfully pressed Escape in textbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to press Escape in textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to press Escape in " + elementName, e);
        }
        return this;
    }

    /**
     * Select all text in textbox
     * @return This TextBox for method chaining
     */
    @Step("Select all text in textbox")
    public TextBox selectAll() {
        logger.info("Selecting all text in textbox [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).sendKeys(Keys.CONTROL + "a");
            logger.debug("Successfully selected all text in textbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to select all text in textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to select all text in " + elementName, e);
        }
        return this;
    }

    /**
     * Get placeholder text
     * @return Placeholder text
     */
    @Step("Get placeholder from textbox")
    public String getPlaceholder() {
        logger.info("Getting placeholder from textbox [{}]", elementName);
        try {
            String placeholder = element.shouldBe(Condition.visible).getAttribute("placeholder");
            logger.debug("Placeholder from textbox [{}]: '{}'", elementName, placeholder);
            return placeholder;
        } catch (Exception e) {
            logger.error("Failed to get placeholder from textbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get placeholder from " + elementName, e);
        }
    }

    /**
     * Check if textbox is empty
     * @return True if empty, false otherwise
     */
    @Step("Check if textbox is empty")
    public boolean isEmpty() {
        String value = getValue();
        boolean empty = value == null || value.trim().isEmpty();
        logger.debug("Textbox [{}] is empty: {}", elementName, empty);
        return empty;
    }
}