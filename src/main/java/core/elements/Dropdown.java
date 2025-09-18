package core.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dropdown element wrapper extending BaseElement
 */
public class Dropdown extends BaseElement {

    /**
     * Constructor for Dropdown element
     * @param element SelenideElement to wrap
     * @param elementName Name for logging and reporting
     */
    public Dropdown(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    /**
     * Constructor for Dropdown element with default name
     * @param element SelenideElement to wrap
     */
    public Dropdown(SelenideElement element) {
        super(element, "Dropdown");
    }

    /**
     * Select option by visible text
     * @param text Visible text of option to select
     * @return This Dropdown for method chaining
     */
    @Step("Select option '{text}' from dropdown [{elementName}]")
    public Dropdown selectByText(String text) {
        logger.info("Selecting option '{}' from dropdown [{}]", text, elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            select.selectByVisibleText(text);
            logger.debug("Successfully selected option '{}' from dropdown [{}]", text, elementName);
        } catch (Exception e) {
            logger.error("Failed to select option '{}' from dropdown [{}]: {}", text, elementName, e.getMessage());
            throw new RuntimeException("Failed to select option from " + elementName, e);
        }
        return this;
    }

    /**
     * Select option by value
     * @param value Value of option to select
     * @return This Dropdown for method chaining
     */
    @Step("Select option by value '{value}' from dropdown [{elementName}]")
    public Dropdown selectByValue(String value) {
        logger.info("Selecting option by value '{}' from dropdown [{}]", value, elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            select.selectByValue(value);
            logger.debug("Successfully selected option by value '{}' from dropdown [{}]", value, elementName);
        } catch (Exception e) {
            logger.error("Failed to select option by value '{}' from dropdown [{}]: {}", value, elementName, e.getMessage());
            throw new RuntimeException("Failed to select option by value from " + elementName, e);
        }
        return this;
    }

    /**
     * Select option by index
     * @param index Index of option to select (0-based)
     * @return This Dropdown for method chaining
     */
    @Step("Select option by index {index} from dropdown [{elementName}]")
    public Dropdown selectByIndex(int index) {
        logger.info("Selecting option by index {} from dropdown [{}]", index, elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            select.selectByIndex(index);
            logger.debug("Successfully selected option by index {} from dropdown [{}]", index, elementName);
        } catch (Exception e) {
            logger.error("Failed to select option by index {} from dropdown [{}]: {}", index, elementName, e.getMessage());
            throw new RuntimeException("Failed to select option by index from " + elementName, e);
        }
        return this;
    }

    /**
     * Get selected option text
     * @return Selected option text
     */
    @Step("Get selected option text from dropdown [{elementName}]")
    public String getSelectedText() {
        logger.info("Getting selected option text from dropdown [{}]", elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            String selectedText = select.getFirstSelectedOption().getText();
            logger.debug("Selected option text from dropdown [{}]: '{}'", elementName, selectedText);
            return selectedText;
        } catch (Exception e) {
            logger.error("Failed to get selected option text from dropdown [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get selected text from " + elementName, e);
        }
    }

    /**
     * Get selected option value
     * @return Selected option value
     */
    @Step("Get selected option value from dropdown [{elementName}]")
    public String getSelectedValue() {
        logger.info("Getting selected option value from dropdown [{}]", elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            String selectedValue = select.getFirstSelectedOption().getAttribute("value");
            logger.debug("Selected option value from dropdown [{}]: '{}'", elementName, selectedValue);
            return selectedValue;
        } catch (Exception e) {
            logger.error("Failed to get selected option value from dropdown [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get selected value from " + elementName, e);
        }
    }

    /**
     * Get all option texts
     * @return List of all option texts
     */
    @Step("Get all option texts from dropdown [{elementName}]")
    public List<String> getAllOptionTexts() {
        logger.info("Getting all option texts from dropdown [{}]", elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            List<String> optionTexts = select.getOptions().stream()
                    .map(option -> option.getText())
                    .collect(Collectors.toList());
            logger.debug("All option texts from dropdown [{}]: {}", elementName, optionTexts);
            return optionTexts;
        } catch (Exception e) {
            logger.error("Failed to get all option texts from dropdown [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get all option texts from " + elementName, e);
        }
    }

    /**
     * Get all option values
     * @return List of all option values
     */
    @Step("Get all option values from dropdown [{elementName}]")
    public List<String> getAllOptionValues() {
        logger.info("Getting all option values from dropdown [{}]", elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            List<String> optionValues = select.getOptions().stream()
                    .map(option -> option.getAttribute("value"))
                    .collect(Collectors.toList());
            logger.debug("All option values from dropdown [{}]: {}", elementName, optionValues);
            return optionValues;
        } catch (Exception e) {
            logger.error("Failed to get all option values from dropdown [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get all option values from " + elementName, e);
        }
    }

    /**
     * Check if dropdown is multiple selection
     * @return True if multiple selection, false otherwise
     */
    @Step("Check if dropdown [{elementName}] is multiple selection")
    public boolean isMultiple() {
        logger.debug("Checking if dropdown [{}] is multiple selection", elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            boolean multiple = select.isMultiple();
            logger.debug("Dropdown [{}] is multiple selection: {}", elementName, multiple);
            return multiple;
        } catch (Exception e) {
            logger.error("Failed to check if dropdown [{}] is multiple: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check if dropdown is multiple " + elementName, e);
        }
    }

    /**
     * Get options count
     * @return Number of options
     */
    @Step("Get options count from dropdown [{elementName}]")
    public int getOptionsCount() {
        logger.info("Getting options count from dropdown [{}]", elementName);
        try {
            Select select = new Select(element.shouldBe(Condition.visible));
            int count = select.getOptions().size();
            logger.debug("Options count from dropdown [{}]: {}", elementName, count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get options count from dropdown [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get options count from " + elementName, e);
        }
    }

    /**
     * Check if option exists by text
     * @param text Option text to check
     * @return True if option exists, false otherwise
     */
    @Step("Check if option '{text}' exists in dropdown [{elementName}]")
    public boolean hasOption(String text) {
        logger.debug("Checking if option '{}' exists in dropdown [{}]", text, elementName);
        try {
            List<String> options = getAllOptionTexts();
            boolean exists = options.contains(text);
            logger.debug("Option '{}' exists in dropdown [{}]: {}", text, elementName, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Failed to check if option '{}' exists in dropdown [{}]: {}", text, elementName, e.getMessage());
            return false;
        }
    }
}