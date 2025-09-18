package pages.theinternet;

import core.elements.CheckBox;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Checkboxes page for testing checkbox interactions
 * URL: https://the-internet.herokuapp.com/checkboxes
 */
public class CheckboxesPage extends BaseInternetPage {
    
    // Page elements
    private final CheckBox checkbox1 = new CheckBox($(By.xpath("//input[@type='checkbox'][1]")), "Checkbox 1");
    private final CheckBox checkbox2 = new CheckBox($(By.xpath("//input[@type='checkbox'][2]")), "Checkbox 2");
    
    // Page URL
    public static final String PAGE_URL = BASE_URL + "/checkboxes";
    
    /**
     * Constructor - validates page is loaded
     */
    public CheckboxesPage() {
        validatePageLoaded();
    }

    /**
     * Check first checkbox
     * @return CheckboxesPage for method chaining
     */
    @Step("Check first checkbox")
    public CheckboxesPage checkFirstCheckbox() {
        LogUtils.logPageAction("Checking first checkbox");
        checkbox1.check();
        logger.info("First checkbox checked");
        return this;
    }

    /**
     * Uncheck first checkbox
     * @return CheckboxesPage for method chaining
     */
    @Step("Uncheck first checkbox")
    public CheckboxesPage uncheckFirstCheckbox() {
        LogUtils.logPageAction("Unchecking first checkbox");
        checkbox1.uncheck();
        logger.info("First checkbox unchecked");
        return this;
    }

    /**
     * Check second checkbox
     * @return CheckboxesPage for method chaining
     */
    @Step("Check second checkbox")
    public CheckboxesPage checkSecondCheckbox() {
        LogUtils.logPageAction("Checking second checkbox");
        checkbox2.check();
        logger.info("Second checkbox checked");
        return this;
    }

    /**
     * Uncheck second checkbox
     * @return CheckboxesPage for method chaining
     */
    @Step("Uncheck second checkbox")
    public CheckboxesPage uncheckSecondCheckbox() {
        LogUtils.logPageAction("Unchecking second checkbox");
        checkbox2.uncheck();
        logger.info("Second checkbox unchecked");
        return this;
    }

    /**
     * Toggle first checkbox
     * @return CheckboxesPage for method chaining
     */
    @Step("Toggle first checkbox")
    public CheckboxesPage toggleFirstCheckbox() {
        LogUtils.logPageAction("Toggling first checkbox");
        checkbox1.toggle();
        logger.info("First checkbox toggled");
        return this;
    }

    /**
     * Toggle second checkbox
     * @return CheckboxesPage for method chaining
     */
    @Step("Toggle second checkbox")
    public CheckboxesPage toggleSecondCheckbox() {
        LogUtils.logPageAction("Toggling second checkbox");
        checkbox2.toggle();
        logger.info("Second checkbox toggled");
        return this;
    }

    /**
     * Check if first checkbox is checked
     * @return True if first checkbox is checked
     */
    @Step("Check if first checkbox is checked")
    public boolean isFirstCheckboxChecked() {
        boolean isChecked = checkbox1.isChecked();
        LogUtils.logTestData("First Checkbox Checked", isChecked);
        logger.info("First checkbox checked status: {}", isChecked);
        return isChecked;
    }

    /**
     * Check if second checkbox is checked
     * @return True if second checkbox is checked
     */
    @Step("Check if second checkbox is checked")
    public boolean isSecondCheckboxChecked() {
        boolean isChecked = checkbox2.isChecked();
        LogUtils.logTestData("Second Checkbox Checked", isChecked);
        logger.info("Second checkbox checked status: {}", isChecked);
        return isChecked;
    }

    /**
     * Get first checkbox state
     * @return Checkbox state as string
     */
    @Step("Get first checkbox state")
    public String getFirstCheckboxState() {
        String state = isFirstCheckboxChecked() ? "checked" : "unchecked";
        LogUtils.logTestData("First Checkbox State", state);
        logger.info("First checkbox state: {}", state);
        return state;
    }

    /**
     * Get second checkbox state
     * @return Checkbox state as string
     */
    @Step("Get second checkbox state")
    public String getSecondCheckboxState() {
        String state = isSecondCheckboxChecked() ? "checked" : "unchecked";
        LogUtils.logTestData("Second Checkbox State", state);
        logger.info("Second checkbox state: {}", state);
        return state;
    }

    /**
     * Check all checkboxes
     * @return CheckboxesPage for method chaining
     */
    @Step("Check all checkboxes")
    public CheckboxesPage checkAllCheckboxes() {
        LogUtils.logPageAction("Checking all checkboxes");
        checkbox1.check();
        checkbox2.check();
        logger.info("All checkboxes checked");
        return this;
    }

    /**
     * Uncheck all checkboxes
     * @return CheckboxesPage for method chaining
     */
    @Step("Uncheck all checkboxes")
    public CheckboxesPage uncheckAllCheckboxes() {
        LogUtils.logPageAction("Unchecking all checkboxes");
        checkbox1.uncheck();
        checkbox2.uncheck();
        logger.info("All checkboxes unchecked");
        return this;
    }

    /**
     * Get count of checked checkboxes
     * @return Number of checked checkboxes
     */
    @Step("Get count of checked checkboxes")
    public int getCheckedCheckboxCount() {
        int count = 0;
        if (isFirstCheckboxChecked()) count++;
        if (isSecondCheckboxChecked()) count++;
        
        LogUtils.logTestData("Checked Checkbox Count", count);
        logger.info("Number of checked checkboxes: {}", count);
        return count;
    }
}