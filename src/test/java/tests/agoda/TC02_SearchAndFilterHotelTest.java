package tests.agoda;

import core.reporting.ScreenshotHandler;
import core.utils.LogUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.agoda.AgodaHomePage;
import pages.agoda.AgodaSearchResultsPage;

import java.time.DayOfWeek;

/**
 * TC 02: Search and Filter Hotel Successfully
 * 
 * Test Steps:
 * 1. Navigate to agoda.com
 * 2. Input destination: Phuket
 * 3. Input dates: Next Friday + 3 days
 * 4. Input travelers: Family Travelers → 2 rooms, 4 adults
 * 5. Click Search
 * 6. Filter by: Property type - Resort
 * 7. Filter by: Star rating - 5 stars
 * 8. Verify: Hotels are displayed with applied filters
 */
@Epic("Agoda Hotel Booking")
@Feature("Hotel Search and Filter")
@Story("TC02 - Search and Filter Hotel Successfully")
public class TC02_SearchAndFilterHotelTest extends AgodaBaseTest {

    @Test(description = "Search for hotels in Phuket and apply Resort and 5-star filters")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can search for hotels in Phuket with specific dates and travelers, then apply Resort and 5-star filters")
    public void testSearchAndFilterHotel() {
        // Test Data
        String destination = "Phuket";
        
        LogUtils.logTestStep("Starting TC02: Search and Filter Hotel Successfully");
        LogUtils.logTestData("Destination", destination);
        LogUtils.logTestData("Property Type Filter", "Resort");
        LogUtils.logTestData("Star Rating Filter", "5 stars");
        
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
        
        // Log initial number of hotels found
        int initialHotelCount = resultsPage.getHotelCount();
        LogUtils.logTestData("Initial number of hotels found", initialHotelCount);
        Assert.assertTrue(initialHotelCount > 0, 
            "At least one hotel should be found in search results");
        
        // Step 6: Filter by Property type - Resort
        resultsPage.applyResortFilter();
        LogUtils.logTestStep("✓ Resort filter applied successfully");
        
        // Verify results after resort filter
        Assert.assertTrue(resultsPage.areResultsDisplayed(), 
            "Search results should still be displayed after applying Resort filter");
        
        int hotelsAfterResortFilter = resultsPage.getHotelCount();
        LogUtils.logTestData("Hotels after Resort filter", hotelsAfterResortFilter);
        
        // Step 7: Filter by Star rating - 5 stars
        resultsPage.applyFiveStarFilter();
        LogUtils.logTestStep("✓ 5-star filter applied successfully");
        
        // Step 8: Verify filters were applied
        Assert.assertTrue(resultsPage.verifyFiltersApplied(), 
            "Filters should be applied successfully");
        LogUtils.logTestStep("✓ Both filters (Resort and 5-star) applied successfully");
        
        // Verify final results after all filters
        Assert.assertTrue(resultsPage.areResultsDisplayed(), 
            "Search results should still be displayed after applying all filters");
        
        int finalHotelCount = resultsPage.getHotelCount();
        LogUtils.logTestData("Final number of hotels after filters", finalHotelCount);
        
        // Get first hotel details after filtering
        String firstHotelDetails = resultsPage.getFirstHotelDetails();
        LogUtils.logTestData("First hotel after filtering", firstHotelDetails);
        
        // Log filter progression
        LogUtils.logTestStep("Filter progression: Initial(" + initialHotelCount + ") → Resort(" + hotelsAfterResortFilter + ") → 5-star(" + finalHotelCount + ")");
        
        LogUtils.logTestStep("✓ TC02: Search and Filter Hotel Successfully - PASSED");
        ScreenshotHandler.takeStepScreenshot("Search and Filter Results");
    }

    @Test(description = "Search for hotels in Phuket starting Sunday for 5 days and apply Resort and 5-star filters")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify flexible date selection with Sunday start and 5-day duration, then apply Resort and 5-star filters")
    public void testSearchAndFilterHotelFlexibleDates() {
        // Test Data - Using new flexible date functionality
        String destination = "Phuket";
        DayOfWeek startDay = DayOfWeek.SUNDAY;
        int duration = 5;
        
        LogUtils.logTestStep("Starting TC02: Search and Filter Hotel with Flexible Dates (Sunday + 5 days)");
        LogUtils.logTestData("Destination", destination);
        LogUtils.logTestData("Start Day", startDay.toString());
        LogUtils.logTestData("Duration", duration + " days");
        LogUtils.logTestData("Property Type Filter", "Resort");
        LogUtils.logTestData("Star Rating Filter", "5 stars");
        
        // Navigate to Agoda homepage
        AgodaHomePage homePage = new AgodaHomePage();
        homePage.navigateToHomepage();
        
        // Verify homepage is loaded
        Assert.assertTrue(homePage.isHomepageDisplayed(), 
            "Agoda homepage should be displayed");
        LogUtils.logTestStep("✓ Agoda homepage loaded successfully");
        
        // Perform hotel search with flexible dates
        AgodaSearchResultsPage resultsPage = homePage.searchHotels(destination, startDay, duration);
        
        // Verify search results are displayed
        Assert.assertTrue(resultsPage.areResultsDisplayed(), 
            "Search results should be displayed");
        LogUtils.logTestStep("✓ Hotel search results displayed successfully with flexible dates");
        
        // Log initial number of hotels found
        int initialHotelCount = resultsPage.getHotelCount();
        LogUtils.logTestData("Initial number of hotels found", initialHotelCount);
        Assert.assertTrue(initialHotelCount > 0, 
            "At least one hotel should be found in search results");
        
        // Apply filters
        resultsPage.applyResortFilter();
        LogUtils.logTestStep("✓ Resort filter applied successfully");
        
        resultsPage.applyFiveStarFilter();
        LogUtils.logTestStep("✓ 5-star filter applied successfully");
        
        // Verify filters were applied
        Assert.assertTrue(resultsPage.verifyFiltersApplied(), 
            "Filters should be applied successfully");
        
        int finalHotelCount = resultsPage.getHotelCount();
        LogUtils.logTestData("Final number of hotels after filters", finalHotelCount);
        
        // Get first hotel details after filtering
        String firstHotelDetails = resultsPage.getFirstHotelDetails();
        LogUtils.logTestData("First hotel after filtering", firstHotelDetails);
        
        LogUtils.logTestStep("✓ TC02: Search and Filter Hotel with Flexible Dates (Sunday + 5 days) - PASSED");
        ScreenshotHandler.takeStepScreenshot("Flexible Date Search and Filter Results");
    }
}