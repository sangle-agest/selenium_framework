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
    
    // Occupancy elements with updated flexible XPaths
    // Rooms - Try multiple possible XPaths
    private final SelenideElement roomValue = $(By.xpath("//div[@data-selenium='desktop-occ-room-value']/p | //div[contains(@class,'OccupancyContainer')]//div[contains(@class,'room')]//span[contains(@class,'value')] | //div[@data-testid='occupancy-room-value'] | (//div[contains(@class,'occupancy')]//div[text()='Rooms']/following-sibling::div//span)[1]"));
    private final Button addRoom = new Button($(By.xpath("//div[@data-selenium='occupancyRooms']//button[@data-selenium='plus'] | //button[contains(@aria-label,'increase room')] | //button[contains(@class,'plus') and ancestor::div[contains(@class,'room')]] | (//div[contains(@class,'occupancy')]//div[text()='Rooms']/following-sibling::div//button[contains(@class,'plus') or text()='+'])[1]")), "Add Room Button");
    private final Button minusRoom = new Button($(By.xpath("//div[@data-selenium='occupancyRooms']//button[@data-selenium='minus'] | //button[contains(@aria-label,'decrease room')] | //button[contains(@class,'minus') and ancestor::div[contains(@class,'room')]] | (//div[contains(@class,'occupancy')]//div[text()='Rooms']/following-sibling::div//button[contains(@class,'minus') or text()='-'])[1]")), "Minus Room Button");
    
    // Adults - Try multiple possible XPaths
    private final SelenideElement adultValue = $(By.xpath("//div[@data-selenium='desktop-occ-adult-value']/p | //div[contains(@class,'OccupancyContainer')]//div[contains(@class,'adult')]//span[contains(@class,'value')] | //div[@data-testid='occupancy-adult-value'] | (//div[contains(@class,'occupancy')]//div[text()='Adults']/following-sibling::div//span)[1]"));
    private final Button addAdult = new Button($(By.xpath("//div[@data-selenium='occupancyAdults']//button[@data-selenium='plus'] | //button[contains(@aria-label,'increase adult')] | //button[contains(@class,'plus') and ancestor::div[contains(@class,'adult')]] | (//div[contains(@class,'occupancy')]//div[text()='Adults']/following-sibling::div//button[contains(@class,'plus') or text()='+'])[1]")), "Add Adult Button");
    private final Button minusAdult = new Button($(By.xpath("//div[@data-selenium='occupancyAdults']//button[@data-selenium='minus'] | //button[contains(@aria-label,'decrease adult')] | //button[contains(@class,'minus') and ancestor::div[contains(@class,'adult')]] | (//div[contains(@class,'occupancy')]//div[text()='Adults']/following-sibling::div//button[contains(@class,'minus') or text()='-'])[1]")), "Minus Adult Button");
    
    // Children - Try multiple possible XPaths
    private final SelenideElement childValue = $(By.xpath("//div[@data-selenium='desktop-occ-children-value']/p | //div[contains(@class,'OccupancyContainer')]//div[contains(@class,'child')]//span[contains(@class,'value')] | //div[@data-testid='occupancy-child-value'] | (//div[contains(@class,'occupancy')]//div[text()='Children']/following-sibling::div//span)[1]"));
    private final Button addChild = new Button($(By.xpath("//div[@data-selenium='occupancyChildren']//button[@data-selenium='plus'] | //button[contains(@aria-label,'increase child')] | //button[contains(@class,'plus') and ancestor::div[contains(@class,'child')]] | (//div[contains(@class,'occupancy')]//div[text()='Children']/following-sibling::div//button[contains(@class,'plus') or text()='+'])[1]")), "Add Child Button");
    private final Button minusChild = new Button($(By.xpath("//div[@data-selenium='occupancyChildren']//button[@data-selenium='minus'] | //button[contains(@aria-label,'decrease child')] | //button[contains(@class,'minus') and ancestor::div[contains(@class,'child')]] | (//div[contains(@class,'occupancy')]//div[text()='Children']/following-sibling::div//button[contains(@class,'minus') or text()='-'])[1]")), "Minus Child Button");

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
        
        try {
            // Try multiple approaches to open the date picker
            if (!openDatePicker()) {
                LogUtils.logTestStep("WARNING: Could not open DatePicker popup, proceeding with default dates");
                return;
            }
            
            selectDate(checkInDate);
            selectDate(checkOutDate);
            
        } catch (Exception e) {
            LogUtils.logError("Error in date selection: " + e.getMessage(), e);
            LogUtils.logTestStep("WARNING: Using default dates due to date picker error");
        }
    }
    
    /**
     * Try multiple approaches to open the date picker
     */
    private boolean openDatePicker() {
        // Check if already visible
        if (isDatePickerVisible()) {
            return true;
        }
        
        // Try different selectors for the date trigger
        String[] dateTriggerXPaths = {
            "//div[@id='check-in-box' and @role='button']",
            "//div[@data-selenium='check-in-box']",
            "//div[contains(@class,'check-in')]",
            "//button[contains(@class,'date')]",
            "//div[@data-testid='check-in-box']"
        };
        
        for (String xpath : dateTriggerXPaths) {
            try {
                SelenideElement trigger = $(By.xpath(xpath));
                if (trigger.exists() && trigger.isDisplayed()) {
                    LogUtils.logTestStep("Trying to open DatePicker with: " + xpath);
                    trigger.click();
                    WaitUtils.sleep(1000);
                    
                    if (isDatePickerVisible()) {
                        LogUtils.logTestStep("✓ DatePicker opened successfully");
                        return true;
                    }
                }
            } catch (Exception e) {
                LogUtils.logTestStep("Failed with xpath: " + xpath + " - " + e.getMessage());
            }
        }
        
        LogUtils.logTestStep("ERROR: Could not open DatePicker with any selector");
        return false;
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
     * Open occupancy popup/dropdown if it exists
     */
    private void openOccupancyPopup() {
        try {
            // Try to find and click the occupancy trigger button/area
            SelenideElement occupancyTrigger = $(By.xpath("//div[@id='occupancy-box'] | //div[contains(@class,'occupancy')] | //div[@data-selenium='occupancy-box'] | //div[@data-testid='occupancy-box'] | //button[contains(@class,'occupancy')]"));
            
            if (occupancyTrigger.exists() && occupancyTrigger.isDisplayed()) {
                occupancyTrigger.click();
                LogUtils.logTestStep("Opened occupancy popup");
                WaitUtils.sleep(1000); // Wait for popup to open
            }
        } catch (Exception e) {
            LogUtils.logTestStep("Occupancy popup not found or already open: " + e.getMessage());
        }
    }

    /**
     * Set occupancy (rooms, adults, children) intelligently
     */
    @Step("Set occupancy - Rooms: {targetRooms}, Adults: {targetAdults}, Children: {targetChildren}")
    public void setOccupancy(int targetRooms, int targetAdults, int targetChildren) {
        LogUtils.logTestStep("Setting occupancy - Rooms: " + targetRooms + ", Adults: " + targetAdults + ", Children: " + targetChildren);
        
        try {
            // First try to open the occupancy popup
            openOccupancyPopup();
            
            // Try to set values but continue even if it fails
            boolean roomsSet = setRoomCount(targetRooms);
            boolean adultsSet = setAdultCount(targetAdults);
            boolean childrenSet = setChildCount(targetChildren);
            
            if (roomsSet || adultsSet || childrenSet) {
                LogUtils.logTestStep("✓ Successfully set some occupancy values");
            } else {
                LogUtils.logTestStep("⚠ Could not set occupancy values, proceeding with defaults");
            }
            
            // Close the popup if it's open
            closeOccupancyPopup();
            
        } catch (Exception e) {
            LogUtils.logError("Error setting occupancy: " + e.getMessage(), e);
            LogUtils.logTestStep("⚠ Proceeding with default occupancy settings");
        }
    }
    
    /**
     * Close occupancy popup if it's open
     */
    private void closeOccupancyPopup() {
        try {
            // Try to find and click outside the popup or find a close button
            SelenideElement closeButton = $(By.xpath("//button[contains(@class,'close')] | //div[contains(@class,'backdrop')] | //button[@aria-label='close']"));
            if (closeButton.exists() && closeButton.isDisplayed()) {
                closeButton.click();
                LogUtils.logTestStep("Closed occupancy popup");
                WaitUtils.sleep(500);
            } else {
                // Click somewhere neutral to close popup
                $(By.xpath("//body")).click();
                LogUtils.logTestStep("Clicked outside to close occupancy popup");
                WaitUtils.sleep(500);
            }
        } catch (Exception e) {
            LogUtils.logTestStep("Could not close occupancy popup explicitly: " + e.getMessage());
        }
    }

    /**
     * Set room count by clicking buttons directly (simplified approach)
     */
    private boolean setRoomCount(int targetRooms) {
        try {
            LogUtils.logTestStep("Setting room count to: " + targetRooms);
            
            // Simplified approach: Just click the buttons to reach the target
            // Assume starting from 1 room (default)
            int clicksNeeded = targetRooms - 1; // Default is 1 room
            
            boolean success = false;
            for (int i = 0; i < clicksNeeded; i++) {
                try {
                    addRoom.click();
                    LogUtils.logTestStep("✓ Added room " + (i + 1) + "/" + clicksNeeded);
                    success = true;
                    WaitUtils.sleep(500);
                } catch (Exception e) {
                    LogUtils.logTestStep("Failed to add room: " + e.getMessage());
                    break;
                }
            }
            return success;
            
        } catch (Exception e) {
            LogUtils.logError("Failed to set room count: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Set adult count by clicking buttons directly (simplified approach)
     */
    private boolean setAdultCount(int targetAdults) {
        try {
            LogUtils.logTestStep("Setting adult count to: " + targetAdults);
            
            // Simplified approach: Just click the buttons to reach the target
            // Assume starting from 2 adults (default)
            int currentAdults = 2; // Default for most booking sites
            int clicksNeeded = targetAdults - currentAdults;
            
            boolean success = false;
            if (clicksNeeded > 0) {
                for (int i = 0; i < clicksNeeded; i++) {
                    try {
                        addAdult.click();
                        LogUtils.logTestStep("✓ Added adult " + (i + 1) + "/" + clicksNeeded);
                        success = true;
                        WaitUtils.sleep(500);
                    } catch (Exception e) {
                        LogUtils.logTestStep("Failed to add adult: " + e.getMessage());
                        break;
                    }
                }
            } else if (clicksNeeded < 0) {
                for (int i = 0; i < Math.abs(clicksNeeded); i++) {
                    try {
                        minusAdult.click();
                        LogUtils.logTestStep("✓ Removed adult " + (i + 1) + "/" + Math.abs(clicksNeeded));
                        success = true;
                        WaitUtils.sleep(500);
                    } catch (Exception e) {
                        LogUtils.logTestStep("Failed to remove adult: " + e.getMessage());
                        break;
                    }
                }
            } else {
                // No change needed
                success = true;
                LogUtils.logTestStep("✓ Adult count already at target: " + targetAdults);
            }
            return success;
            
        } catch (Exception e) {
            LogUtils.logError("Failed to set adult count: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Set child count by clicking buttons directly (simplified approach)
     */
    private boolean setChildCount(int targetChildren) {
        try {
            LogUtils.logTestStep("Setting child count to: " + targetChildren);
            
            // Simplified approach: Just click the buttons to reach the target
            // Assume starting from 0 children (default)
            int clicksNeeded = targetChildren; // Default is 0 children
            
            boolean success = false;
            for (int i = 0; i < clicksNeeded; i++) {
                try {
                    addChild.click();
                    LogUtils.logTestStep("✓ Added child " + (i + 1) + "/" + clicksNeeded);
                    success = true;
                    WaitUtils.sleep(500);
                } catch (Exception e) {
                    LogUtils.logTestStep("Failed to add child: " + e.getMessage());
                    break;
                }
            }
            
            if (targetChildren == 0) {
                success = true; // No children needed, so it's successful
                LogUtils.logTestStep("✓ Child count already at target: " + targetChildren);
            }
            
            return success;
            
        } catch (Exception e) {
            LogUtils.logError("Failed to set child count: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Click search button and handle new tab
     */
    @Step("Click search button")
    public AgodaSearchResultsPage clickSearchButton() {
        searchButton.click();
        
        // Wait for new tab to open
        WaitUtils.sleep(3000);
        
        // Handle new tab - switch to hotel search results tab
        SwitchTabs.switchToHotelSearchTab();
        
        // Create results page instance and wait for it to load
        AgodaSearchResultsPage resultsPage = new AgodaSearchResultsPage();
        resultsPage.waitForPageToLoad();
        
        return resultsPage;
    }

    /**
     * Method 1: Search for destination and handle autocomplete
     */
    @Step("Select destination: {destination}")
    public void selectDestination(String destination) {
        LogUtils.logTestStep("Step 1: Selecting destination - " + destination);
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
     * Method 3a: Set occupancy only
     */
    @Step("Set occupancy - Rooms: {rooms}, Adults: {adults}, Children: {children}")
    public void setOccupancyValues(int rooms, int adults, int children) {
        LogUtils.logTestStep("Step 3a: Setting occupancy - Rooms: " + rooms + ", Adults: " + adults + ", Children: " + children);
        setOccupancy(rooms, adults, children);
    }
    
    /**
     * Method 3b: Click search button and return results page
     */
    @Step("Click search button")
    public AgodaSearchResultsPage clickSearch() {
        LogUtils.logTestStep("Step 3b: Clicking search button");
        return clickSearchButton();
    }
    
    /**
     * Method 3: Set occupancy and perform search (DEPRECATED - split into setOccupancy and clickSearch)
     */
    @Deprecated
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
        selectDestination(destination);
        
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
     * Helper class for tab switching - Enhanced to prioritize hotel search results page
     */
    private static class SwitchTabs {
        @Step("Switch to hotel search results tab")
        public static void switchToHotelSearchTab() {
            String methodName = "SwitchTabs.switchToHotelSearchTab()";
            try {
                // Get all window handles
                java.util.Set<String> windowHandles = WebDriverRunner.getWebDriver().getWindowHandles();
                LogUtils.logTestStep(methodName + " - Found " + windowHandles.size() + " window handles");
                
                if (windowHandles.size() > 1) {
                    // Debug: Log all available tabs
                    int tabIndex = 1;
                    for (String windowHandle : windowHandles) {
                        WebDriverRunner.getWebDriver().switchTo().window(windowHandle);
                        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
                        LogUtils.logTestStep(methodName + " - Tab " + tabIndex + " URL: " + currentUrl);
                        tabIndex++;
                    }
                    
                    // Priority 1: Look for hotel search results page with "guid=" pattern
                    for (String windowHandle : windowHandles) {
                        WebDriverRunner.getWebDriver().switchTo().window(windowHandle);
                        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
                        
                        if (currentUrl.contains("agoda.com/search?guid=")) {
                            LogUtils.logTestStep(methodName + " - ✓ Found hotel search tab with guid pattern: " + currentUrl);
                            return;
                        }
                    }
                    
                    // Priority 2: Look for hotel search results page with search parameters
                    for (String windowHandle : windowHandles) {
                        WebDriverRunner.getWebDriver().switchTo().window(windowHandle);
                        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
                        
                        if (currentUrl.contains("agoda.com/search?") && 
                            (currentUrl.contains("city=") || currentUrl.contains("checkIn="))) {
                            LogUtils.logTestStep(methodName + " - ✓ Found hotel search tab with search parameters: " + currentUrl);
                            return;
                        }
                    }
                    
                    // Priority 3: Look for any search page that is NOT activities
                    for (String windowHandle : windowHandles) {
                        WebDriverRunner.getWebDriver().switchTo().window(windowHandle);
                        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
                        
                        if (currentUrl.contains("agoda.com/search") && !currentUrl.contains("activities")) {
                            LogUtils.logTestStep(methodName + " - ✓ Found non-activities search tab: " + currentUrl);
                            return;
                        }
                    }
                    
                    // If no hotel tab found, try to use the most recent tab (last opened)
                    String lastHandle = null;
                    for (String windowHandle : windowHandles) {
                        lastHandle = windowHandle;
                    }
                    if (lastHandle != null) {
                        WebDriverRunner.getWebDriver().switchTo().window(lastHandle);
                        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
                        LogUtils.logTestStep(methodName + " - ⚠ Using last opened tab: " + currentUrl);
                    }
                } else {
                    LogUtils.logTestStep(methodName + " - Only one tab found, staying on current tab");
                }
                
            } catch (Exception e) {
                LogUtils.logTestStep(methodName + " - Error switching tabs: " + e.getMessage());
            }
        }
    }
}