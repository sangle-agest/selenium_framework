package tests.agoda;

import core.reporting.ScreenshotHandler;
import core.utils.LogUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.agoda.AgodaHomePage;
import pages.agoda.AgodaSearchResultsPage;

/**
 * TC 01: Search and Sort Hotel Successfully
 * 
 * Test Steps:
 * 1. Navigate to agoda.com
 * 2. Input destination: Phuket
 * 3. Input dates: Next Friday + 3 days
 * 4. Input travelers: Family Travelers → 2 rooms, 4 adults
 * 5. Click Search
 * 6. Sort by: Price (low to high)
 * 7. Verify: Hotels are displayed and sorted
 */
@Epic("Agoda Hotel Booking")
@Feature("Hotel Search and Sort")
@Story("TC01 - Search and Sort Hotel Successfully")
public class TC01_SearchAndSortHotelTest extends AgodaBaseTest {

    @Test(description = "Search for hotels in Phuket and sort by price (low to high)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can search for hotels in Phuket with specific dates and travelers, then sort results by price")
    public void testSearchAndSortHotel() {
        // Test Data
        String destination = "Phuket";
        String sortOption = "Price (low to high)";
        
        LogUtils.logTestStep("Starting TC01: Search and Sort Hotel Successfully");
        LogUtils.logTestData("Destination", destination);
        LogUtils.logTestData("Sort Option", sortOption);
        
        // Step 1: Navigate to Agoda homepage
        AgodaHomePage homePage = new AgodaHomePage();
        homePage.navigateToHomepage();
        
        // Verify homepage is loaded
        Assert.assertTrue(homePage.isHomepageDisplayed(), 
            "Agoda homepage should be displayed");
        LogUtils.logTestStep("✓ Agoda homepage loaded successfully");
        
        // Step 2-5: Perform hotel search
        AgodaSearchResultsPage resultsPage = homePage.searchHotels(destination);
        
        // Verify search results are displayed
        Assert.assertTrue(resultsPage.areResultsDisplayed(), 
            "Search results should be displayed");
        LogUtils.logTestStep("✓ Hotel search results displayed successfully");
        
        // Log number of hotels found
        int hotelCount = resultsPage.getHotelCount();
        LogUtils.logTestData("Number of hotels found", hotelCount);
        Assert.assertTrue(hotelCount > 0, 
            "At least one hotel should be found in search results");
        
        // Step 6: Sort by Price (low to high)
        resultsPage.sortBy(sortOption);
        
        // Step 7: Verify sorting was applied
        Assert.assertTrue(resultsPage.verifySortingApplied(), 
            "Sorting should be applied successfully");
        LogUtils.logTestStep("✓ Hotels sorted by price (low to high) successfully");
        
        // Get first hotel details after sorting
        String firstHotelDetails = resultsPage.getFirstHotelDetails();
        LogUtils.logTestData("First hotel after sorting", firstHotelDetails);
        
        // Final verification
        Assert.assertTrue(resultsPage.areResultsDisplayed(), 
            "Search results should still be displayed after sorting");
        
        LogUtils.logTestStep("✓ TC01: Search and Sort Hotel Successfully - PASSED");
        ScreenshotHandler.takeStepScreenshot("Search and Sort Results");
    }
}