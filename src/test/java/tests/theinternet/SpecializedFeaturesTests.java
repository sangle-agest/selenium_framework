package tests.theinternet;

import core.elements.FileUpload;
import core.utils.JavaScriptUtils;
import core.utils.WindowUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseTest;

import java.time.Duration;
import java.util.Set;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Specialized test suite for advanced framework features
 * Tests file upload, JavaScript interactions, and window management
 */
@Epic("Advanced Framework Features")
@Feature("File Upload and JavaScript Interactions")
public class SpecializedFeaturesTests extends BaseTest {

    /**
     * Test file upload functionality
     */
    @Test(groups = {"regression", "fileupload"}, priority = 1)
    @Story("File Upload")
    @Description("Test file upload operations with single and multiple files")
    public void testFileUpload() {
        logStep("Starting file upload test");
        
        // Navigate to file upload page
        open("https://the-internet.herokuapp.com/upload");
        takeScreenshot("File upload page loaded");
        
        // Create file upload element
        FileUpload fileUploader = new FileUpload($("input[type='file']"));
        
        logStep("Testing file upload functionality");
        
        // Test creating and uploading a temporary file
        String fileContent = "This is a test file for upload validation.";
        fileUploader.uploadTemporaryFile("test.txt", fileContent);
        takeScreenshot("Temporary file uploaded");
        
        // Click upload button
        $("input[type='submit']").click();
        sleep(2000);
        takeScreenshot("File upload submitted");
        
        // Verify upload success
        String pageText = $("body").getText();
        Assert.assertTrue(pageText.contains("File Uploaded!") || pageText.contains("test.txt"), 
                         "Upload should be successful");
        
        logStep("File upload test completed successfully");
    }

    /**
     * Test JavaScript utilities
     */
    @Test(groups = {"regression", "javascript"}, priority = 2)
    @Story("JavaScript Interactions")
    @Description("Test JavaScript execution and DOM manipulation utilities")
    public void testJavaScriptInteractions() {
        logStep("Starting JavaScript interactions test");
        
        // Navigate to a test page
        open("https://the-internet.herokuapp.com/login");
        takeScreenshot("Login page for JavaScript testing");
        
        // Test JavaScript execution
        logStep("Testing JavaScript execution");
        
        // Execute simple JavaScript
        Object result = JavaScriptUtils.executeScript("return document.title;");
        logTestData("Page title via JavaScript", result.toString());
        Assert.assertTrue(result.toString().contains("Internet"), "Title should contain 'Internet'");
        
        // Test scrolling
        logStep("Testing JavaScript scrolling");
        JavaScriptUtils.scrollToBottom();
        takeScreenshot("Scrolled to bottom");
        
        JavaScriptUtils.scrollToTop();
        takeScreenshot("Scrolled to top");
        
        // Test element highlighting
        logStep("Testing element highlighting");
        JavaScriptUtils.highlightElement($("input#username"), "red");
        takeScreenshot("Username field highlighted");
        
        // Test scroll into view
        JavaScriptUtils.scrollIntoView($("button[type='submit']"));
        takeScreenshot("Submit button in view");
        
        // Test click via JavaScript
        logStep("Testing JavaScript click");
        JavaScriptUtils.clickElement($("input#username"));
        takeScreenshot("Username field clicked via JavaScript");
        
        logStep("JavaScript interactions test completed successfully");
    }

    /**
     * Test window management utilities
     */
    @Test(groups = {"regression", "windows"}, priority = 3)
    @Story("Window Management")
    @Description("Test window switching and tab management")
    public void testWindowManagement() {
        logStep("Starting window management test");
        
        // Navigate to multiple windows page
        open("https://the-internet.herokuapp.com/windows");
        takeScreenshot("Multiple windows page loaded");
        
        // Get initial window handle
        String originalWindow = WindowUtils.getCurrentWindowHandle();
        logTestData("Original window handle", originalWindow);
        
        // Click to open new window
        $("a[href='/windows/new']").click();
        sleep(2000);
        
        // Test window switching
        logStep("Testing window switching");
        Set<String> allWindows = WindowUtils.getAllWindowHandles();
        logTestData("Total windows", String.valueOf(allWindows.size()));
        Assert.assertTrue(allWindows.size() >= 2, "Should have at least 2 windows");
        
        // Switch to new window
        WindowUtils.switchToNewWindow();
        takeScreenshot("Switched to new window");
        
        // Verify we're in the new window
        String newWindowTitle = title();
        logTestData("New window title", newWindowTitle);
        Assert.assertTrue(newWindowTitle.contains("New Window"), "Should be in new window");
        
        // Test switching back
        logStep("Testing window switching back");
        WindowUtils.switchToWindow(originalWindow);
        takeScreenshot("Switched back to original window");
        
        // Verify we're back in original window
        String backTitle = title();
        Assert.assertTrue(backTitle.contains("Internet"), "Should be back in original window");
        
        // Clean up - close new window
        WindowUtils.switchToNewWindow();
        WindowUtils.closeCurrentWindow();
        WindowUtils.switchToWindow(originalWindow);
        
        logStep("Window management test completed successfully");
    }

    /**
     * Test alert handling utilities
     */
    @Test(groups = {"regression", "alerts"}, priority = 4)
    @Story("Alert Handling")
    @Description("Test JavaScript alert, confirm, and prompt handling")
    public void testAlertHandling() {
        logStep("Starting alert handling test");
        
        // Navigate to JavaScript alerts page
        open("https://the-internet.herokuapp.com/javascript_alerts");
        takeScreenshot("JavaScript alerts page loaded");
        
        // Test simple alert
        logStep("Testing simple alert");
        $("button[onclick='jsAlert()']").click();
        sleep(1000);
        
        String alertText = switchTo().alert().getText();
        logTestData("Alert text", alertText);
        Assert.assertEquals(alertText, "I am a JS Alert", "Alert text should match");
        
        switchTo().alert().accept();
        takeScreenshot("Alert accepted");
        
        // Verify result
        String result = $("p#result").getText();
        Assert.assertTrue(result.contains("successfully"), "Alert should be accepted successfully");
        
        // Test confirm dialog - accept
        logStep("Testing confirm dialog - accept");
        $("button[onclick='jsConfirm()']").click();
        sleep(1000);
        
        switchTo().alert().accept();
        takeScreenshot("Confirm accepted");
        
        result = $("p#result").getText();
        Assert.assertTrue(result.contains("Ok"), "Confirm should be accepted");
        
        // Test confirm dialog - dismiss
        logStep("Testing confirm dialog - dismiss");
        $("button[onclick='jsConfirm()']").click();
        sleep(1000);
        
        switchTo().alert().dismiss();
        takeScreenshot("Confirm dismissed");
        
        result = $("p#result").getText();
        Assert.assertTrue(result.contains("Cancel"), "Confirm should be dismissed");
        
        // Test prompt dialog
        logStep("Testing prompt dialog");
        $("button[onclick='jsPrompt()']").click();
        sleep(1000);
        
        String promptText = "Test Input";
        switchTo().alert().sendKeys(promptText);
        switchTo().alert().accept();
        takeScreenshot("Prompt submitted");
        
        result = $("p#result").getText();
        Assert.assertTrue(result.contains(promptText), "Prompt result should contain input text");
        
        logStep("Alert handling test completed successfully");
    }

    /**
     * Test dynamic content handling
     */
    @Test(groups = {"regression", "dynamic"}, priority = 5)
    @Story("Dynamic Content")
    @Description("Test handling of dynamic and loading content")
    public void testDynamicContentHandling() {
        logStep("Starting dynamic content handling test");
        
        // Navigate to dynamic loading page
        open("https://the-internet.herokuapp.com/dynamic_loading/1");
        takeScreenshot("Dynamic loading page loaded");
        
        // Test waiting for dynamic content
        logStep("Testing dynamic content loading");
        
        // Click start button
        $("button").click();
        takeScreenshot("Loading started");
        
        // Wait for content to appear (using Selenide's built-in waiting)
        $("div#finish").shouldBe(visible, Duration.ofSeconds(10));
        takeScreenshot("Content loaded");
        
        // Verify content
        String loadedText = $("div#finish h4").getText();
        logTestData("Loaded text", loadedText);
        Assert.assertEquals(loadedText, "Hello World!", "Loaded content should be 'Hello World!'");
        
        // Test another dynamic loading scenario
        logStep("Testing another dynamic loading scenario");
        open("https://the-internet.herokuapp.com/dynamic_loading/2");
        takeScreenshot("Second dynamic loading page");
        
        $("button").click();
        takeScreenshot("Second loading started");
        
        $("div#finish").shouldBe(visible, Duration.ofSeconds(10));
        takeScreenshot("Second content loaded");
        
        loadedText = $("div#finish h4").getText();
        Assert.assertEquals(loadedText, "Hello World!", "Second loaded content should be 'Hello World!'");
        
        logStep("Dynamic content handling test completed successfully");
    }

    /**
     * Test iframe interactions
     */
    @Test(groups = {"regression", "iframe"}, priority = 6)
    @Story("iFrame Interactions")
    @Description("Test switching to iframes and interacting with content")
    public void testIFrameInteractions() {
        logStep("Starting iframe interactions test");
        
        // Navigate to iframe page
        open("https://the-internet.herokuapp.com/iframe");
        takeScreenshot("iFrame page loaded");
        
        // Switch to iframe
        logStep("Testing iframe switching");
        switchTo().frame("mce_0_ifr");
        takeScreenshot("Switched to iframe");
        
        // Interact with iframe content
        $("body").clear();
        $("body").type("Testing iframe content input");
        takeScreenshot("Text entered in iframe");
        
        // Verify content
        String iframeText = $("body").getText();
        Assert.assertTrue(iframeText.contains("Testing iframe"), "iFrame should contain entered text");
        
        // Switch back to main content
        switchTo().defaultContent();
        takeScreenshot("Switched back to main content");
        
        // Verify we're back in main content
        String mainTitle = $("h3").getText();
        Assert.assertEquals(mainTitle, "An iFrame containing the TinyMCE WYSIWYG Editor", 
                           "Should be back in main content");
        
        logStep("iFrame interactions test completed successfully");
    }
}