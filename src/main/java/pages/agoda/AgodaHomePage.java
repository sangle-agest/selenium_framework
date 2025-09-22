package pages.agoda;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import core.constants.ApplicationConstants;
import core.elements.Button;
import core.elements.TextBox;
import core.utils.LogUtils;
import core.utils.PageWaitUtils;
import core.utils.WaitUtils;
import core.utils.DatePickerJavaScriptUtils;
import core.utils.CalendarMonthParser;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

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
     * Check if DatePicker popup is accessible and visible
     */
    private boolean isDatePickerVisible() {
        try {
            return $(By.xpath("//div[@id='DatePicker__AccessibleV2']")).exists() && 
                   $(By.xpath("//div[@id='DatePicker__AccessibleV2']")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Select dates using simple date format (YYYY-MM-DD)
     */
    @Step("Select check-in date: {checkInDate} and check-out date: {checkOutDate}")
    public void selectDates(String checkInDate, String checkOutDate) {
        LogUtils.logTestStep("Selecting dates - Check-in: " + checkInDate + ", Check-out: " + checkOutDate);
        
        // Open DatePicker if not already visible
        if (!isDatePickerVisible()) {
            checkInButton.click();
            if (!PageWaitUtils.waitForPopupVisible(this::isDatePickerVisible, "DatePicker")) {
                return;
            }
        }
        
        selectDate(checkInDate);
        selectDate(checkOutDate);
    }

    /**
     * Select a specific date using the new Agoda date picker structure
     * Simplified version using utilities
     */
    private void selectDate(String dateString) {
        try {
            // Ensure DatePicker popup is visible
            SelenideElement datePickerPopup = $(By.xpath("//div[@id='DatePicker__AccessibleV2']"));
            if (!datePickerPopup.exists() || !datePickerPopup.isDisplayed()) {
                LogUtils.logTestStep("ERROR: DatePicker popup is not visible");
                return;
            }
            
            WaitUtils.sleep(1000); // Wait for popup to fully load
            
            // Try to click the date directly
            SelenideElement targetDateElement = $(By.xpath("//span[@data-selenium-date='" + dateString + "']"));
            if (targetDateElement.exists() && targetDateElement.isDisplayed()) {
                String clickResult = DatePickerJavaScriptUtils.clickDate(dateString);
                LogUtils.logTestStep("✓ " + clickResult);
                WaitUtils.sleep(500);
                return;
            }
            
            // If date not visible, check if navigation is needed
            String availableDates = DatePickerJavaScriptUtils.getAvailableDates();
            if (!availableDates.contains(dateString)) {
                // Parse date and navigate to correct month
                String[] dateParts = dateString.split("-");
                int targetYear = Integer.parseInt(dateParts[0]);
                int targetMonth = Integer.parseInt(dateParts[1]);
                
                if (navigateToTargetMonth(targetYear, targetMonth)) {
                    WaitUtils.sleep(1000);
                    String retryResult = DatePickerJavaScriptUtils.clickDate(dateString);
                    LogUtils.logTestStep("✓ After navigation: " + retryResult);
                    return;
                }
            }
            
            // Fallback: try alternative date in same month
            String targetMonthPrefix = dateString.substring(0, 7); // YYYY-MM
            String alternativeResult = DatePickerJavaScriptUtils.clickAlternativeDate(targetMonthPrefix);
            LogUtils.logTestStep("Fallback: " + alternativeResult);
            
        } catch (Exception e) {
            LogUtils.logTestStep("ERROR: Exception selecting date: " + e.getMessage());
        }
    }

    /**
     * Navigate to a specific target month and year in the date picker
     * Simplified using utilities
     */
    private boolean navigateToTargetMonth(int targetYear, int targetMonth) {
        try {
            String currentMonthText = DatePickerJavaScriptUtils.getCurrentMonthCaption();
            CalendarMonthParser.MonthInfo currentMonth = CalendarMonthParser.parseMonthInfo(currentMonthText);
            
            if (currentMonth == null) {
                return false;
            }
            
            int monthDiff = CalendarMonthParser.calculateMonthDifference(currentMonth, targetYear, targetMonth);
            
            if (monthDiff == 0) {
                return true; // Already at target month
            }
            
            // Navigate forward or backward
            boolean navigateForward = monthDiff > 0;
            int stepsToNavigate = Math.abs(monthDiff);
            String buttonSelector = navigateForward ? 
                "//button[@data-selenium='calendar-next-month-button']" : 
                "//button[@data-selenium='calendar-previous-month-button']";
            
            for (int i = 0; i < stepsToNavigate && i < 24; i++) { // Limit to 24 months max
                SelenideElement navButton = $(By.xpath(buttonSelector));
                if (navButton.exists() && navButton.isDisplayed()) {
                    navButton.click();
                    WaitUtils.sleep(500);
                } else {
                    return false;
                }
            }
            
            // Verify we reached the target month
            WaitUtils.sleep(1000);
            String finalMonthText = DatePickerJavaScriptUtils.getCurrentMonthCaption();
            return finalMonthText.contains("Tháng " + targetMonth) && finalMonthText.contains(String.valueOf(targetYear));
            
        } catch (Exception e) {
            return false;
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
        PageWaitUtils.updateCounterSmart(roomValue, currentRooms, targetRooms, addRoom, minusRoom, "Room");
    }

    /**
     * Set adult count by comparing current value with target
     */
    private void setAdultCount(int targetAdults) {
        int currentAdults = Integer.parseInt(adultValue.getText().trim());
        PageWaitUtils.updateCounterSmart(adultValue, currentAdults, targetAdults, addAdult, minusAdult, "Adult");
    }

    /**
     * Set child count by comparing current value with target
     */
    private void setChildCount(int targetChildren) {
        int currentChildren = Integer.parseInt(childValue.getText().trim());
        PageWaitUtils.updateCounterSmart(childValue, currentChildren, targetChildren, addChild, minusChild, "Children");
    }

    /**
     * Click search button and handle new tab
     */
    @Step("Click search button")
    public AgodaSearchResultsPage clickSearchButton() {
        searchButton.click();
        
        // Handle new tab - switch back to expected tab
        SwitchTabs.switchToExpectedTab();
        
        // Create results page instance and wait for it to load
        AgodaSearchResultsPage resultsPage = new AgodaSearchResultsPage();
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
    }
    
    /**
     * Method 2: Select check-in and check-out dates
     */
    @Step("Select travel dates: {checkInDate} to {checkOutDate}")
    public void selectTravelDates(String checkInDate, String checkOutDate) {
        LogUtils.logTestStep("Step 2: Selecting travel dates - Check-in: " + checkInDate + ", Check-out: " + checkOutDate);
        
        // Wait for the page to be ready for date selection
        WaitUtils.waitForCondition(() -> {
            try {
                return checkInButton.exists() && checkInButton.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }, 10);
        
        selectDates(checkInDate, checkOutDate);
    }
    
    /**
     * Method 3: Set occupancy and perform search
     */
    @Step("Set occupancy and search - Rooms: {rooms}, Adults: {adults}, Children: {children}")
    public AgodaSearchResultsPage setOccupancyAndSearch(int rooms, int adults, int children) {
        LogUtils.logTestStep("Step 3: Setting occupancy - Rooms: " + rooms + ", Adults: " + adults + ", Children: " + children);
        setOccupancy(rooms, adults, children);
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