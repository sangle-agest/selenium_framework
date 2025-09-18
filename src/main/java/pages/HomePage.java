package pages;

import core.elements.BaseElement;
import core.elements.Button;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

/**
 * Home Page Object
 * Represents the main application page after successful login
 */
public class HomePage {
    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    // Page Elements
    private final BaseElement welcomeMessage = new BaseElement($(By.className("welcome-message")), "Welcome Message");
    private final BaseElement userMenu = new BaseElement($(By.className("user-menu")), "User Menu");
    private final Button logoutButton = new Button($(By.id("logout-button")), "Logout Button");
    private final Button profileButton = new Button($(By.id("profile-button")), "Profile Button");
    private final BaseElement pageTitle = new BaseElement($(By.tagName("h1")), "Page Title");
    private final BaseElement navigationMenu = new BaseElement($(By.className("navigation")), "Navigation Menu");

    // Page Constants
    public static final String HOME_URL = "/home";
    public static final String EXPECTED_TITLE = "Home";

    /**
     * Constructor - validates page is loaded
     */
    public HomePage() {
        validatePageLoaded();
    }

    /**
     * Validate that home page is properly loaded
     */
    @Step("Validate home page is loaded")
    public HomePage validatePageLoaded() {
        LogUtils.logPageAction("Validating home page is loaded");
        welcomeMessage.waitForVisible();
        userMenu.waitForVisible();
        logoutButton.waitForVisible();
        
        logger.info("Home page validation completed successfully");
        return this;
    }

    /**
     * Get welcome message text
     * @return Welcome message text
     */
    @Step("Get welcome message")
    public String getWelcomeMessage() {
        LogUtils.logPageAction("Getting welcome message");
        
        String message = welcomeMessage.getText();
        LogUtils.logTestData("Welcome Message", message);
        logger.info("Welcome message retrieved: {}", message);
        return message;
    }

    /**
     * Click logout button
     * @return LoginPage object
     */
    @Step("Click logout button")
    public LoginPage logout() {
        LogUtils.logPageAction("Clicking logout button");
        
        logoutButton.click();
        
        logger.info("Logout button clicked");
        return new LoginPage();
    }

    /**
     * Click profile button
     * @return ProfilePage object
     */
    @Step("Click profile button")
    public ProfilePage openProfile() {
        LogUtils.logPageAction("Clicking profile button");
        
        profileButton.click();
        
        logger.info("Profile button clicked");
        return new ProfilePage();
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

    /**
     * Check if user is logged in (welcome message visible)
     * @return True if user is logged in
     */
    @Step("Check if user is logged in")
    public boolean isUserLoggedIn() {
        boolean isLoggedIn = welcomeMessage.isVisible() && userMenu.isVisible();
        LogUtils.logTestData("User Logged In", isLoggedIn);
        logger.info("User logged in status: {}", isLoggedIn);
        return isLoggedIn;
    }

    /**
     * Check if logout button is available
     * @return True if logout button is visible and enabled
     */
    @Step("Check if logout button is available")
    public boolean isLogoutButtonAvailable() {
        boolean isAvailable = logoutButton.isVisible() && logoutButton.isEnabled();
        LogUtils.logTestData("Logout Button Available", isAvailable);
        logger.info("Logout button available: {}", isAvailable);
        return isAvailable;
    }

    /**
     * Wait for page to be ready
     * @return HomePage for method chaining
     */
    @Step("Wait for home page to be ready")
    public HomePage waitForPageReady() {
        LogUtils.logPageAction("Waiting for home page to be ready");
        
        welcomeMessage.waitForVisible();
        userMenu.waitForVisible();
        logoutButton.waitForEnabled();
        
        logger.info("Home page is ready");
        return this;
    }
}