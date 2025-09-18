package pages.agoda;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import core.elements.Button;
import core.elements.TextBox;
import core.utils.LogUtils;
import core.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

/**
 * Updated AgodaHomePage with correct XPaths captured from actual Agoda site
 */
public class AgodaHomePageUpdated {
    private static final Logger logger = LoggerFactory.getLogger(AgodaHomePageUpdated.class);
    
    // URL
    private static final String AGODA_URL = "https://www.agoda.com/";
    
    // Page Elements - Updated with captured XPaths from actual Agoda site
    private final TextBox destinationInput = new TextBox($(By.xpath("//div[@data-selenium='icon-box-child']//input[@id='textInput']")), "Destination Input");
    
    // Search autocomplete dropdown
    private final SelenideElement firstAutocompleteOption = $(By.xpath("(//ul[@class='AutocompleteList']//li[@role='option'])[1]"));
    
    // Date picker elements
    private final Button datePickerButton = new Button($(By.xpath("//button[@data-selenium='date-display-btn']")), "Date Picker Button");
    
    // Calendar tab elements - verify before date selection
    private final SelenideElement calendarTab = $(By.xpath("//button[@role='tab']//p[normalize-space()='Calendar']"));
    private final SelenideElement flexibleTab = $(By.xpath("//button[@role='tab']//p[normalize-space()=\"I'm flexible\"]"));
    
    // Search button  
    private final Button searchButton = new Button($(By.xpath("//button[@data-selenium='searchButton']")), "Search Button");
    
    // Occupancy elements with correct XPaths
    // Rooms
    private final SelenideElement roomValue = $(By.xpath("//div[@data-selenium='desktop-occ-room-value']/p"));
    private final Button addRoom = new Button($(By.xpath("//div[@data-selenium='occupancyRooms']//button[@data-selenium='plus']")), "Add Room Button");
    private final Button minusRoom = new Button($(By.xpath("//div[@data-selenium='occupancyRooms']//button[@data-selenium='minus']")), "Minus Room Button");
    
    // Adults
    private final SelenideElement adultValue = $(By.xpath("//div[@data-selenium='desktop-occ-adult-value']/p"));
    private final Button addAdult = new Button($(By.xpath("//div[@data-selenium='occupancyAdults']//button[@data-selenium='plus']")), "Add Adult Button");
    private final Button minusAdult = new Button($(By.xpath("//div[@data-selenium='occupancyAdults']//button[@data-selenium='minus']")), "Minus Adult Button");
    
    // Children
    private final SelenideElement childValue = $(By.xpath("//div[@data-selenium='desktop-occ-children-value']/p"));
    private final Button addChild = new Button($(By.xpath("//div[@data-selenium='occupancyChildren']//button[@data-selenium='plus']")), "Add Child Button");
    private final Button minusChild = new Button($(By.xpath("//div[@data-selenium='occupancyChildren']//button[@data-selenium='minus']")), "Minus Child Button");

    public AgodaHomePageUpdated() {
        logger.info("Initialized AgodaHomePageUpdated");
    }

    /**
     * Navigate to Agoda homepage
     */
    @Step("Navigate to Agoda homepage")
    public void navigateToHomepage() {
        LogUtils.logTestStep("Navigate to Agoda homepage: " + AGODA_URL);
        open(AGODA_URL);
        WaitUtils.waitForPageLoad();
        LogUtils.logTestStep("Agoda homepage loaded successfully");
    }

    /**
     * Enter destination with autocomplete selection
     */
    @Step("Enter destination: {destination}")
    public void enterDestination(String destination) {
        LogUtils.logTestStep("Entering destination: " + destination);
        
        // Clear and enter destination
        destinationInput.clear();
        destinationInput.type(destination);
        
        // Wait for autocomplete results to appear
        WaitUtils.waitForVisible(firstAutocompleteOption, Duration.ofSeconds(10));
        
        // Click first option
        firstAutocompleteOption.click();
        LogUtils.logTestStep("Selected first autocomplete option for: " + destination);
    }

    /**
     * Verify calendar tabs are displayed and interactable
     */
    @Step("Verify calendar tabs are displayed and interactable")
    public void verifyCalendarTabs() {
        LogUtils.logTestStep("Verifying calendar tabs are displayed and interactable");
        
        // Wait for calendar tabs to be visible
        WaitUtils.waitForVisible(calendarTab, Duration.ofSeconds(5));
        WaitUtils.waitForVisible(flexibleTab, Duration.ofSeconds(5));
        
        // Verify both tabs are displayed and clickable
        if (calendarTab.isDisplayed() && calendarTab.isEnabled()) {
            LogUtils.logTestStep("✓ Calendar tab is displayed and interactable");
        } else {
            LogUtils.logTestStep("✗ Calendar tab is not properly displayed or interactable");
        }
        
        if (flexibleTab.isDisplayed() && flexibleTab.isEnabled()) {
            LogUtils.logTestStep("✓ I'm flexible tab is displayed and interactable");
        } else {
            LogUtils.logTestStep("✗ I'm flexible tab is not properly displayed or interactable");
        }
    }

    /**
     * Select dates using dynamic XPath
     */
    @Step("Select dates from {startDate} to {endDate}")
    public void selectDates(String startYear, String startMonth, String startDay, 
                           String endYear, String endMonth, String endDay) {
        
        // First click the date picker button to open calendar
        LogUtils.logTestStep("Opening date picker");
        datePickerButton.click();
        WaitUtils.sleep(1000); // Wait for calendar to open
        
        // Now verify calendar tabs are available
        verifyCalendarTabs();
        
        LogUtils.logTestStep("Selecting check-in date: " + startYear + "-" + startMonth + "-" + startDay);
        selectDate(startYear, startMonth, startDay);
        
        LogUtils.logTestStep("Selecting check-out date: " + endYear + "-" + endMonth + "-" + endDay);
        selectDate(endYear, endMonth, endDay);
    }

    /**
     * Select a specific date using updated dynamic XPath - clicks the button containing the date span
     */
    private void selectDate(String year, String month, String day) {
        // Updated XPath to click the button containing the date span
        String dateString = String.format("%s-%s-%s", year, month, day);
        String xpath = String.format("//div[@role='button']//span[@data-selenium-date='%s']/ancestor::div[@role='button']", dateString);
        SelenideElement dateElement = $(By.xpath(xpath));
        
        LogUtils.logTestStep("Clicking date element with XPath: " + xpath);
        
        // Use JavaScript click as mentioned in requirements
        dateElement.scrollTo().click();
        
        LogUtils.logTestStep("Successfully selected date: " + dateString);
    }

    /**
     * Set occupancy (rooms, adults, children) intelligently
     */
    @Step("Set occupancy - Rooms: {rooms}, Adults: {adults}, Children: {children}")
    public void setOccupancy(int targetRooms, int targetAdults, int targetChildren) {
        LogUtils.logTestStep("Setting occupancy - Rooms: " + targetRooms + ", Adults: " + targetAdults + ", Children: " + targetChildren);
        
        // Set rooms
        setRoomCount(targetRooms);
        
        // Set adults
        setAdultCount(targetAdults);
        
        // Set children
        setChildCount(targetChildren);
    }

    /**
     * Set room count by comparing current value with target
     */
    private void setRoomCount(int targetRooms) {
        int currentRooms = Integer.parseInt(roomValue.getText().trim());
        LogUtils.logTestStep("Current rooms: " + currentRooms + ", Target rooms: " + targetRooms);
        
        while (currentRooms < targetRooms) {
            addRoom.click();
            currentRooms++;
            sleep(1000); // Simple wait instead of waitForElementUpdate
        }
        
        while (currentRooms > targetRooms) {
            minusRoom.click();
            currentRooms--;
            sleep(1000); // Simple wait instead of waitForElementUpdate
        }
    }

    /**
     * Set adult count by comparing current value with target
     */
    private void setAdultCount(int targetAdults) {
        int currentAdults = Integer.parseInt(adultValue.getText().trim());
        LogUtils.logTestStep("Current adults: " + currentAdults + ", Target adults: " + targetAdults);
        
        while (currentAdults < targetAdults) {
            addAdult.click();
            currentAdults++;
            sleep(1000); // Simple wait instead of waitForElementUpdate
        }
        
        while (currentAdults > targetAdults) {
            minusAdult.click();
            currentAdults--;
            sleep(1000); // Simple wait instead of waitForElementUpdate
        }
    }

    /**
     * Set child count by comparing current value with target
     */
    private void setChildCount(int targetChildren) {
        int currentChildren = Integer.parseInt(childValue.getText().trim());
        LogUtils.logTestStep("Current children: " + currentChildren + ", Target children: " + targetChildren);
        
        while (currentChildren < targetChildren) {
            addChild.click();
            currentChildren++;
            sleep(1000); // Simple wait instead of waitForElementUpdate
        }
        
        while (currentChildren > targetChildren) {
            minusChild.click();
            currentChildren--;
            sleep(1000); // Simple wait instead of waitForElementUpdate
        }
    }

    /**
     * Click search button and handle new tab
     */
    @Step("Click search button")
    public AgodaSearchResultsPageUpdated clickSearchButton() {
        LogUtils.logTestStep("Clicking search button");
        searchButton.click();
        
        // Handle new tab - switch back to expected tab
        LogUtils.logTestStep("Handling tab switching after search");
        SwitchTabs.switchToExpectedTab();
        
        return new AgodaSearchResultsPageUpdated();
    }

    /**
     * Complete hotel search with all parameters
     */
    @Step("Search hotels with destination: {destination}")
    public AgodaSearchResultsPageUpdated searchHotels(String destination, String startYear, String startMonth, String startDay,
                                                     String endYear, String endMonth, String endDay, 
                                                     int rooms, int adults, int children) {
        enterDestination(destination);
        selectDates(startYear, startMonth, startDay, endYear, endMonth, endDay);
        setOccupancy(rooms, adults, children);
        return clickSearchButton();
    }

    /**
     * Check if homepage is displayed
     */
    public boolean isHomepageDisplayed() {
        try {
            boolean isDisplayed = destinationInput.isVisible() && 
                                searchButton.isVisible();
            
            LogUtils.logTestStep("Homepage verification: " + (isDisplayed ? "PASSED" : "FAILED"));
            return isDisplayed;
        } catch (Exception e) {
            LogUtils.logTestStep("Homepage verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper class for tab switching
     */
    private static class SwitchTabs {
        @Step("Switch to expected search results tab")
        public static void switchToExpectedTab() {
            try {
                // Get all window handles
                java.util.Set<String> windowHandles = WebDriverRunner.getWebDriver().getWindowHandles();
                
                if (windowHandles.size() > 1) {
                    // Switch through tabs to find the expected one
                    for (String windowHandle : windowHandles) {
                        WebDriverRunner.getWebDriver().switchTo().window(windowHandle);
                        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
                        
                        // Check if this is the expected search results tab
                        if (currentUrl.contains("agoda.com/activities/search") || 
                            currentUrl.contains("agoda.com/search")) {
                            LogUtils.logTestStep("Switched to expected search results tab: " + currentUrl);
                            return;
                        }
                    }
                }
                
                LogUtils.logTestStep("Staying on current tab");
            } catch (Exception e) {
                LogUtils.logTestStep("Error switching tabs: " + e.getMessage());
            }
        }
    }
}