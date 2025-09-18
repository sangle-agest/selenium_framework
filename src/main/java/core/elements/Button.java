package core.elements;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

/**
 * Button element wrapper extending BaseElement
 */
public class Button extends BaseElement {

    /**
     * Constructor for Button element
     * @param element SelenideElement to wrap
     * @param elementName Name for logging and reporting
     */
    public Button(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    /**
     * Constructor for Button element with default name
     * @param element SelenideElement to wrap
     */
    public Button(SelenideElement element) {
        super(element, "Button");
    }

    /**
     * Click the button
     * @return This Button for method chaining
     */
    @Override
    @Step("Click button [{elementName}]")
    public Button click() {
        logger.info("Clicking button [{}]", elementName);
        super.click();
        return this;
    }

    /**
     * Submit the button (for submit type buttons)
     * @return This Button for method chaining
     */
    @Step("Submit button [{elementName}]")
    public Button submit() {
        logger.info("Submitting button [{}]", elementName);
        try {
            element.submit();
            logger.debug("Successfully submitted button [{}]", elementName);
        } catch (Exception e) {
            logger.error("Failed to submit button [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to submit button " + elementName, e);
        }
        return this;
    }
}