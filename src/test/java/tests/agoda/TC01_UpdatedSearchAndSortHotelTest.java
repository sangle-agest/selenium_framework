package tests.agoda;

import core.utils.LogUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.agoda.AgodaHomePageUpdated;
import pages.agoda.AgodaSearchResultsPageUpdated;

/**
 * TC 01: Updated Search and Sort Hotel Test with correct XPaths
 * 
 * Test Steps:
 * 1. Navigate to agoda.com (handled by AgodaBaseTest)
 * 2. Input destination: Da Nang (using autocomplete)
 * 3. Input dates: Dynamic date selection  
 * 4. Input travelers: 2 rooms, 3 adults, 0 children
 * 5. Click Search (handles tab switching)
 * 6. Verify search results > 0
 * 7. Sort by: Lowest price first
 * 8. Verify: Top 5 results are sorted by price ascending
 */
@Epic("Agoda Hotel Booking")
@Feature("Hotel Search and Sort") 
@Story("TC01 - Updated Search and Sort Hotel Successfully")
public class TC01_UpdatedSearchAndSortHotelTest extends AgodaBaseTest {

    @Test(description = "Search for hotels in Da Nang and sort by price (lowest first)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can search for hotels in Da Nang with specific dates and travelers, then sort results by lowest price first")
    public void testSearchAndSortHotel() {
        // Test Data
        String destination = "Da Nang";
        String sortOption = "Lowest price first";
        
        // Dates - using simple date format (changed to October for better availability)
        String checkInDate = "2025-10-01";
        String checkOutDate = "2025-10-05";
        
        // Occupancy
        int rooms = 2;
        int adults = 3;
        int children = 0;
        
        LogUtils.logTestStep("Starting TC01: Updated Search and Sort Hotel Successfully");
        LogUtils.logTestData("Destination", destination);
        LogUtils.logTestData("Check-in Date", checkInDate);
        LogUtils.logTestData("Check-out Date", checkOutDate);
        LogUtils.logTestData("Rooms", String.valueOf(rooms));
        LogUtils.logTestData("Adults", String.valueOf(adults));
        LogUtils.logTestData("Children", String.valueOf(children));
        LogUtils.logTestData("Sort Option", sortOption);
        
        // Step 1: Initialize homepage (already navigated by AgodaBaseTest)
        AgodaHomePageUpdated homePage = new AgodaHomePageUpdated();
        
        // Verify homepage is loaded 
        Assert.assertTrue(homePage.isHomepageDisplayed(), 
            "Agoda homepage should be displayed");
        LogUtils.logTestStep("✓ Agoda homepage loaded successfully");
        
        // Option A: Use the 3 separate methods for detailed debugging
        // Step 2: Search for destination
        homePage.searchDestination(destination);
        
        // Step 3: Select travel dates
        homePage.selectTravelDates(checkInDate, checkOutDate);
        
        // Step 4: Set occupancy and perform search
        AgodaSearchResultsPageUpdated resultsPage = homePage.setOccupancyAndSearch(rooms, adults, children);
        
        // Option B: Use the simplified single method (commented out - you can choose)
        // AgodaSearchResultsPageUpdated resultsPage = homePage.searchHotels(
        //     destination, checkInDate, checkOutDate, rooms, adults, children);
        
        LogUtils.logTestStep("✓ Hotel search completed successfully");
        
        // Step 6: Verify search results
        resultsPage.verifySearchResults();
        LogUtils.logTestStep("✓ Search results verified");
        
        // Step 7: Sort by lowest price first
        resultsPage.sortBy(sortOption);
        LogUtils.logTestStep("✓ Applied sort: " + sortOption);
        
        // Step 8: Verify price sorting
        resultsPage.verifyPriceSorting();
        LogUtils.logTestStep("✓ Price sorting verification completed");
        
        LogUtils.logTestStep("TC01: Search and Sort Hotel test completed successfully");
    }
}