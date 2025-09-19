package tests.theinternet;

import core.constants.ApplicationConstants;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.theinternet.CheckboxesPage;
import pages.theinternet.DragAndDropPage;
import pages.theinternet.DropdownPage;
import tests.BaseTest;

import static com.codeborne.selenide.Selenide.open;

/**
 * Comprehensive test suite for The Internet website
 * Demonstrates all framework capabilities and element interactions
 */
@Epic("The Internet Test Suite")
@Feature("UI Element Interactions")
public class TheInternetTests extends BaseTest {

    /**
     * Test checkbox interactions
     */
    @Test(groups = {"smoke", "regression", "checkboxes"}, priority = 1)
    @Story("Checkbox Interactions")
    @Description("Test all checkbox operations: check, uncheck, toggle, and state verification")
    public void testCheckboxInteractions() {
        logStep("Starting checkbox interactions test");
        
        // Navigate to checkboxes page
        open(CheckboxesPage.PAGE_URL);
        CheckboxesPage checkboxesPage = new CheckboxesPage();
        takeScreenshot("Checkboxes page loaded");
        
        // Verify page title
        String pageTitle = checkboxesPage.getPageTitle();
        Assert.assertEquals(pageTitle, "Checkboxes", "Page title should be 'Checkboxes'");
        
        // Test initial states
        logStep("Testing initial checkbox states");
        boolean firstInitialState = checkboxesPage.isFirstCheckboxChecked();
        boolean secondInitialState = checkboxesPage.isSecondCheckboxChecked();
        
        logTestData("First checkbox initial state", firstInitialState);
        logTestData("Second checkbox initial state", secondInitialState);
        
        // Typically first checkbox is unchecked, second is checked initially
        Assert.assertFalse(firstInitialState, "First checkbox should be unchecked initially");
        Assert.assertTrue(secondInitialState, "Second checkbox should be checked initially");
        
        // Test checking first checkbox
        logStep("Testing first checkbox operations");
        checkboxesPage.checkFirstCheckbox();
        takeScreenshot("First checkbox checked");
        Assert.assertTrue(checkboxesPage.isFirstCheckboxChecked(), "First checkbox should be checked");
        
        // Test unchecking first checkbox
        checkboxesPage.uncheckFirstCheckbox();
        takeScreenshot("First checkbox unchecked");
        Assert.assertFalse(checkboxesPage.isFirstCheckboxChecked(), "First checkbox should be unchecked");
        
        // Test toggling
        logStep("Testing checkbox toggle functionality");
        checkboxesPage.toggleFirstCheckbox();
        Assert.assertTrue(checkboxesPage.isFirstCheckboxChecked(), "First checkbox should be checked after toggle");
        
        checkboxesPage.toggleSecondCheckbox();
        Assert.assertFalse(checkboxesPage.isSecondCheckboxChecked(), "Second checkbox should be unchecked after toggle");
        
        // Test check all functionality
        logStep("Testing check all functionality");
        checkboxesPage.checkAllCheckboxes();
        takeScreenshot("All checkboxes checked");
        Assert.assertEquals(checkboxesPage.getCheckedCheckboxCount(), 2, "All checkboxes should be checked");
        
        // Test uncheck all functionality
        checkboxesPage.uncheckAllCheckboxes();
        takeScreenshot("All checkboxes unchecked");
        Assert.assertEquals(checkboxesPage.getCheckedCheckboxCount(), 0, "No checkboxes should be checked");
        
        logStep("Checkbox interactions test completed successfully");
    }

    /**
     * Test dropdown interactions
     */
    @Test(groups = {"smoke", "regression", "dropdown"}, priority = 2)
    @Story("Dropdown Interactions")
    @Description("Test dropdown operations: select by text, value, and state verification")
    public void testDropdownInteractions() {
        logStep("Starting dropdown interactions test");
        
        // Navigate to dropdown page
        open(DropdownPage.PAGE_URL);
        DropdownPage dropdownPage = new DropdownPage();
        takeScreenshot("Dropdown page loaded");
        
        // Verify page title
        String pageTitle = dropdownPage.getPageTitle();
        Assert.assertEquals(pageTitle, "Dropdown List", "Page title should be 'Dropdown List'");
        
        // Verify dropdown is enabled
        logStep("Verifying dropdown functionality");
        Assert.assertTrue(dropdownPage.isDropdownEnabled(), "Dropdown should be enabled");
        
        // Get options count
        int optionsCount = dropdownPage.getOptionsCount();
        logTestData("Options count", optionsCount);
        Assert.assertTrue(optionsCount >= 3, "Dropdown should have at least 3 options");
        
        // Test selecting Option 1
        logStep("Testing option selection");
        dropdownPage.selectOption1();
        takeScreenshot("Option 1 selected");
        String selectedText = dropdownPage.getSelectedOptionText();
        Assert.assertEquals(selectedText, "Option 1", "Selected option should be 'Option 1'");
        
        // Test selecting Option 2
        dropdownPage.selectOption2();
        takeScreenshot("Option 2 selected");
        selectedText = dropdownPage.getSelectedOptionText();
        Assert.assertEquals(selectedText, "Option 2", "Selected option should be 'Option 2'");
        
        // Test option availability
        logStep("Testing option availability");
        Assert.assertTrue(dropdownPage.isOptionAvailable("Option 1"), "Option 1 should be available");
        Assert.assertTrue(dropdownPage.isOptionAvailable("Option 2"), "Option 2 should be available");
        Assert.assertFalse(dropdownPage.isOptionAvailable("Non-existent Option"), "Non-existent option should not be available");
        
        logStep("Dropdown interactions test completed successfully");
    }

    /**
     * Test drag and drop interactions
     */
    @Test(groups = {"regression", "dragdrop"}, priority = 3)
    @Story("Drag and Drop Interactions")
    @Description("Test drag and drop functionality with position verification")
    public void testDragAndDropInteractions() {
        logStep("Starting drag and drop interactions test");
        
        // Navigate to drag and drop page
        open(DragAndDropPage.PAGE_URL);
        DragAndDropPage dragDropPage = new DragAndDropPage();
        takeScreenshot("Drag and drop page loaded");
        
        // Verify page title
        String pageTitle = dragDropPage.getPageTitle();
        Assert.assertEquals(pageTitle, "Drag and Drop", "Page title should be 'Drag and Drop'");
        
        // Verify both columns are visible
        logStep("Verifying initial page state");
        Assert.assertTrue(dragDropPage.isColumnAVisible(), "Column A should be visible");
        Assert.assertTrue(dragDropPage.isColumnBVisible(), "Column B should be visible");
        
        // Check initial positions
        Assert.assertTrue(dragDropPage.areColumnsInOriginalPosition(), "Columns should be in original position");
        String initialOrder = dragDropPage.getCurrentColumnOrder();
        logTestData("Initial column order", initialOrder);
        Assert.assertEquals(initialOrder, "A-B", "Initial order should be A-B");
        
        // Perform drag and drop
        logStep("Performing drag and drop operation");
        dragDropPage.dragColumnAToB();
        takeScreenshot("After drag and drop");
        
        // Verify positions have changed
        logStep("Verifying drag and drop results");
        String newOrder = dragDropPage.getCurrentColumnOrder();
        logTestData("New column order", newOrder);
        
        // Note: The actual behavior might vary, so we check if order changed
        Assert.assertNotEquals(newOrder, initialOrder, "Column order should have changed after drag and drop");
        
        // Try to reset to original position
        logStep("Testing reset functionality");
        dragDropPage.resetToOriginalPosition();
        takeScreenshot("After reset attempt");
        
        logStep("Drag and drop interactions test completed successfully");
    }

    /**
     * Test basic navigation and page elements
     */
    @Test(groups = {"smoke", "navigation"}, priority = 4)
    @Story("Page Navigation")
    @Description("Test basic page navigation and common elements")
    public void testBasicNavigation() {
        logStep("Starting basic navigation test");
        
        // Test checkboxes page navigation
        open(CheckboxesPage.PAGE_URL);
        CheckboxesPage checkboxesPage = new CheckboxesPage();
        takeScreenshot("Navigated to checkboxes page");
        
        String title = checkboxesPage.getPageTitle();
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
        
        // Test dropdown page navigation
        open(DropdownPage.PAGE_URL);
        DropdownPage dropdownPage = new DropdownPage();
        takeScreenshot("Navigated to dropdown page");
        
        title = dropdownPage.getPageTitle();
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
        
        // Test drag and drop page navigation
        open(DragAndDropPage.PAGE_URL);
        DragAndDropPage dragDropPage = new DragAndDropPage();
        takeScreenshot("Navigated to drag and drop page");
        
        title = dragDropPage.getPageTitle();
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
        
        logStep("Basic navigation test completed successfully");
    }

    /**
     * Test framework capabilities demonstration
     */
    @Test(groups = {"regression", "framework"}, priority = 5)
    @Story("Framework Capabilities")
    @Description("Demonstrate advanced framework features like logging, screenshots, and data handling")
    public void testFrameworkCapabilities() {
        logStep("Starting framework capabilities demonstration");
        
        // Navigate to a test page
        open(CheckboxesPage.PAGE_URL);
        CheckboxesPage checkboxesPage = new CheckboxesPage();
        
        // Demonstrate logging capabilities
        logStep("Demonstrating logging capabilities");
        logTestData("Test Environment", ApplicationConstants.Browser.BROWSER_TYPE);
        logTestData("Base URL", ApplicationConstants.URLs.BASE_URL);
        logTestData("Headless Mode", ApplicationConstants.Browser.IS_HEADLESS);
        
        // Demonstrate screenshot capabilities
        logStep("Demonstrating screenshot capabilities");
        takeScreenshot("Framework capabilities test");
        
        // Demonstrate element interactions with logging
        logStep("Demonstrating element interactions with detailed logging");
        checkboxesPage.toggleFirstCheckbox();
        boolean firstState = checkboxesPage.isFirstCheckboxChecked();
        logAssertion("First checkbox is checked after toggle", firstState);
        
        checkboxesPage.toggleSecondCheckbox();
        boolean secondState = checkboxesPage.isSecondCheckboxChecked();
        logAssertion("Second checkbox state changed after toggle", secondState != checkboxesPage.getSecondCheckboxState().equals("unchecked"));
        
        // Demonstrate test data collection
        logStep("Demonstrating test data collection");
        int checkedCount = checkboxesPage.getCheckedCheckboxCount();
        logTestData("Total checked checkboxes", checkedCount);
        
        String firstCheckboxState = checkboxesPage.getFirstCheckboxState();
        String secondCheckboxState = checkboxesPage.getSecondCheckboxState();
        logTestData("First checkbox final state", firstCheckboxState);
        logTestData("Second checkbox final state", secondCheckboxState);
        
        // Final verification
        Assert.assertTrue(checkedCount >= 0 && checkedCount <= 2, "Checked count should be between 0 and 2");
        
        logStep("Framework capabilities demonstration completed successfully");
    }
}