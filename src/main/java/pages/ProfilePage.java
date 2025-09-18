package pages;

import core.elements.BaseElement;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

/**
 * Profile Page Object
 * Represents the user profile page
 */
public class ProfilePage {
    private final Logger logger = LoggerFactory.getLogger(ProfilePage.class);

    // Page Elements
    private final BaseElement profileForm = new BaseElement($(By.className("profile-form")), "Profile Form");
    private final BaseElement pageTitle = new BaseElement($(By.tagName("h1")), "Page Title");

    // Page Constants
    public static final String PROFILE_URL = "/profile";
    public static final String EXPECTED_TITLE = "User Profile";

    /**
     * Constructor - validates page is loaded
     */
    public ProfilePage() {
        validatePageLoaded();
    }

    /**
     * Validate that profile page is properly loaded
     */
    @Step("Validate profile page is loaded")
    public ProfilePage validatePageLoaded() {
        LogUtils.logPageAction("Validating profile page is loaded");
        profileForm.waitForVisible();
        
        logger.info("Profile page validation completed successfully");
        return this;
    }

    /**
     * Get page title
     * @return Page title text
     */
    @Step("Get page title")
    public String getPageTitle() {
        LogUtils.logPageAction("Getting page title");
        
        String title = pageTitle.getText();
        LogUtils.logTestData("Page Title", title);
        logger.info("Page title retrieved: {}", title);
        return title;
    }
}