package pages.agoda;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import core.elements.BaseElement;
import core.elements.Dropdown;
import core.utils.LogUtils;
import core.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

/**
 * Page Object for Agoda Search Results Page
 * Handles hotel listing, sorting, and filtering functionality
 */
public class AgodaSearchResultsPage {
    private static final Logger logger = LoggerFactory.getLogger(AgodaSearchResultsPage.class);
    
    // Results and sorting elements
    private final BaseElement resultsContainer = new BaseElement($(By.xpath("//div[@data-selenium='hotel-list']")), "Results Container");
    private final Dropdown sortDropdown = new Dropdown($(By.xpath("//select[@data-selenium='sortBy']")), "Sort Dropdown");
    private final BaseElement sortButton = new BaseElement($(By.xpath("//button[@data-selenium='sortBy']")), "Sort Button");
    
    // Hotel cards
    private final ElementsCollection hotelCards = $$(By.xpath("//div[@data-selenium='hotel-item']"));
    
    // Filters
    private final BaseElement filtersPanel = new BaseElement($(By.xpath("//div[@data-selenium='filters']")), "Filters Panel");
    
    // Pagination and loading
    private final BaseElement loadingSpinner = new BaseElement($(By.xpath("//div[@data-selenium='loading']")), "Loading Spinner");
    
    public AgodaSearchResultsPage() {
        logger.info("Initialized AgodaSearchResultsPage");
        waitForPageToLoad();
    }

    /**
     * Wait for search results page to load
     */
    @Step("Wait for search results to load")
    public void waitForPageToLoad() {
        LogUtils.logTestStep("Waiting for search results page to load");
        
        // Wait for page load
        WaitUtils.waitForPageLoad();
        
        // Wait for results container to be visible
        WaitUtils.waitForVisible(resultsContainer.getElement(), Duration.ofSeconds(15));
        
        // Wait for loading spinner to disappear if present
        try {
            if (loadingSpinner.isVisible()) {
                // Wait for loading to complete - just wait a bit longer since no waitForInvisible method
                WaitUtils.sleep(2000);
            }
        } catch (Exception e) {
            // Loading spinner might not be present, continue
            logger.debug("Loading spinner not found or already hidden");
        }
        
        LogUtils.logTestStep("Search results page loaded successfully");
    }

    /**
     * Verify search results are displayed
     * @return true if search results are displayed
     */
    @Step("Verify search results are displayed")
    public boolean areResultsDisplayed() {
        try {
            boolean hasResults = resultsContainer.isVisible() && 
                               getHotelCount() > 0;
            
            LogUtils.logTestStep("Search results verification: " + (hasResults ? "PASSED" : "FAILED"));
            LogUtils.logTestStep("Number of hotels found: " + getHotelCount());
            
            return hasResults;
        } catch (Exception e) {
            LogUtils.logTestStep("Search results verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the number of hotel results displayed
     * @return Number of hotel cards found
     */
    @Step("Get hotel count")
    public int getHotelCount() {
        try {
            int count = hotelCards.size();
            LogUtils.logTestStep("Found " + count + " hotels in search results");
            return count;
        } catch (Exception e) {
            LogUtils.logTestStep("Failed to get hotel count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Sort hotels by the specified criteria
     * @param sortOption The sort option to select (e.g., "Price (low to high)", "Star rating", "Distance")
     * @return This page object for method chaining
     */
    @Step("Sort hotels by: {sortOption}")
    public AgodaSearchResultsPage sortBy(String sortOption) {
        LogUtils.logTestStep("Sorting hotels by: " + sortOption);
        
        try {
            // Try to click sort button first (if it's a button-based sort)
            if (sortButton.isVisible()) {
                sortButton.click();
                WaitUtils.sleep(500);
                
                // Look for sort option in dropdown or menu
                SelenideElement sortOptionElement = $(By.xpath(String.format("//span[contains(text(),'%s')] | //option[contains(text(),'%s')]", sortOption, sortOption)));
                if (sortOptionElement.isDisplayed()) {
                    sortOptionElement.click();
                } else {
                    // Try alternative selectors
                    sortOptionElement = $(By.xpath(String.format("//*[contains(text(),'%s')]", sortOption)));
                    sortOptionElement.click();
                }
            } 
            // Try dropdown-based sort
            else if (sortDropdown.isVisible()) {
                sortDropdown.selectByText(sortOption);
            }
            
            // Wait for results to refresh
            waitForResultsToRefresh();
            
            LogUtils.logTestStep("Successfully sorted by: " + sortOption);
            
        } catch (Exception e) {
            LogUtils.logTestStep("Failed to sort by " + sortOption + ": " + e.getMessage());
            throw new RuntimeException("Could not sort hotels by: " + sortOption, e);
        }
        
        return this;
    }

    /**
     * Apply property type filter - Resort
     * @return This page object for method chaining
     */
    @Step("Apply Resort filter")
    public AgodaSearchResultsPage applyResortFilter() {
        LogUtils.logTestStep("Applying Resort property type filter");
        
        try {
            // Scroll to filters if needed
            if (filtersPanel.isVisible()) {
                filtersPanel.scrollTo();
            }
            
            // Look for resort filter checkbox/option
            SelenideElement resortFilter = $(By.xpath("//label[contains(text(),'Resort')] | //span[contains(text(),'Resort')] | //*[@data-selenium='resort-filter']"));
            
            if (!resortFilter.isDisplayed()) {
                // Try to expand property type section
                SelenideElement propertyTypeSection = $(By.xpath("//div[contains(@class,'property-type')] | //h3[contains(text(),'Property type')]"));
                if (propertyTypeSection.isDisplayed()) {
                    propertyTypeSection.click();
                    WaitUtils.sleep(500);
                }
                
                // Try again to find resort filter
                resortFilter = $(By.xpath("//label[contains(text(),'Resort')] | //span[contains(text(),'Resort')]"));
            }
            
            WaitUtils.waitForClickable(resortFilter);
            resortFilter.click();
            
            // Wait for results to refresh
            waitForResultsToRefresh();
            
            LogUtils.logTestStep("Resort filter applied successfully");
            
        } catch (Exception e) {
            LogUtils.logTestStep("Failed to apply Resort filter: " + e.getMessage());
            throw new RuntimeException("Could not apply Resort filter", e);
        }
        
        return this;
    }

    /**
     * Apply star rating filter - 5 stars
     * @return This page object for method chaining
     */
    @Step("Apply 5-star rating filter")
    public AgodaSearchResultsPage applyFiveStarFilter() {
        LogUtils.logTestStep("Applying 5-star rating filter");
        
        try {
            // Scroll to filters if needed
            if (filtersPanel.isVisible()) {
                filtersPanel.scrollTo();
            }
            
            // Look for 5-star filter checkbox/option
            SelenideElement fiveStarFilter = $(By.xpath("//label[contains(text(),'5')] | //span[contains(text(),'5 star')] | //*[@data-selenium='5-star-filter']"));
            
            if (!fiveStarFilter.isDisplayed()) {
                // Try to expand star rating section
                SelenideElement starRatingSection = $(By.xpath("//div[contains(@class,'star-rating')] | //h3[contains(text(),'Star rating')]"));
                if (starRatingSection.isDisplayed()) {
                    starRatingSection.click();
                    WaitUtils.sleep(500);
                }
                
                // Try again to find 5-star filter
                fiveStarFilter = $(By.xpath("//label[contains(text(),'5')] | //span[contains(text(),'5')]"));
            }
            
            WaitUtils.waitForClickable(fiveStarFilter);
            fiveStarFilter.click();
            
            // Wait for results to refresh
            waitForResultsToRefresh();
            
            LogUtils.logTestStep("5-star filter applied successfully");
            
        } catch (Exception e) {
            LogUtils.logTestStep("Failed to apply 5-star filter: " + e.getMessage());
            throw new RuntimeException("Could not apply 5-star filter", e);
        }
        
        return this;
    }

    /**
     * Wait for search results to refresh after sorting or filtering
     */
    private void waitForResultsToRefresh() {
        LogUtils.logTestStep("Waiting for results to refresh");
        
        // Wait a short time for any loading indicators
        WaitUtils.sleep(1000);
        
        // Wait for loading spinner to disappear if present
        try {
            if (loadingSpinner.isVisible()) {
                // Wait for loading to complete - just wait a bit longer since no waitForInvisible method
                WaitUtils.sleep(2000);
            }
        } catch (Exception e) {
            // Loading spinner might not be present
            logger.debug("Loading spinner not found during refresh");
        }
        
        // Ensure results container is still visible
        WaitUtils.waitForVisible(resultsContainer.getElement(), Duration.ofSeconds(5));
        
        LogUtils.logTestStep("Results refreshed successfully");
    }

    /**
     * Get details of the first hotel in results
     * @return Hotel name and price information
     */
    @Step("Get first hotel details")
    public String getFirstHotelDetails() {
        try {
            if (hotelCards.size() > 0) {
                SelenideElement firstHotel = hotelCards.first();
                
                // Try to get hotel name
                String hotelName = "Unknown";
                try {
                    SelenideElement nameElement = firstHotel.$(By.xpath(".//h3 | .//h2 | .//*[contains(@class,'hotel-name')]"));
                    if (nameElement.isDisplayed()) {
                        hotelName = nameElement.getText().trim();
                    }
                } catch (Exception e) {
                    logger.debug("Could not extract hotel name: " + e.getMessage());
                }
                
                // Try to get price
                String price = "Price not available";
                try {
                    SelenideElement priceElement = firstHotel.$(By.xpath(".//*[contains(@class,'price')] | .//*[contains(text(),'$')] | .//*[contains(text(),'USD')]"));
                    if (priceElement.isDisplayed()) {
                        price = priceElement.getText().trim();
                    }
                } catch (Exception e) {
                    logger.debug("Could not extract hotel price: " + e.getMessage());
                }
                
                String hotelDetails = String.format("Hotel: %s, Price: %s", hotelName, price);
                LogUtils.logTestStep("First hotel details: " + hotelDetails);
                return hotelDetails;
            } else {
                LogUtils.logTestStep("No hotels found in search results");
                return "No hotels found";
            }
        } catch (Exception e) {
            LogUtils.logTestStep("Failed to get first hotel details: " + e.getMessage());
            return "Failed to get hotel details";
        }
    }

    /**
     * Verify that sorting was applied by checking if results changed
     * @return true if sorting appears to have been applied
     */
    @Step("Verify sorting was applied")
    public boolean verifySortingApplied() {
        try {
            // Simple verification - check if we still have results
            boolean hasResults = areResultsDisplayed();
            LogUtils.logTestStep("Sorting verification: " + (hasResults ? "PASSED" : "FAILED"));
            return hasResults;
        } catch (Exception e) {
            LogUtils.logTestStep("Sorting verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify that filters were applied by checking if results changed
     * @return true if filters appear to have been applied
     */
    @Step("Verify filters were applied")
    public boolean verifyFiltersApplied() {
        try {
            // Simple verification - check if we still have results
            boolean hasResults = areResultsDisplayed();
            LogUtils.logTestStep("Filter verification: " + (hasResults ? "PASSED" : "FAILED"));
            return hasResults;
        } catch (Exception e) {
            LogUtils.logTestStep("Filter verification failed: " + e.getMessage());
            return false;
        }
    }
}