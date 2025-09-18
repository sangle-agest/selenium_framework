package core.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

/**
 * Link element wrapper extending BaseElement
 */
public class Link extends BaseElement {

    /**
     * Constructor for Link element
     * @param element SelenideElement to wrap
     * @param elementName Name for logging and reporting
     */
    public Link(SelenideElement element, String elementName) {
        super(element, elementName);
    }

    /**
     * Constructor for Link element with default name
     * @param element SelenideElement to wrap
     */
    public Link(SelenideElement element) {
        super(element, "Link");
    }

    /**
     * Click the link
     * @return This Link for method chaining
     */
    @Override
    @Step("Click link [{elementName}]")
    public Link click() {
        logger.info("Clicking link [{}]", elementName);
        super.click();
        return this;
    }

    /**
     * Get href attribute of the link
     * @return Href attribute value
     */
    @Step("Get href from link [{elementName}]")
    public String getHref() {
        logger.info("Getting href from link [{}]", elementName);
        try {
            String href = element.shouldBe(Condition.visible).getAttribute("href");
            logger.debug("Href from link [{}]: '{}'", elementName, href);
            return href;
        } catch (Exception e) {
            logger.error("Failed to get href from link [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get href from " + elementName, e);
        }
    }

    /**
     * Get target attribute of the link
     * @return Target attribute value
     */
    @Step("Get target from link [{elementName}]")
    public String getTarget() {
        logger.info("Getting target from link [{}]", elementName);
        try {
            String target = element.shouldBe(Condition.visible).getAttribute("target");
            logger.debug("Target from link [{}]: '{}'", elementName, target);
            return target;
        } catch (Exception e) {
            logger.error("Failed to get target from link [{}]: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get target from " + elementName, e);
        }
    }

    /**
     * Check if link opens in new window/tab
     * @return True if opens in new window, false otherwise
     */
    @Step("Check if link [{elementName}] opens in new window")
    public boolean opensInNewWindow() {
        String target = getTarget();
        boolean newWindow = "_blank".equals(target);
        logger.debug("Link [{}] opens in new window: {}", elementName, newWindow);
        return newWindow;
    }
}