package pages.theinternet;

import core.elements.BaseElement;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

/**
 * Base page for The Internet website
 * Contains common elements and methods for all pages
 */
public abstract class BaseInternetPage {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    // Common elements
    protected final BaseElement pageTitle = new BaseElement($(By.tagName("h3")), "Page Title");
    protected final BaseElement backToMainLink = new BaseElement($(By.linkText("Elemental Selenium")), "Back to Main Link");
    
    // Base URL
    public static final String BASE_URL = "https://the-internet.herokuapp.com";

    /**
     * Get page title text
     * @return Page title text
     */
    @Step("Get page title")
    public String getPageTitle() {
        LogUtils.logPageAction("Getting page title");
        String title = pageTitle.getText();
        LogUtils.logTestData("Page Title", title);
        logger.info("Page title: {}", title);
        return title;
    }

    /**
     * Navigate to main page
     */
    @Step("Navigate to main page")
    public void navigateToMain() {
        LogUtils.logPageAction("Navigating to main page");
        if (backToMainLink.isVisible()) {
            backToMainLink.click();
        }
        logger.info("Navigated to main page");
    }

    /**
     * Validate page is loaded by checking if title is visible
     */
    @Step("Validate page is loaded")
    public void validatePageLoaded() {
        LogUtils.logPageAction("Validating page is loaded");
        pageTitle.waitForVisible();
        logger.info("Page validation completed");
    }
}