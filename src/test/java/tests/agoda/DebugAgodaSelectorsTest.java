package tests.agoda;

import core.utils.LogUtils;
import org.testng.annotations.Test;
import com.codeborne.selenide.Selenide;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

/**
 * Debug test to check page selectors and structure
 */
public class DebugAgodaSelectorsTest extends AgodaBaseTest {

    @Test(description = "Debug Agoda page selectors and structure")
    public void debugAgodaSelectors() {
        LogUtils.logTestStep("=== DEBUG: Analyzing Agoda page structure ===");
        
        // Navigate to Agoda and wait for page load
        open("https://www.agoda.com");
        Selenide.sleep(3000);
        
        LogUtils.logTestStep("DEBUG: Checking destination input");
        boolean destInputExists = $(By.xpath("//div[@data-selenium='icon-box-child']//input[@id='textInput']")).exists();
        LogUtils.logTestStep("Destination input exists: " + destInputExists);
        
        LogUtils.logTestStep("DEBUG: Checking check-in button variations");
        String[] checkInSelectors = {
            "//div[@id='check-in-box' and @role='button']",
            "//div[@id='check-in-box']",
            "//*[@id='check-in-box']",
            "//div[contains(@id, 'check-in')]",
            "//div[contains(@class, 'check-in')]",
            "//input[contains(@placeholder, 'Check-in') or contains(@placeholder, 'check-in')]",
            "//*[contains(text(), 'Check-in') or contains(text(), 'check-in')]"
        };
        
        for (String selector : checkInSelectors) {
            boolean exists = $(By.xpath(selector)).exists();
            LogUtils.logTestStep("Selector: " + selector + " exists: " + exists);
            if (exists) {
                try {
                    boolean visible = $(By.xpath(selector)).isDisplayed();
                    LogUtils.logTestStep("  --> Visible: " + visible);
                } catch (Exception e) {
                    LogUtils.logTestStep("  --> Visibility check failed: " + e.getMessage());
                }
            }
        }
        
        LogUtils.logTestStep("DEBUG: Checking occupancy room value variations");
        String[] roomSelectors = {
            "//div[@data-selenium='desktop-occ-room-value']/p",
            "//div[@data-selenium='desktop-occ-room-value']",
            "//*[@data-selenium='desktop-occ-room-value']",
            "//*[contains(@data-selenium, 'room-value')]",
            "//*[contains(@data-selenium, 'room')]",
            "//*[contains(text(), '1') and contains(@class, 'room')]"
        };
        
        for (String selector : roomSelectors) {
            boolean exists = $(By.xpath(selector)).exists();
            LogUtils.logTestStep("Room selector: " + selector + " exists: " + exists);
        }
        
        LogUtils.logTestStep("DEBUG: Getting all data-selenium attributes on page");
        String dataSeleniumScript = 
            "var elements = document.querySelectorAll('[data-selenium]'); " +
            "var result = []; " +
            "for(var i = 0; i < Math.min(20, elements.length); i++) { " +
            "  result.push(elements[i].getAttribute('data-selenium')); " +
            "} " +
            "return result.join(', ');";
        
        String dataSeleniumAttrs = (String) executeJavaScript(dataSeleniumScript);
        LogUtils.logTestStep("Found data-selenium attributes: " + dataSeleniumAttrs);
        
        LogUtils.logTestStep("=== DEBUG: Analysis complete ===");
    }
}