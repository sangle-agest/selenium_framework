package pages;

import core.elements.Button;
import core.elements.TextBox;
import core.elements.BaseElement;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

/**
 * Login Page Object
 * Represents the login page and its elements and actions
 */
public class LoginPage {
    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Page Elements
    private final TextBox usernameField = new TextBox($(By.id("username")), "Username Field");
    private final TextBox passwordField = new TextBox($(By.id("password")), "Password Field");
    private final Button loginButton = new Button($(By.id("login-button")), "Login Button");
    private final Button forgotPasswordLink = new Button($(By.linkText("Forgot Password?")), "Forgot Password Link");
    private final BaseElement loginForm = new BaseElement($(By.className("login-form")), "Login Form");
    private final BaseElement errorMessage = new BaseElement($(By.className("error-message")), "Error Message");
    private final BaseElement successMessage = new BaseElement($(By.className("success-message")), "Success Message");
    private final BaseElement pageTitle = new BaseElement($(By.tagName("h1")), "Page Title");

    // Page URLs and Constants
    public static final String LOGIN_URL = "/login";
    public static final String EXPECTED_TITLE = "Login";
    public static final String EXPECTED_SUCCESS_MESSAGE = "Login successful";

    /**
     * Constructor - validates page is loaded
     */
    public LoginPage() {
        validatePageLoaded();
    }

    /**
     * Validate that login page is properly loaded
     */
    @Step("Validate login page is loaded")
    public LoginPage validatePageLoaded() {
        LogUtils.logPageAction("Validating login page is loaded");
        loginForm.waitForVisible();
        usernameField.waitForVisible();
        passwordField.waitForVisible();
        loginButton.waitForVisible();
        
        logger.info("Login page validation completed successfully");
        return this;
    }

    /**
     * Enter username
     * @param username Username to enter
     * @return LoginPage for method chaining
     */
    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        LogUtils.logTestData("Username", username);
        LogUtils.logPageAction("Entering username");
        
        usernameField.clear();
        usernameField.sendKeys(username);
        
        logger.info("Username entered successfully: {}", username);
        return this;
    }

    /**
     * Enter password
     * @param password Password to enter
     * @return LoginPage for method chaining
     */
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        LogUtils.logTestData("Password", "***masked***");
        LogUtils.logPageAction("Entering password");
        
        passwordField.clear();
        passwordField.sendKeys(password);
        
        logger.info("Password entered successfully");
        return this;
    }

    /**
     * Click login button
     * @return HomePage object (assuming successful login)
     */
    @Step("Click login button")
    public HomePage clickLoginButton() {
        LogUtils.logPageAction("Clicking login button");
        
        loginButton.click();
        
        logger.info("Login button clicked");
        return new HomePage();
    }

    /**
     * Click forgot password link
     * @return ForgotPasswordPage object
     */
    @Step("Click forgot password link")
    public ForgotPasswordPage clickForgotPasswordLink() {
        LogUtils.logPageAction("Clicking forgot password link");
        
        forgotPasswordLink.click();
        
        logger.info("Forgot password link clicked");
        return new ForgotPasswordPage();
    }

    /**
     * Perform complete login action
     * @param username Username
     * @param password Password
     * @return HomePage object
     */
    @Step("Login with username: {username}")
    public HomePage login(String username, String password) {
        LogUtils.logTestStep("Performing login with username: " + username);
        
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    /**
     * Attempt login and expect to stay on login page (for negative tests)
     * @param username Username
     * @param password Password
     * @return LoginPage object
     */
    @Step("Attempt login with invalid credentials - username: {username}")
    public LoginPage attemptLogin(String username, String password) {
        LogUtils.logTestStep("Attempting login with invalid credentials");
        
        enterUsername(username);
        enterPassword(password);
        loginButton.click();
        
        // Wait for error message or stay on same page
        try {
            Thread.sleep(1000); // Small wait for error message to appear
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        logger.info("Login attempt completed (expecting failure)");
        return this;
    }

    /**
     * Get error message text
     * @return Error message text
     */
    @Step("Get error message")
    public String getErrorMessage() {
        LogUtils.logPageAction("Getting error message");
        
        if (errorMessage.isVisible()) {
            String message = errorMessage.getText();
            LogUtils.logTestData("Error Message", message);
            logger.info("Error message retrieved: {}", message);
            return message;
        }
        
        logger.info("No error message found");
        return "";
    }

    /**
     * Get success message text
     * @return Success message text
     */
    @Step("Get success message")
    public String getSuccessMessage() {
        LogUtils.logPageAction("Getting success message");
        
        if (successMessage.isVisible()) {
            String message = successMessage.getText();
            LogUtils.logTestData("Success Message", message);
            logger.info("Success message retrieved: {}", message);
            return message;
        }
        
        logger.info("No success message found");
        return "";
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
     * Check if error message is displayed
     * @return True if error message is visible
     */
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        boolean isDisplayed = errorMessage.isVisible();
        LogUtils.logTestData("Error Message Displayed", isDisplayed);
        logger.info("Error message displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Check if success message is displayed
     * @return True if success message is visible
     */
    @Step("Check if success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        boolean isDisplayed = successMessage.isVisible();
        LogUtils.logTestData("Success Message Displayed", isDisplayed);
        logger.info("Success message displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Check if login button is enabled
     * @return True if login button is enabled
     */
    @Step("Check if login button is enabled")
    public boolean isLoginButtonEnabled() {
        boolean isEnabled = loginButton.isEnabled();
        LogUtils.logTestData("Login Button Enabled", isEnabled);
        logger.info("Login button enabled: {}", isEnabled);
        return isEnabled;
    }

    /**
     * Clear all form fields
     * @return LoginPage for method chaining
     */
    @Step("Clear login form")
    public LoginPage clearForm() {
        LogUtils.logPageAction("Clearing login form");
        
        usernameField.clear();
        passwordField.clear();
        
        logger.info("Login form cleared");
        return this;
    }

    /**
     * Get username field value
     * @return Username field value
     */
    @Step("Get username field value")
    public String getUsernameValue() {
        String value = usernameField.getValue();
        LogUtils.logTestData("Username Field Value", value);
        logger.info("Username field value: {}", value);
        return value;
    }

    /**
     * Get password field value
     * @return Password field value (masked for security)
     */
    @Step("Get password field value")
    public String getPasswordValue() {
        String value = passwordField.getValue();
        LogUtils.logTestData("Password Field Value", "***masked***");
        logger.info("Password field value retrieved (masked)");
        return value;
    }

    /**
     * Wait for page to be ready
     * @return LoginPage for method chaining
     */
    @Step("Wait for login page to be ready")
    public LoginPage waitForPageReady() {
        LogUtils.logPageAction("Waiting for login page to be ready");
        
        loginForm.waitForVisible();
        usernameField.waitForEnabled();
        passwordField.waitForEnabled();
        loginButton.waitForEnabled();
        
        logger.info("Login page is ready");
        return this;
    }
}