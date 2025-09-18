package pages;

import core.elements.BaseElement;
import core.elements.Button;
import core.elements.TextBox;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

/**
 * Forgot Password Page Object
 * Represents the forgot password page for password recovery
 */
public class ForgotPasswordPage {
    private final Logger logger = LoggerFactory.getLogger(ForgotPasswordPage.class);

    // Page Elements
    private final TextBox emailField = new TextBox($(By.id("email")), "Email Field");
    private final Button resetButton = new Button($(By.id("reset-button")), "Reset Password Button");
    private final Button backToLoginButton = new Button($(By.linkText("Back to Login")), "Back to Login Button");
    private final BaseElement forgotPasswordForm = new BaseElement($(By.className("forgot-password-form")), "Forgot Password Form");
    private final BaseElement successMessage = new BaseElement($(By.className("success-message")), "Success Message");
    private final BaseElement errorMessage = new BaseElement($(By.className("error-message")), "Error Message");
    private final BaseElement pageTitle = new BaseElement($(By.tagName("h1")), "Page Title");

    // Page Constants
    public static final String FORGOT_PASSWORD_URL = "/forgot-password";
    public static final String EXPECTED_TITLE = "Forgot Password";

    /**
     * Constructor - validates page is loaded
     */
    public ForgotPasswordPage() {
        validatePageLoaded();
    }

    /**
     * Validate that forgot password page is properly loaded
     */
    @Step("Validate forgot password page is loaded")
    public ForgotPasswordPage validatePageLoaded() {
        LogUtils.logPageAction("Validating forgot password page is loaded");
        forgotPasswordForm.waitForVisible();
        emailField.waitForVisible();
        resetButton.waitForVisible();
        
        logger.info("Forgot password page validation completed successfully");
        return this;
    }

    /**
     * Enter email address
     * @param email Email address to enter
     * @return ForgotPasswordPage for method chaining
     */
    @Step("Enter email: {email}")
    public ForgotPasswordPage enterEmail(String email) {
        LogUtils.logTestData("Email", email);
        LogUtils.logPageAction("Entering email address");
        
        emailField.clear();
        emailField.sendKeys(email);
        
        logger.info("Email entered successfully: {}", email);
        return this;
    }

    /**
     * Click reset password button
     * @return ForgotPasswordPage for method chaining (stays on same page)
     */
    @Step("Click reset password button")
    public ForgotPasswordPage clickResetButton() {
        LogUtils.logPageAction("Clicking reset password button");
        
        resetButton.click();
        
        logger.info("Reset password button clicked");
        return this;
    }

    /**
     * Click back to login button
     * @return LoginPage object
     */
    @Step("Click back to login button")
    public LoginPage clickBackToLogin() {
        LogUtils.logPageAction("Clicking back to login button");
        
        backToLoginButton.click();
        
        logger.info("Back to login button clicked");
        return new LoginPage();
    }

    /**
     * Perform complete password reset action
     * @param email Email address
     * @return ForgotPasswordPage for method chaining
     */
    @Step("Reset password for email: {email}")
    public ForgotPasswordPage resetPassword(String email) {
        LogUtils.logTestStep("Performing password reset for email: " + email);
        
        enterEmail(email);
        clickResetButton();
        
        logger.info("Password reset initiated for email: {}", email);
        return this;
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
     * Clear email field
     * @return ForgotPasswordPage for method chaining
     */
    @Step("Clear email field")
    public ForgotPasswordPage clearEmailField() {
        LogUtils.logPageAction("Clearing email field");
        
        emailField.clear();
        
        logger.info("Email field cleared");
        return this;
    }

    /**
     * Get email field value
     * @return Email field value
     */
    @Step("Get email field value")
    public String getEmailValue() {
        String value = emailField.getValue();
        LogUtils.logTestData("Email Field Value", value);
        logger.info("Email field value: {}", value);
        return value;
    }

    /**
     * Check if reset button is enabled
     * @return True if reset button is enabled
     */
    @Step("Check if reset button is enabled")
    public boolean isResetButtonEnabled() {
        boolean isEnabled = resetButton.isEnabled();
        LogUtils.logTestData("Reset Button Enabled", isEnabled);
        logger.info("Reset button enabled: {}", isEnabled);
        return isEnabled;
    }

    /**
     * Wait for page to be ready
     * @return ForgotPasswordPage for method chaining
     */
    @Step("Wait for forgot password page to be ready")
    public ForgotPasswordPage waitForPageReady() {
        LogUtils.logPageAction("Waiting for forgot password page to be ready");
        
        forgotPasswordForm.waitForVisible();
        emailField.waitForVisible();
        resetButton.waitForVisible();
        
        logger.info("Forgot password page is ready");
        return this;
    }
}