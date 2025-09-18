package pages.theinternet;

import core.elements.Dropdown;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * Dropdown page for testing dropdown interactions
 * URL: https://the-internet.herokuapp.com/dropdown
 */
public class DropdownPage extends BaseInternetPage {
    
    // Page elements
    private final Dropdown dropdown = new Dropdown($(By.id("dropdown")), "Main Dropdown");
    
    // Page URL
    public static final String PAGE_URL = BASE_URL + "/dropdown";
    
    /**
     * Constructor - validates page is loaded
     */
    public DropdownPage() {
        validatePageLoaded();
    }

    /**
     * Select option 1
     * @return DropdownPage for method chaining
     */
    @Step("Select option 1")
    public DropdownPage selectOption1() {
        LogUtils.logPageAction("Selecting option 1");
        dropdown.selectByText("Option 1");
        logger.info("Option 1 selected");
        return this;
    }

    /**
     * Select option 2
     * @return DropdownPage for method chaining
     */
    @Step("Select option 2")
    public DropdownPage selectOption2() {
        LogUtils.logPageAction("Selecting option 2");
        dropdown.selectByText("Option 2");
        logger.info("Option 2 selected");
        return this;
    }

    /**
     * Select option by text
     * @param optionText Option text to select
     * @return DropdownPage for method chaining
     */
    @Step("Select option by text: {optionText}")
    public DropdownPage selectOptionByText(String optionText) {
        LogUtils.logPageAction("Selecting option by text: " + optionText);
        dropdown.selectByText(optionText);
        logger.info("Option selected by text: {}", optionText);
        return this;
    }

    /**
     * Select option by value
     * @param value Option value to select
     * @return DropdownPage for method chaining
     */
    @Step("Select option by value: {value}")
    public DropdownPage selectOptionByValue(String value) {
        LogUtils.logPageAction("Selecting option by value: " + value);
        dropdown.selectByValue(value);
        logger.info("Option selected by value: {}", value);
        return this;
    }

    /**
     * Get selected option text
     * @return Selected option text
     */
    @Step("Get selected option text")
    public String getSelectedOptionText() {
        LogUtils.logPageAction("Getting selected option text");
        String selectedText = dropdown.getSelectedText();
        LogUtils.logTestData("Selected Option Text", selectedText);
        logger.info("Selected option text: {}", selectedText);
        return selectedText;
    }

    /**
     * Get selected option value
     * @return Selected option value
     */
    @Step("Get selected option value")
    public String getSelectedOptionValue() {
        LogUtils.logPageAction("Getting selected option value");
        String selectedValue = dropdown.getSelectedValue();
        LogUtils.logTestData("Selected Option Value", selectedValue);
        logger.info("Selected option value: {}", selectedValue);
        return selectedValue;
    }

    /**
     * Get all available options
     * @return Array of all option texts
     */
    @Step("Get all available options")
    public String[] getAllOptions() {
        LogUtils.logPageAction("Getting all available options");
        List<String> optionsList = dropdown.getAllOptionTexts();
        String[] options = optionsList.toArray(new String[0]);
        LogUtils.logTestData("All Options Count", options.length);
        logger.info("Available options: {}", java.util.Arrays.toString(options));
        return options;
    }

    /**
     * Check if specific option is available
     * @param optionText Option text to check
     * @return True if option is available
     */
    @Step("Check if option is available: {optionText}")
    public boolean isOptionAvailable(String optionText) {
        LogUtils.logPageAction("Checking if option is available: " + optionText);
        boolean isAvailable = dropdown.hasOption(optionText);
        LogUtils.logTestData("Option Available", isAvailable);
        logger.info("Option '{}' available: {}", optionText, isAvailable);
        return isAvailable;
    }

    /**
     * Verify dropdown is enabled
     * @return True if dropdown is enabled
     */
    @Step("Verify dropdown is enabled")
    public boolean isDropdownEnabled() {
        LogUtils.logPageAction("Checking if dropdown is enabled");
        boolean isEnabled = dropdown.isEnabled();
        LogUtils.logTestData("Dropdown Enabled", isEnabled);
        logger.info("Dropdown enabled: {}", isEnabled);
        return isEnabled;
    }

    /**
     * Get dropdown options count
     * @return Number of available options
     */
    @Step("Get dropdown options count")
    public int getOptionsCount() {
        LogUtils.logPageAction("Getting dropdown options count");
        int count = dropdown.getOptionsCount();
        LogUtils.logTestData("Options Count", count);
        logger.info("Dropdown options count: {}", count);
        return count;
    }
}