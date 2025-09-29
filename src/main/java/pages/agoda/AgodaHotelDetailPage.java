package pages.agoda;

import com.codeborne.selenide.SelenideElement;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class AgodaHotelDetailPage {
        private final SelenideElement hotelName = $(By.xpath("//h1[@data-testid='hotel-name'] | //h1[contains(@class,'hotel-name')] | //h1[@id='hotel-name']"));
    private final SelenideElement hotelLocation = $(By.xpath("//span[@data-testid='hotel-location'] | //div[contains(@class,'location')] | //span[contains(@class,'address')]"));
    private final SelenideElement swimmingPoolIndicator = $(By.xpath("//span[contains(text(),'Swimming pool')] | //div[contains(@class,'amenity')][contains(text(),'Pool')] | //i[contains(@class,'pool')]"));
    private final SelenideElement backToResultsButton = $(By.xpath("//button[contains(text(),'Back')] | //a[contains(@class,'back')] | //button[@data-testid='back-button']"));
    
    // Amenities section
    private final SelenideElement amenitiesSection = $(By.xpath("//div[contains(@class,'amenities')] | //section[@data-testid='amenities'] | //div[@id='amenities']"));
    
    public AgodaHotelDetailPage() {
        LogUtils.logTestStep("Initialized AgodaHotelDetailPage");
    }
    
    /**
     * Get hotel name from detail page
     * @return Hotel name text
     */
    @Step("Get hotel name from detail page")
    public String getHotelName() {
        LogUtils.logTestStep("Getting hotel name from detail page");
        try {
            String name = hotelName.getText();
            LogUtils.logTestStep("✓ Hotel name: " + name);
            return name;
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Could not get hotel name: " + e.getMessage());
            return "Name not found";
        }
    }
    
    /**
     * Get hotel location/destination from detail page
     * @return Hotel location text
     */
    @Step("Get hotel location from detail page")
    public String getHotelLocation() {
        LogUtils.logTestStep("Getting hotel location from detail page");
        try {
            String location = hotelLocation.getText();
            LogUtils.logTestStep("✓ Hotel location: " + location);
            return location;
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Could not get hotel location: " + e.getMessage());
            return "Location not found";
        }
    }
    
    /**
     * Check if swimming pool amenity is displayed
     * @return true if swimming pool is indicated, false otherwise
     */
    @Step("Verify swimming pool amenity is displayed")
    public boolean hasSwimmingPool() {
        LogUtils.logTestStep("Checking if hotel has swimming pool amenity");
        try {
            boolean hasPool = swimmingPoolIndicator.exists() && swimmingPoolIndicator.isDisplayed();
            if (hasPool) {
                LogUtils.logVerificationStep("✓ Swimming pool amenity found");
            } else {
                LogUtils.logTestStep("⚠ Swimming pool amenity not found");
            }
            return hasPool;
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Error checking swimming pool amenity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all amenities for verification
     * @return String containing all amenities
     */
    @Step("Get all hotel amenities")
    public String getAllAmenities() {
        LogUtils.logTestStep("Getting all hotel amenities");
        try {
            String amenities = amenitiesSection.getText();
            LogUtils.logTestStep("✓ Hotel amenities: " + amenities);
            return amenities;
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Could not get amenities: " + e.getMessage());
            return "Amenities not found";
        }
    }
    
    /**
     * Click back to search results
     * @return AgodaSearchResultsPage instance
     */
    @Step("Navigate back to search results")
    public AgodaSearchResultsPage goBackToResults() {
        LogUtils.logTestStep("Navigating back to search results");
        try {
            if (backToResultsButton.exists() && backToResultsButton.isDisplayed()) {
                backToResultsButton.click();
                LogUtils.logTestStep("✓ Clicked back button");
            } else {
                LogUtils.logTestStep("Back button not found, using browser back");
                // Fallback to browser back
                executeJavaScript("window.history.back();");
            }
            LogUtils.logTestStep("✓ Navigated back to search results");
            return new AgodaSearchResultsPage();
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Error navigating back: " + e.getMessage());
            // Try browser back as fallback
            executeJavaScript("window.history.back();");
            return new AgodaSearchResultsPage();
        }
    }
    
    /**
     * Verify hotel details match expected criteria
     * @param expectedName Expected hotel name (can be partial)
     * @param expectedDestination Expected destination
     * @param shouldHaveSwimmingPool Whether hotel should have swimming pool
     * @return true if all details match, false otherwise
     */
    @Step("Verify hotel details")
    public boolean verifyHotelDetails(String expectedName, String expectedDestination, boolean shouldHaveSwimmingPool) {
        LogUtils.logTestStep("Verifying hotel details on detail page");
        
        String actualName = getHotelName();
        String actualLocation = getHotelLocation();
        boolean hasPool = hasSwimmingPool();
        
        boolean nameMatch = expectedName == null || actualName.toLowerCase().contains(expectedDestination.toLowerCase());
        boolean locationMatch = actualLocation.toLowerCase().contains(expectedDestination.toLowerCase());
        boolean poolMatch = shouldHaveSwimmingPool == hasPool;
        
        LogUtils.logTestStep("Hotel Detail Verification:");
        LogUtils.logTestStep("- Name contains destination: " + nameMatch + " (Expected: " + expectedDestination + ", Actual: " + actualName + ")");
        LogUtils.logTestStep("- Location match: " + locationMatch + " (Expected: " + expectedDestination + ", Actual: " + actualLocation + ")");
        LogUtils.logTestStep("- Swimming pool: " + poolMatch + " (Expected: " + shouldHaveSwimmingPool + ", Actual: " + hasPool + ")");
        
        boolean allMatch = nameMatch && locationMatch && poolMatch;
        
        if (allMatch) {
            LogUtils.logVerificationStep("✓ All hotel details verified successfully");
        } else {
            LogUtils.logTestStep("⚠ Some hotel details don't match expectations");
        }
        
        return allMatch;
    }
    
    /**
     * Check if we are on the hotel detail page
     * @return true if on detail page, false otherwise
     */
    @Step("Verify we are on hotel detail page")
    public boolean isOnHotelDetailPage() {
        try {
            return hotelName.exists() || amenitiesSection.exists();
        } catch (Exception e) {
            return false;
        }
    }
}