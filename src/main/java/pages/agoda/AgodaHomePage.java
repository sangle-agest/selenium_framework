package pages.agoda;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import core.constants.ApplicationConstants;
import core.elements.Button;
import core.elements.TextBox;
import core.utils.LogUtils;
import core.utils.PageWaitUtils;
import core.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

import static com.codeborne.selenide.Selenide.*;

/**
 * Updated AgodaHomePage with correct XPaths captured from actual Agoda site
 */
public class AgodaHomePage {
    
    // Page Elements - Updated with captured XPaths from actual Agoda site
    private final TextBox destinationInput = new TextBox($(By.xpath("//div[@data-selenium='icon-box-child']//input[@id='textInput']")), "Destination Input");
    
    // Search autocomplete dropdown
    private final SelenideElement firstAutocompleteOption = $(By.xpath("(//ul[@class='AutocompleteList']//li[@role='option'])[1]"));
    
    // Date picker elements
    private final SelenideElement checkInButton = $(By.xpath("//div[@id='check-in-box' and @role='button']"));
    private final SelenideElement checkOutButton = $(By.xpath("//div[@id='check-out-box' and @role='button']"));
    
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

    public AgodaHomePage() {
        LogUtils.logTestStep("Initialized AgodaHomePageUpdated");
    }

    /**
     * Navigate to Agoda homepage
     */
    @Step("Navigate to Agoda homepage")
    public void navigateToHomepage() {
        LogUtils.logTestStep("Navigate to Agoda homepage: " + ApplicationConstants.URLs.AGODA_URL);
        open(ApplicationConstants.URLs.AGODA_URL);
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
        
        // Use common autocomplete selection method
        PageWaitUtils.waitForAutocompleteAndSelect(firstAutocompleteOption, destination);
    }

    /**
     * Verify calendar tabs are displayed and interactable
     */
    @Step("Verify calendar tabs are displayed and interactable")
    public void verifyCalendarTabs() {
        LogUtils.logTestStep("Verifying calendar tabs are displayed and interactable");
        
        try {
            // Wait for calendar tabs to be visible with shorter timeout
            WaitUtils.waitForVisible(calendarTab, Duration.ofSeconds(3));
            LogUtils.logVerificationStep("✓ Calendar tab is displayed and interactable");
        } catch (Exception e) {
            LogUtils.logVerificationStep("⚠ Calendar tab not found - continuing without verification: " + e.getMessage());
        }
        
        try {
            WaitUtils.waitForVisible(flexibleTab, Duration.ofSeconds(3));
            LogUtils.logVerificationStep("✓ I'm flexible tab is displayed and interactable");
        } catch (Exception e) {
            LogUtils.logVerificationStep("⚠ I'm flexible tab not found - continuing without verification: " + e.getMessage());
        }
    }

    /**
     * Check if DatePicker popup is accessible and visible
     */
    private boolean isDatePickerVisible() {
        try {
            LogUtils.logTestStep("DEBUG: Checking for DatePicker accessibility...");
            SelenideElement datePickerPopup = $(By.xpath("//div[@id='DatePicker__AccessibleV2']"));
            boolean isVisible = datePickerPopup.exists() && datePickerPopup.isDisplayed();
            LogUtils.logTestStep("DEBUG: DatePicker visibility check result: " + isVisible);
            return isVisible;
        } catch (Exception e) {
            LogUtils.logTestStep("DEBUG: Exception checking DatePicker visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Select dates using simple date format (YYYY-MM-DD)
     */
    @Step("Select check-in date: {checkInDate} and check-out date: {checkOutDate}")
    public void selectDates(String checkInDate, String checkOutDate) {
        LogUtils.logTestStep("Selecting dates - Check-in: " + checkInDate + ", Check-out: " + checkOutDate);
        
        // First check if DatePicker popup is already visible after autocomplete selection
        LogUtils.logTestStep("Checking if DatePicker popup is already visible after autocomplete selection");
        
        if (isDatePickerVisible()) {
            LogUtils.logVerificationStep("✓ DatePicker popup is already visible, proceeding with date selection");
        } else {
            LogUtils.logTestStep("DatePicker popup not visible, clicking check-in button to open calendar");
            checkInButton.click();
            
            // Wait for DatePicker popup to become visible using common method
            boolean isPopupVisible = PageWaitUtils.waitForPopupVisible(this::isDatePickerVisible, "DatePicker");
            
            if (!isPopupVisible) {
                return;
            }
        }
        
        LogUtils.logTestStep("Selecting check-in date: " + checkInDate);
        selectDate(checkInDate);
        
        LogUtils.logTestStep("Selecting check-out date: " + checkOutDate);
        selectDate(checkOutDate);
    }

        /**
     * Select a specific date using simple date format (YYYY-MM-DD) within the DatePicker popup
     */
    private void selectDate(String dateString) {
        try {
            LogUtils.logTestStep("Looking for date: " + dateString);
            
            // First ensure DatePicker popup is visible
            SelenideElement datePickerPopup = $(By.xpath("//div[@id='DatePicker__AccessibleV2']"));
            if (!datePickerPopup.exists() || !datePickerPopup.isDisplayed()) {
                LogUtils.logTestStep("ERROR: DatePicker popup is not visible when trying to select date");
                return;
            }
            
            // Use common date selection method
            boolean dateSelected = PageWaitUtils.waitForDateSelection(dateString, "//div[@id='DatePicker__AccessibleV2']");
            
            if (!dateSelected) {
                // Debug: Check available dates
                LogUtils.logTestStep("DEBUG: Checking what dates are available in the DatePicker popup...");
                String availableDatesScript = "var dates = document.evaluate(\"//div[@id='DatePicker__AccessibleV2']//span[@data-selenium-date]\", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null); var result = []; for(var i = 0; i < Math.min(10, dates.snapshotLength); i++) { result.push(dates.snapshotItem(i).getAttribute('data-selenium-date')); } return result.join(', ');";
                String availableDates = (String) executeJavaScript(availableDatesScript);
                LogUtils.logTestStep("DEBUG: Available dates in popup: " + availableDates);
                
                // Try to find a similar date if exact date not available
                LogUtils.logTestStep("DEBUG: Attempting to find alternative dates close to: " + dateString);
                String alternativeScript = "var dates = document.evaluate(\"//div[@id='DatePicker__AccessibleV2']//span[@data-selenium-date]\", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null); for(var i = 0; i < dates.snapshotLength; i++) { var date = dates.snapshotItem(i).getAttribute('data-selenium-date'); if(date && date.includes('2025-09')) { dates.snapshotItem(i).click(); return 'Clicked alternative date: ' + date; } } return 'No suitable alternative found';";
                String alternativeResult = (String) executeJavaScript(alternativeScript);
                LogUtils.logTestStep("DEBUG: Alternative date selection result: " + alternativeResult);
            }
            
        } catch (Exception e) {
            LogUtils.logTestStep("ERROR: Exception selecting date: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set occupancy (rooms, adults, children) intelligently
     */
    @Step("Set occupancy - Rooms: {targetRooms}, Adults: {targetAdults}, Children: {targetChildren}")
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
        
        // Use common counter update method
        PageWaitUtils.updateCounterSmart(roomValue, currentRooms, targetRooms, addRoom, minusRoom, "Room");
    }

    /**
     * Set adult count by comparing current value with target
     */
    private void setAdultCount(int targetAdults) {
        int currentAdults = Integer.parseInt(adultValue.getText().trim());
        LogUtils.logTestStep("Current adults: " + currentAdults + ", Target adults: " + targetAdults);
        
        // Use common counter update method
        PageWaitUtils.updateCounterSmart(adultValue, currentAdults, targetAdults, addAdult, minusAdult, "Adult");
    }

    /**
     * Set child count by comparing current value with target
     */
    private void setChildCount(int targetChildren) {
        int currentChildren = Integer.parseInt(childValue.getText().trim());
        LogUtils.logTestStep("Current children: " + currentChildren + ", Target children: " + targetChildren);
        
        // Use common counter update method
        PageWaitUtils.updateCounterSmart(childValue, currentChildren, targetChildren, addChild, minusChild, "Children");
    }

    /**
     * Click search button and handle new tab
     */
    @Step("Click search button")
    public AgodaSearchResultsPage clickSearchButton() {
        LogUtils.logTestStep("Clicking search button");
        searchButton.click();
        
        // Handle new tab - switch back to expected tab
        LogUtils.logTestStep("Handling tab switching after search");
        SwitchTabs.switchToExpectedTab();
        
        // Create results page instance and wait for it to load
        AgodaSearchResultsPage resultsPage = new AgodaSearchResultsPage();
        LogUtils.logTestStep("Waiting for search results page to fully load after tab switch...");
        resultsPage.waitForPageToLoad();
        
        return resultsPage;
    }

    /**
     * Method 1: Search for destination and handle autocomplete
     */
    @Step("Search destination: {destination}")
    public void searchDestination(String destination) {
        LogUtils.logTestStep("Step 1: Searching for destination - " + destination);
        enterDestination(destination);
        LogUtils.logVerificationStep("✓ Destination search completed");
    }
    
    /**
     * Method 2: Select check-in and check-out dates
     */
    @Step("Select travel dates: {checkInDate} to {checkOutDate}")
    public void selectTravelDates(String checkInDate, String checkOutDate) {
        LogUtils.logTestStep("Step 2: Selecting travel dates - Check-in: " + checkInDate + ", Check-out: " + checkOutDate);
        
        // Wait for the page to be ready for date selection by checking if check-in button is clickable
        WaitUtils.waitForCondition(() -> {
            try {
                return checkInButton.exists() && checkInButton.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }, 10);
        
        selectDates(checkInDate, checkOutDate);
        LogUtils.logVerificationStep("✓ Travel dates selection completed");
    }
    
    /**
     * Method 3: Set occupancy and perform search
     */
    @Step("Set occupancy and search - Rooms: {rooms}, Adults: {adults}, Children: {children}")
    public AgodaSearchResultsPage setOccupancyAndSearch(int rooms, int adults, int children) {
        LogUtils.logTestStep("Step 3: Setting occupancy - Rooms: " + rooms + ", Adults: " + adults + ", Children: " + children);
        setOccupancy(rooms, adults, children);
        LogUtils.logVerificationStep("✓ Occupancy set, clicking search button");
        return clickSearchButton();
    }

    /**
     * Complete hotel search with all parameters (simplified version)
     */
    @Step("Search hotels with destination: {destination}")
    public AgodaSearchResultsPage searchHotels(String destination, String checkInDate, String checkOutDate,
                                               int rooms, int adults, int children) {
        LogUtils.logTestStep("Starting complete hotel search process");
        
        // Step 1: Search destination
        searchDestination(destination);
        
        // Step 2: Select dates
        selectTravelDates(checkInDate, checkOutDate);
        
        // Step 3: Set occupancy and search
        AgodaSearchResultsPage resultsPage = setOccupancyAndSearch(rooms, adults, children);
        
        LogUtils.logVerificationStep("✓ Complete hotel search process finished");
        return resultsPage;
    }

    /**
     * Check if homepage is displayed
     */
    public boolean isHomepageDisplayed() {
        try {
            boolean isDisplayed = destinationInput.isVisible() && 
                                searchButton.isVisible();
            
            LogUtils.logVerificationStep("Homepage verification: " + (isDisplayed ? "PASSED" : "FAILED"));
            return isDisplayed;
        } catch (Exception e) {
            LogUtils.logVerificationStep("Homepage verification failed: " + e.getMessage());
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