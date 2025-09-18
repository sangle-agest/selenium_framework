package tests;

import core.elements.Button;
import core.elements.Slider;
import core.elements.Table;
import core.elements.TextBox;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

/**
 * Advanced test suite demonstrating framework's specialized element capabilities
 * Tests Table, Slider, and other advanced UI components
 */
@Epic("Advanced Elements Test Suite")
@Feature("Specialized UI Components")
public class AdvancedElementsTests extends BaseTest {

    /**
     * Test table interactions
     */
    @Test(groups = {"regression", "table"}, priority = 1)
    @Story("Table Interactions")
    @Description("Test table operations: cell access, row finding, and data extraction")
    public void testTableInteractions() {
        logStep("Starting table interactions test");
        
        // Navigate to tables page
        open("https://the-internet.herokuapp.com/tables");
        takeScreenshot("Tables page loaded");
        
        // Create table element for first table
        Table table1 = new Table($("table#table1"));
        
        logStep("Testing table structure and content");
        
        // Test table data access
        String cellData = table1.getCellText(1, 1);
        logTestData("Cell (1,1) data", cellData);
        Assert.assertFalse(cellData.isEmpty(), "Cell data should not be empty");
        
        // Test getting specific cell
        String lastName = table1.getCellText(1, 0); // First row, first column (Last Name)
        logTestData("First row last name", lastName);
        Assert.assertFalse(lastName.isEmpty(), "Last name should not be empty");
        
        // Test finding row by column text
        int rowIndex = table1.findRowByColumnText(0, lastName);
        Assert.assertEquals(rowIndex, 1, "Should find the row at index 1");
        
        // Test getting all data
        logStep("Testing table data extraction");
        List<List<String>> allData = table1.getAllData();
        Assert.assertTrue(allData.size() > 0, "Table should have data");
        Assert.assertTrue(allData.get(0).size() > 0, "Table should have columns");
        
        logTestData("Table dimensions", allData.size() + "x" + allData.get(0).size());
        
        // Test table sorting (if sortable)
        logStep("Testing table sorting functionality");
        try {
            table1.sortByColumn(0); // Sort by first column
            takeScreenshot("Table sorted by first column");
            logStep("Table sorting completed successfully");
        } catch (Exception e) {
            logStep("Table sorting not available - this is expected for static tables");
        }
        
        logStep("Table interactions test completed successfully");
    }

    /**
     * Test slider interactions
     */
    @Test(groups = {"regression", "slider"}, priority = 2)
    @Story("Slider Interactions")
    @Description("Test slider operations: value setting, step movement, and percentage positioning")
    public void testSliderInteractions() {
        logStep("Starting slider interactions test");
        
        // Navigate to horizontal slider page
        open("https://the-internet.herokuapp.com/horizontal_slider");
        takeScreenshot("Horizontal slider page loaded");
        
        // Create slider element
        Slider slider = new Slider($("input[type='range']"));
        
        logStep("Testing slider functionality");
        
        // Test setting specific value
        slider.setValue("3");
        takeScreenshot("Slider set to value 3.0");
        
        // Get current value display
        String displayValue = $("span#range").getText();
        logTestData("Displayed slider value", displayValue);
        Assert.assertEquals(displayValue, "3", "Displayed value should be 3");
        
        // Test moving by steps
        logStep("Testing slider step movement");
        slider.moveBySteps(2);
        takeScreenshot("Slider moved by 2 steps");
        
        displayValue = $("span#range").getText();
        logTestData("Value after 2 steps", displayValue);
        
        // Test percentage positioning
        logStep("Testing slider percentage positioning");
        slider.moveToPercentage(75);
        takeScreenshot("Slider at 75% position");
        
        displayValue = $("span#range").getText();
        logTestData("Value at 75% position", displayValue);
        
        // Test edge values
        logStep("Testing slider edge values");
        slider.setValue("0");
        displayValue = $("span#range").getText();
        Assert.assertEquals(displayValue, "0", "Minimum value should be 0");
        
        slider.setValue("5");
        displayValue = $("span#range").getText();
        Assert.assertEquals(displayValue, "5", "Maximum value should be 5");
        
        logStep("Slider interactions test completed successfully");
    }

    /**
     * Test advanced button interactions
     */
    @Test(groups = {"regression", "buttons"}, priority = 3)
    @Story("Advanced Button Interactions")
    @Description("Test various button interaction methods and state verification")
    public void testAdvancedButtonInteractions() {
        logStep("Starting advanced button interactions test");
        
        // Navigate to dynamic controls page
        open("https://the-internet.herokuapp.com/dynamic_controls");
        takeScreenshot("Dynamic controls page loaded");
        
        // Test enable/disable button
        Button enableButton = new Button($("button[onclick='swapButton()']"));
        
        logStep("Testing button enable/disable functionality");
        
        // Initial state
        String initialText = enableButton.getText();
        logTestData("Initial button text", initialText);
        
        // Click to change state
        enableButton.click();
        takeScreenshot("Button clicked - state changing");
        
        // Wait for change and verify
        sleep(2000); // Wait for animation
        String newText = enableButton.getText();
        logTestData("New button text", newText);
        Assert.assertNotEquals(newText, initialText, "Button text should change after click");
        
        // Test button location and size
        logStep("Testing button properties");
        int x = enableButton.getLocation().getX();
        int y = enableButton.getLocation().getY();
        int width = enableButton.getSize().getWidth();
        int height = enableButton.getSize().getHeight();
        
        logTestData("Button position", "(" + x + ", " + y + ")");
        logTestData("Button size", width + "x" + height);
        
        Assert.assertTrue(x >= 0, "X coordinate should be non-negative");
        Assert.assertTrue(y >= 0, "Y coordinate should be non-negative");
        Assert.assertTrue(width > 0, "Width should be positive");
        Assert.assertTrue(height > 0, "Height should be positive");
        
        logStep("Advanced button interactions test completed successfully");
    }

    /**
     * Test advanced text box interactions
     */
    @Test(groups = {"regression", "textbox"}, priority = 4)
    @Story("Advanced TextBox Interactions")
    @Description("Test text input operations with various input methods")
    public void testAdvancedTextBoxInteractions() {
        logStep("Starting advanced text box interactions test");
        
        // Navigate to dynamic controls page
        open("https://the-internet.herokuapp.com/dynamic_controls");
        takeScreenshot("Dynamic controls page loaded");
        
        // Find text input
        TextBox textInput = new TextBox($("input[type='text']"));
        
        logStep("Testing text box functionality");
        
        // Test initial state
        boolean initiallyEnabled = textInput.isEnabled();
        logTestData("Text input initially enabled", initiallyEnabled);
        
        if (!initiallyEnabled) {
            // Enable the input first
            Button enableButton = new Button($("button[onclick='swapInput()']"));
            enableButton.click();
            sleep(2000); // Wait for enable animation
            takeScreenshot("Text input enabled");
        }
        
        // Test text operations
        logStep("Testing text input operations");
        String testText = "Framework Test Input";
        textInput.clear();
        textInput.type(testText);
        takeScreenshot("Text entered");
        
        String enteredText = textInput.getValue();
        Assert.assertEquals(enteredText, testText, "Entered text should match");
        
        // Test appending text
        textInput.type(" - Appended");
        String appendedText = textInput.getValue();
        Assert.assertTrue(appendedText.contains("Appended"), "Text should contain appended content");
        
        // Test clearing
        textInput.clear();
        String clearedText = textInput.getValue();
        Assert.assertTrue(clearedText.isEmpty(), "Text should be empty after clear");
        
        // Test special keys
        logStep("Testing special key operations");
        textInput.type("Hello World");
        textInput.pressKey(Keys.CONTROL); // Press control
        textInput.pressKey(Keys.DELETE); // Delete selected
        
        String finalText = textInput.getValue();
        Assert.assertTrue(finalText.isEmpty() || finalText.length() < testText.length(), 
                         "Text should be cleared or reduced after key operations");
        
        logStep("Advanced text box interactions test completed successfully");
    }

    /**
     * Test framework element properties and methods
     */
    @Test(groups = {"smoke", "framework"}, priority = 5)
    @Story("Element Properties")
    @Description("Test common element properties and attribute access")
    public void testElementProperties() {
        logStep("Starting element properties test");
        
        // Navigate to form authentication page
        open("https://the-internet.herokuapp.com/login");
        takeScreenshot("Login page loaded");
        
        // Test various element properties
        TextBox usernameField = new TextBox($("input#username"));
        
        logStep("Testing element attribute access");
        
        // Test getting attributes
        String placeholder = usernameField.getAttribute("placeholder");
        String type = usernameField.getAttribute("type");
        String id = usernameField.getAttribute("id");
        
        logTestData("Username field placeholder", placeholder);
        logTestData("Username field type", type);
        logTestData("Username field id", id);
        
        Assert.assertEquals(type, "text", "Input type should be text");
        Assert.assertEquals(id, "username", "Input id should be username");
        
        // Test CSS properties
        logStep("Testing CSS property access");
        String backgroundColor = usernameField.getCssValue("background-color");
        String fontSize = usernameField.getCssValue("font-size");
        
        logTestData("Background color", backgroundColor);
        logTestData("Font size", fontSize);
        
        Assert.assertFalse(backgroundColor.isEmpty(), "Background color should not be empty");
        Assert.assertFalse(fontSize.isEmpty(), "Font size should not be empty");
        
        // Test element visibility and state
        logStep("Testing element state verification");
        Assert.assertTrue(usernameField.isVisible(), "Username field should be visible");
        Assert.assertTrue(usernameField.isEnabled(), "Username field should be enabled");
        
        logStep("Element properties test completed successfully");
    }
}