package tests.agoda;

import core.data.models.AgodaTestData;
import core.data.providers.AgodaTestDataProvider;
import core.utils.LogUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.agoda.AgodaHomePage;
import pages.agoda.AgodaSearchResultsPage;

/**
 * TC 01: Search and Sort Hotel Test with JSON test data
 * 
 * Test Steps:
 * 1. Navigate to agoda.com (handled by AgodaBaseTest)
 * 2. Load test data from JSON file using DataProvider
 * 3. Input destination: Da Nang (using autocomplete)
 * 4. Input dates: Dynamic date selection  
 * 5. Input travelers: 2 rooms, 3 adults, 0 children
 * 6. Click Search (handles tab switching)
 * 7. Verify search results > 0
 * 8. Sort by: Lowest price first
 * 9. Verify: Top 5 results are sorted by price ascending
 */
@Epic("Agoda Hotel Booking")
@Feature("Hotel Search and Sort") 
@Story("TC01 - Search and Sort Hotel Successfully")
public class TC01_SearchAndSortHotelTest extends AgodaBaseTest {

    @Test(description = "Search for hotels in Da Nang and sort by price (lowest first)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can search for hotels in Da Nang with specific dates and travelers, then sort results by lowest price first")
    public void testSearchAndSortHotel() {
        // Load test data from JSON file
        AgodaTestData testData = AgodaTestDataProvider.getTestData("TC01_SearchAndSortHotel");
        
        // Validate required test data
        Assert.assertTrue(AgodaTestDataProvider.validateTestData(testData, "destination", "checkInDate", "checkOutDate", "occupancy"),
            "Test data validation failed");
        
        // Log test data information automatically
        AgodaTestDataProvider.logTestDataInfo(testData);
        
        // Extract test data for easier access
        String destination = testData.getDestination();
        String checkInDate = testData.getCheckInDate();
        String checkOutDate = testData.getCheckOutDate();
        int rooms = testData.getOccupancy().getRooms();
        int adults = testData.getOccupancy().getAdults();
        int children = testData.getOccupancy().getChildren();
        String sortOption = testData.getSortOption();
        
        LogUtils.logTestStep("Starting TC01: Search and Sort Hotel test with JSON test data");
        
        // Step 1: Initialize homepage (already navigated by AgodaBaseTest)
        AgodaHomePage homePage = new AgodaHomePage();
        
        // Verify homepage is loaded
        verifyHomepageIsDisplayed(homePage);
        
        // Step 2: Search for destination
        homePage.searchDestination(destination);
        
        // Step 3: Select travel dates
        homePage.selectTravelDates(checkInDate, checkOutDate);
        
        // Step 4: Set occupancy and perform search
        AgodaSearchResultsPage resultsPage = homePage.setOccupancyAndSearch(rooms, adults, children);
        
        LogUtils.logTestStep("✓ Hotel search completed successfully");
        
        // Step 5: Verify search results
        verifySearchResults(resultsPage);
        
        // Step 6: Sort by lowest price first
        resultsPage.sortBy(sortOption);
        LogUtils.logTestStep("✓ Applied sort: " + sortOption);
        
        // Step 7: Verify price sorting
        verifyPriceSorting(resultsPage);
        
        LogUtils.logTestStep("TC01: Search and Sort Hotel test completed successfully");
    }
    
    /**
     * Verify homepage is displayed with detailed Allure reporting
     * @param homePage the homepage object
     */
    @Step("Verify Agoda homepage is displayed")
    private void verifyHomepageIsDisplayed(AgodaHomePage homePage) {
        boolean isDisplayed = homePage.isHomepageDisplayed();
        
        Assert.assertTrue(isDisplayed, 
            "Agoda homepage is not displayed! Please check if navigation was successful " +
            "and all required page elements are loaded.");
        
        LogUtils.logVerificationStep("✓ Agoda homepage loaded successfully");
    }
    
    /**
     * Verify search results are available with detailed Allure reporting
     * @param resultsPage the search results page object
     */
    @Step("Verify search results are displayed")
    private void verifySearchResults(AgodaSearchResultsPage resultsPage) {
        boolean hasResults = resultsPage.hasSearchResults();
        int resultCount = resultsPage.getSearchResultCount();
        
        Assert.assertTrue(hasResults, 
            "No search results found! Expected search results > 0 but found: " + resultCount + 
            ". Please check if destination, dates, or search criteria are valid.");
        
        LogUtils.logVerificationStep("✓ Search results verified: " + resultCount + " results found");
    }
    
    /**
     * Verify price sorting is correct with detailed Allure reporting
     * @param resultsPage the search results page object
     */
    @Step("Verify price sorting is correct (lowest price first)")
    private void verifyPriceSorting(AgodaSearchResultsPage resultsPage) {
        AgodaSearchResultsPage.PriceSortingResult sortingResult = resultsPage.checkPriceSorting();
        
        Assert.assertTrue(sortingResult.isCorrect(), 
            "Price sorting verification failed! " + sortingResult.getErrorMessage() + 
            ". Checked " + sortingResult.getCheckedResults() + " results. Details: " + 
            sortingResult.getDetails());
        
        LogUtils.logVerificationStep("✓ Price sorting verification completed successfully for " + 
                           sortingResult.getCheckedResults() + " results");
    }
}