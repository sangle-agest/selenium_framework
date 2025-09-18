package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.HomePage;
import pages.ForgotPasswordPage;

import static com.codeborne.selenide.Selenide.open;

/**
 * Sample Login Tests
 * Demonstrates the automation framework capabilities
 */
@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTests extends BaseTest {

    /**
     * Test successful login with valid credentials
     */
    @Test(groups = {"smoke", "regression"}, priority = 1)
    @Story("Valid Login")
    @Description("Test login with valid username and password")
    public void testValidLogin() {
        logStep("Starting valid login test");
        
        // Navigate to application
        String baseUrl = config.getBaseUrl();
        logTestData("Base URL", baseUrl);
        open(baseUrl + LoginPage.LOGIN_URL);
        
        // Create page object and perform login
        LoginPage loginPage = new LoginPage();
        takeScreenshot("Login page loaded");
        
        // Verify page title
        String actualTitle = loginPage.getPageTitle();
        logAssertion("Login page title matches expected", LoginPage.EXPECTED_TITLE.equals(actualTitle));
        Assert.assertEquals(actualTitle, LoginPage.EXPECTED_TITLE, "Login page title should match expected");
        
        // Perform login
        String username = getTestParameter("valid.username", "testuser");
        String password = getTestParameter("valid.password", "testpass");
        
        HomePage homePage = loginPage.login(username, password);
        takeScreenshot("Login completed");
        
        // Verify successful login
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in after valid credentials");
        
        String welcomeMessage = homePage.getWelcomeMessage();
        logTestData("Welcome Message", welcomeMessage);
        Assert.assertFalse(welcomeMessage.isEmpty(), "Welcome message should not be empty");
        
        logStep("Valid login test completed successfully");
    }

    /**
     * Test login with invalid credentials
     */
    @Test(groups = {"negative", "regression"}, priority = 2)
    @Story("Invalid Login")
    @Description("Test login with invalid username and password")
    public void testInvalidLogin() {
        logStep("Starting invalid login test");
        
        // Navigate to application
        String baseUrl = config.getBaseUrl();
        open(baseUrl + LoginPage.LOGIN_URL);
        
        // Create page object and attempt login with invalid credentials
        LoginPage loginPage = new LoginPage();
        takeScreenshot("Login page loaded for invalid test");
        
        String invalidUsername = "invaliduser";
        String invalidPassword = "invalidpass";
        
        loginPage.attemptLogin(invalidUsername, invalidPassword);
        takeScreenshot("Invalid login attempted");
        
        // Verify login failed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid login");
        
        String errorMessage = loginPage.getErrorMessage();
        logTestData("Error Message", errorMessage);
        Assert.assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        
        logStep("Invalid login test completed successfully");
    }

    /**
     * Test empty username and password
     */
    @Test(groups = {"negative", "smoke"}, priority = 3)
    @Story("Empty Credentials")
    @Description("Test login with empty username and password fields")
    public void testEmptyCredentials() {
        logStep("Starting empty credentials test");
        
        // Navigate to application
        String baseUrl = config.getBaseUrl();
        open(baseUrl + LoginPage.LOGIN_URL);
        
        // Create page object
        LoginPage loginPage = new LoginPage();
        takeScreenshot("Login page loaded for empty credentials test");
        
        // Attempt login with empty credentials
        loginPage.attemptLogin("", "");
        takeScreenshot("Empty credentials login attempted");
        
        // Verify appropriate handling of empty credentials
        // This could be error message or disabled button based on application behavior
        boolean hasError = loginPage.isErrorMessageDisplayed();
        boolean buttonEnabled = loginPage.isLoginButtonEnabled();
        
        logTestData("Has Error Message", hasError);
        logTestData("Login Button Enabled", buttonEnabled);
        
        // At least one of these should be true (either error shown or button disabled)
        Assert.assertTrue(hasError || !buttonEnabled, 
            "Either error message should be shown or login button should be disabled for empty credentials");
        
        logStep("Empty credentials test completed successfully");
    }

    /**
     * Test forgot password functionality
     */
    @Test(groups = {"functional", "regression"}, priority = 4)
    @Story("Forgot Password")
    @Description("Test forgot password link functionality")
    public void testForgotPasswordLink() {
        logStep("Starting forgot password link test");
        
        // Navigate to application
        String baseUrl = config.getBaseUrl();
        open(baseUrl + LoginPage.LOGIN_URL);
        
        // Create page object and click forgot password link
        LoginPage loginPage = new LoginPage();
        takeScreenshot("Login page loaded for forgot password test");
        
        ForgotPasswordPage forgotPasswordPage = loginPage.clickForgotPasswordLink();
        takeScreenshot("Forgot password page loaded");
        
        // Verify forgot password page
        String actualTitle = forgotPasswordPage.getPageTitle();
        logAssertion("Forgot password page title matches expected", 
            ForgotPasswordPage.EXPECTED_TITLE.equals(actualTitle));
        Assert.assertEquals(actualTitle, ForgotPasswordPage.EXPECTED_TITLE, 
            "Forgot password page title should match expected");
        
        // Test password reset with valid email
        String testEmail = getTestParameter("test.email", "test@example.com");
        forgotPasswordPage.resetPassword(testEmail);
        takeScreenshot("Password reset initiated");
        
        // Verify reset was initiated (could be success message or confirmation)
        boolean hasSuccessMessage = forgotPasswordPage.isSuccessMessageDisplayed();
        boolean hasErrorMessage = forgotPasswordPage.isErrorMessageDisplayed();
        
        logTestData("Has Success Message", hasSuccessMessage);
        logTestData("Has Error Message", hasErrorMessage);
        
        // Either success or error message should be displayed
        Assert.assertTrue(hasSuccessMessage || hasErrorMessage, 
            "Either success or error message should be displayed after password reset attempt");
        
        logStep("Forgot password link test completed successfully");
    }

    /**
     * Test login page form validation
     */
    @Test(groups = {"functional"}, priority = 5)
    @Story("Form Validation")
    @Description("Test login form field validation and behavior")
    public void testLoginFormValidation() {
        logStep("Starting login form validation test");
        
        // Navigate to application
        String baseUrl = config.getBaseUrl();
        open(baseUrl + LoginPage.LOGIN_URL);
        
        // Create page object
        LoginPage loginPage = new LoginPage();
        takeScreenshot("Login page loaded for form validation test");
        
        // Test form clearing
        loginPage.enterUsername("testuser");
        loginPage.enterPassword("testpass");
        takeScreenshot("Form filled");
        
        // Verify values were entered
        String usernameValue = loginPage.getUsernameValue();
        String passwordValue = loginPage.getPasswordValue();
        
        logTestData("Username Value", usernameValue);
        logTestData("Password Value Length", passwordValue.length());
        
        Assert.assertEquals(usernameValue, "testuser", "Username should be entered correctly");
        Assert.assertTrue(passwordValue.length() > 0, "Password should be entered");
        
        // Test form clearing
        loginPage.clearForm();
        takeScreenshot("Form cleared");
        
        // Verify form was cleared
        String clearedUsername = loginPage.getUsernameValue();
        String clearedPassword = loginPage.getPasswordValue();
        
        logTestData("Cleared Username", clearedUsername);
        logTestData("Cleared Password Length", clearedPassword.length());
        
        Assert.assertTrue(clearedUsername.isEmpty(), "Username field should be empty after clearing");
        Assert.assertTrue(clearedPassword.isEmpty(), "Password field should be empty after clearing");
        
        logStep("Login form validation test completed successfully");
    }

    /**
     * Test login and logout flow
     */
    @Test(groups = {"smoke", "regression"}, priority = 6, dependsOnMethods = {"testValidLogin"})
    @Story("Login-Logout Flow")
    @Description("Test complete login and logout flow")
    public void testLoginLogoutFlow() {
        logStep("Starting login-logout flow test");
        
        // Navigate to application
        String baseUrl = config.getBaseUrl();
        open(baseUrl + LoginPage.LOGIN_URL);
        
        // Login
        LoginPage loginPage = new LoginPage();
        String username = getTestParameter("valid.username", "testuser");
        String password = getTestParameter("valid.password", "testpass");
        
        HomePage homePage = loginPage.login(username, password);
        takeScreenshot("Logged in successfully");
        
        // Verify login
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in");
        Assert.assertTrue(homePage.isLogoutButtonAvailable(), "Logout button should be available");
        
        // Logout
        LoginPage returnedLoginPage = homePage.logout();
        takeScreenshot("Logged out successfully");
        
        // Verify logout (should be back on login page)
        String loginPageTitle = returnedLoginPage.getPageTitle();
        logAssertion("Returned to login page after logout", 
            LoginPage.EXPECTED_TITLE.equals(loginPageTitle));
        Assert.assertEquals(loginPageTitle, LoginPage.EXPECTED_TITLE, 
            "Should return to login page after logout");
        
        logStep("Login-logout flow test completed successfully");
    }
}