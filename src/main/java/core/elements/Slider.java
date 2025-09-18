package core.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import com.codeborne.selenide.Selenide;

/**
 * Slider element wrapper for handling HTML range inputs and sliders
 */
public class Slider extends BaseElement {

    /**
     * Constructor for Slider element
     * @param element SelenideElement representing the slider
     * @param elementName Name for logging and reporting
     */
    public Slider(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    /**
     * Constructor for Slider element with default name
     * @param element SelenideElement representing the slider
     */
    public Slider(SelenideElement element) {
        super(element, "Slider");
    }

    /**
     * Set slider value by moving to specific value
     * @param value Value to set
     * @return This Slider for method chaining
     */
    @Step("Set slider [{elementName}] value to {value}")
    public Slider setValue(String value) {
        logger.info("Setting slider [{}] value to {}", elementName, value);
        try {
            element.shouldBe(Condition.visible).setValue(value);
            logger.debug("Successfully set slider [{}] value to {}", elementName, value);
        } catch (Exception e) {
            logger.error("Failed to set slider [{}] value: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to set slider " + elementName + " value", e);
        }
        return this;
    }

    /**
     * Get current slider value
     * @return Current slider value
     */
    @Step("Get slider [{elementName}] value")
    public String getValue() {
        logger.info("Getting slider [{}] value", elementName);
        try {
            String value = element.shouldBe(Condition.visible).getValue();
            logger.debug("Slider [{}] current value: {}", elementName, value);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get slider [{}] value: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get slider " + elementName + " value", e);
        }
    }

    /**
     * Move slider by specific number of steps using arrow keys
     * @param steps Number of steps to move (positive for right/up, negative for left/down)
     * @return This Slider for method chaining
     */
    @Step("Move slider [{elementName}] by {steps} steps")
    public Slider moveBySteps(int steps) {
        logger.info("Moving slider [{}] by {} steps", elementName, steps);
        try {
            element.shouldBe(Condition.visible).click(); // Focus the slider first
            
            Keys key = steps > 0 ? Keys.ARROW_RIGHT : Keys.ARROW_LEFT;
            int absoluteSteps = Math.abs(steps);
            
            for (int i = 0; i < absoluteSteps; i++) {
                element.sendKeys(key);
            }
            
            logger.debug("Successfully moved slider [{}] by {} steps", elementName, steps);
        } catch (Exception e) {
            logger.error("Failed to move slider [{}] by steps: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to move slider " + elementName + " by steps", e);
        }
        return this;
    }

    /**
     * Move slider to minimum value
     * @return This Slider for method chaining
     */
    @Step("Move slider [{elementName}] to minimum")
    public Slider moveToMin() {
        logger.info("Moving slider [{}] to minimum", elementName);
        try {
            element.shouldBe(Condition.visible).click();
            element.sendKeys(Keys.HOME);
            logger.debug("Successfully moved slider [{}] to minimum", elementName);
        } catch (Exception e) {
            logger.error("Failed to move slider [{}] to minimum: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to move slider " + elementName + " to minimum", e);
        }
        return this;
    }

    /**
     * Move slider to maximum value
     * @return This Slider for method chaining
     */
    @Step("Move slider [{elementName}] to maximum")
    public Slider moveToMax() {
        logger.info("Moving slider [{}] to maximum", elementName);
        try {
            element.shouldBe(Condition.visible).click();
            element.sendKeys(Keys.END);
            logger.debug("Successfully moved slider [{}] to maximum", elementName);
        } catch (Exception e) {
            logger.error("Failed to move slider [{}] to maximum: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to move slider " + elementName + " to maximum", e);
        }
        return this;
    }

    /**
     * Drag slider to specific position by offset
     * @param xOffset Horizontal offset in pixels
     * @return This Slider for method chaining
     */
    @Step("Drag slider [{elementName}] by offset {xOffset}")
    public Slider dragByOffset(int xOffset) {
        logger.info("Dragging slider [{}] by offset {}", elementName, xOffset);
        try {
            Actions actions = new Actions(Selenide.webdriver().driver().getWebDriver());
            actions.clickAndHold(element.shouldBe(Condition.visible))
                   .moveByOffset(xOffset, 0)
                   .release()
                   .perform();
            logger.debug("Successfully dragged slider [{}] by offset {}", elementName, xOffset);
        } catch (Exception e) {
            logger.error("Failed to drag slider [{}] by offset: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to drag slider " + elementName + " by offset", e);
        }
        return this;
    }

    /**
     * Get minimum value of slider
     * @return Minimum value
     */
    @Step("Get minimum value of slider [{elementName}]")
    public String getMinValue() {
        logger.info("Getting minimum value of slider [{}]", elementName);
        try {
            String minValue = element.getAttribute("min");
            logger.debug("Slider [{}] minimum value: {}", elementName, minValue);
            return minValue != null ? minValue : "0";
        } catch (Exception e) {
            logger.error("Failed to get minimum value of slider [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get minimum value of slider " + elementName, e);
        }
    }

    /**
     * Get maximum value of slider
     * @return Maximum value
     */
    @Step("Get maximum value of slider [{elementName}]")
    public String getMaxValue() {
        logger.info("Getting maximum value of slider [{}]", elementName);
        try {
            String maxValue = element.getAttribute("max");
            logger.debug("Slider [{}] maximum value: {}", elementName, maxValue);
            return maxValue != null ? maxValue : "100";
        } catch (Exception e) {
            logger.error("Failed to get maximum value of slider [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get maximum value of slider " + elementName, e);
        }
    }

    /**
     * Get step value of slider
     * @return Step value
     */
    @Step("Get step value of slider [{elementName}]")
    public String getStepValue() {
        logger.info("Getting step value of slider [{}]", elementName);
        try {
            String stepValue = element.getAttribute("step");
            logger.debug("Slider [{}] step value: {}", elementName, stepValue);
            return stepValue != null ? stepValue : "1";
        } catch (Exception e) {
            logger.error("Failed to get step value of slider [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get step value of slider " + elementName, e);
        }
    }

    /**
     * Move slider to specific percentage of its range
     * @param percentage Percentage (0-100) of the slider range
     * @return This Slider for method chaining
     */
    @Step("Move slider [{elementName}] to {percentage}% of range")
    public Slider moveToPercentage(double percentage) {
        logger.info("Moving slider [{}] to {}% of range", elementName, percentage);
        try {
            if (percentage < 0 || percentage > 100) {
                throw new IllegalArgumentException("Percentage must be between 0 and 100");
            }
            
            double minValue = Double.parseDouble(getMinValue());
            double maxValue = Double.parseDouble(getMaxValue());
            double targetValue = minValue + (maxValue - minValue) * (percentage / 100.0);
            
            setValue(String.valueOf(targetValue));
            logger.debug("Successfully moved slider [{}] to {}% (value: {})", elementName, percentage, targetValue);
        } catch (Exception e) {
            logger.error("Failed to move slider [{}] to percentage: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to move slider " + elementName + " to percentage", e);
        }
        return this;
    }
}