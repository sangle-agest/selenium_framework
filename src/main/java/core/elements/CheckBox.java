package core.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

/**
 * CheckBox element wrapper extending BaseElement
 */
public class CheckBox extends BaseElement {

    /**
     * Constructor for CheckBox element
     * @param element SelenideElement to wrap
     * @param elementName Name for logging and reporting
     */
    public CheckBox(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    /**
     * Constructor for CheckBox element with default name
     * @param element SelenideElement to wrap
     */
    public CheckBox(SelenideElement element) {
        super(element, "CheckBox");
    }

    /**
     * Check the checkbox
     * @return This CheckBox for method chaining
     */
    @Step("Check checkbox [{elementName}]")
    public CheckBox check() {
        logger.info("Checking checkbox [{}]", elementName);
        try {
            if (!isChecked()) {
                element.shouldBe(Condition.visible).click();
                logger.debug("Successfully checked checkbox [{}]", elementName);
            } else {
                logger.debug("Checkbox [{}] is already checked", elementName);
            }
        } catch (Exception e) {
            logger.error("Failed to check checkbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check " + elementName, e);
        }
        return this;
    }

    /**
     * Uncheck the checkbox
     * @return This CheckBox for method chaining
     */
    @Step("Uncheck checkbox [{elementName}]")
    public CheckBox uncheck() {
        logger.info("Unchecking checkbox [{}]", elementName);
        try {
            if (isChecked()) {
                element.shouldBe(Condition.visible).click();
                logger.debug("Successfully unchecked checkbox [{}]", elementName);
            } else {
                logger.debug("Checkbox [{}] is already unchecked", elementName);
            }
        } catch (Exception e) {
            logger.error("Failed to uncheck checkbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to uncheck " + elementName, e);
        }
        return this;
    }

    /**
     * Toggle the checkbox state
     * @return This CheckBox for method chaining
     */
    @Step("Toggle checkbox [{elementName}]")
    public CheckBox toggle() {
        logger.info("Toggling checkbox [{}]", elementName);
        try {
            element.shouldBe(Condition.visible).click();
            logger.debug("Successfully toggled checkbox [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to toggle checkbox [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to toggle " + elementName, e);
        }
        return this;
    }

    /**
     * Set checkbox state
     * @param checked True to check, false to uncheck
     * @return This CheckBox for method chaining
     */
    @Step("Set checkbox [{elementName}] to {checked}")
    public CheckBox setChecked(boolean checked) {
        logger.info("Setting checkbox [{}] to {}", elementName, checked);
        if (checked) {
            check();
        } else {
            uncheck();
        }
        return this;
    }

    /**
     * Check if checkbox is checked
     * @return True if checked, false otherwise
     */
    @Step("Check if checkbox [{elementName}] is checked")
    public boolean isChecked() {
        logger.debug("Checking if checkbox [{}] is checked", elementName);
        try {
            boolean checked = element.shouldBe(Condition.visible).isSelected();
            logger.debug("Checkbox [{}] is checked: {}", elementName, checked);
            return checked;
        } catch (Exception e) {
            logger.error("Failed to check if checkbox [{}] is checked: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check state of " + elementName, e);
        }
    }
}