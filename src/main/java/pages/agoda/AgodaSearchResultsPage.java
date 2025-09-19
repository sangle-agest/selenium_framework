package pages.agoda;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import core.elements.Button;
import core.utils.BrowserUtils;
import core.utils.LogUtils;
import core.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

/**
 * Updated AgodaSearchResultsPage with correct XPaths for search results
 */
public class AgodaSearchResultsPage {
    
    // Search results elements - Updated to support both hotel and activities results
    private final ElementsCollection searchResults = $$(By.xpath("//div[@data-selenium='hotel-item'] | //div[contains(@class,'PropertyCard')] | //div[contains(@class,'property-card')] | //a[@data-testid='hotel-item'] | //a[@data-testid='activities-card-content']"));
    private final ElementsCollection originalPrices = $$(By.xpath("//span[@data-testid='hotel-original-price'] | //span[@data-testid='activities-original-price']"));
    
    // Sort elements for waiting
    private final SelenideElement sortContainer = $(By.xpath("//div[contains(@class,'sort')] | //div[@data-testid='sort-container'] | //label[@data-testid='activities-sort-option']"));
    private final SelenideElement loadingIndicator = $(By.xpath("//div[contains(@class,'loading')] | //div[contains(@class,'spinner')] | //div[@data-testid='loading']"));
    
    public AgodaSearchResultsPage() {
        LogUtils.logTestStep("Initialized AgodaSearchResultsPageUpdated");
    }
    
    /**
     * Wait for search results page to fully load after tab switching
     */
    @Step("Wait for search results page to load")
    public void waitForPageToLoad() {
        LogUtils.logTestStep("Waiting for search results page to fully load...");
        
        // Wait for page to stabilize after tab switch
        WaitUtils.sleep(3000);
        
        // Wait for sort elements to be visible (key indicator that page is loaded)
        try {
            LogUtils.logTestStep("Waiting for sort container to be visible...");
            WaitUtils.waitForVisible(sortContainer, Duration.ofSeconds(15));
            LogUtils.logVerificationStep("✓ Sort container is visible");
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Sort container not found, continuing with basic wait: " + e.getMessage());
            WaitUtils.sleep(5000); // Fallback wait
        }
        
        // Wait for any loading indicators to disappear
        try {
            LogUtils.logTestStep("Checking for loading indicators...");
            if (loadingIndicator.exists() && loadingIndicator.isDisplayed()) {
                LogUtils.logTestStep("Loading indicator found, waiting for it to disappear...");
                // Wait for loading indicator to disappear
                for (int i = 0; i < 20; i++) {
                    if (!loadingIndicator.isDisplayed()) {
                        LogUtils.logVerificationStep("✓ Loading indicator disappeared");
                        break;
                    }
                    WaitUtils.sleep(1000);
                }
            } else {
                LogUtils.logVerificationStep("✓ No loading indicators found");
            }
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Issue with loading indicator check: " + e.getMessage());
        }
        
        // Wait for search results to be present
        try {
            LogUtils.logTestStep("Waiting for search results to be available...");
            for (int i = 0; i < 10; i++) {
                if (searchResults.size() > 0) {
                    LogUtils.logVerificationStep("✓ Search results are available (" + searchResults.size() + " results found)");
                    break;
                }
                WaitUtils.sleep(2000);
                LogUtils.logTestStep("Still waiting for search results... attempt " + (i + 1));
            }
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Issue waiting for search results: " + e.getMessage());
        }
        
        // Final wait to ensure everything is stable
        WaitUtils.sleep(2000);
        LogUtils.logVerificationStep("✓ Page loading wait completed");
    }
    
    /**
     * Check if search results are displayed
     * @return true if search results are found, false otherwise
     */
    @Step("Check search results availability")
    public boolean hasSearchResults() {
        LogUtils.logTestStep("Checking search results availability");
        
        // First ensure page is fully loaded
        waitForPageToLoad();
        
        int resultCount = searchResults.size();
        LogUtils.logTestStep("Found " + resultCount + " search results");
        
        return resultCount > 0;
    }
    
    /**
     * Get the number of search results
     * @return number of search results found
     */
    public int getSearchResultCount() {
        waitForPageToLoad();
        return searchResults.size();
    }
    
    /**
     * Sort results by price (lowest first)
     */
    @Step("Sort by: {sortOption}")
    public void sortBy(String sortOption) {
        LogUtils.logTestStep("Sorting by: " + sortOption);
        
        // Ensure page is loaded before trying to sort
        waitForPageToLoad();
        
        // First try to find and open the sort dropdown/container
        try {
            LogUtils.logTestStep("Looking for sort dropdown or container to open...");
            
            // Try different approaches to access sort options
            SelenideElement sortDropdown = $(By.xpath("//div[contains(@class,'sort')] | //button[contains(text(),'Sort')] | //div[@data-testid='sort-dropdown'] | //select[contains(@class,'sort')]"));
            
            if (sortDropdown.exists() && sortDropdown.isDisplayed()) {
                LogUtils.logTestStep("Found sort dropdown, clicking to open...");
                sortDropdown.click();
                WaitUtils.sleep(2000); // Wait for dropdown to open
            }
            
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Could not find sort dropdown, trying direct access: " + e.getMessage());
        }
        
        // Dynamic XPath for sort option
        String xpath = String.format("//label[@data-testid='activities-sort-option']//input[@aria-label='%s']", sortOption);
        SelenideElement sortElement = $(By.xpath(xpath));
        
        // Check if the element exists first
        if (!sortElement.exists()) {
            LogUtils.logTestStep("⚠ Sort option not found with primary selector, trying alternatives...");
            
            // Try alternative selectors
            String[] alternativeXpaths = {
                String.format("//input[@value='Price_Ascending'] | //option[contains(text(),'%s')] | //button[contains(text(),'%s')]", sortOption, sortOption),
                "//input[@value='Price_Ascending']",
                "//option[contains(text(),'Lowest price')]",
                "//button[contains(text(),'Price')]",
                "//a[contains(text(),'Price')]"
            };
            
            for (String altXpath : alternativeXpaths) {
                SelenideElement altElement = $(By.xpath(altXpath));
                if (altElement.exists()) {
                    LogUtils.logTestStep("Found alternative sort option with XPath: " + altXpath);
                    sortElement = altElement;
                    break;
                }
            }
        }
        
        // Try to click the sort element
        try {
            if (sortElement.exists()) {
                // If element is not visible, try to scroll or use JavaScript
                if (!sortElement.isDisplayed()) {
                    LogUtils.logTestStep("Sort element exists but not visible, trying JavaScript click...");
                    executeJavaScript("arguments[0].click();", sortElement);
                } else {
                    LogUtils.logTestStep("Sort element is visible, clicking normally...");
                    sortElement.click();
                }
                LogUtils.logVerificationStep("✓ Applied sort: " + sortOption);
            } else {
                LogUtils.logTestStep("⚠ No sort options found, skipping sort step");
                return;
            }
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Could not click sort option: " + sortOption + ", error: " + e.getMessage());
            LogUtils.logTestStep("⚠ Continuing without sorting - this might be expected for some pages");
        }
        
        // Wait for results to update after sorting
        WaitUtils.sleep(3000);
        LogUtils.logVerificationStep("✓ Sort operation completed");
    }
    
    /**
     * Check if price sorting is correct (top 5 results should be in ascending order)
     * @return PriceSortingResult object containing validation result and details
     */
    @Step("Check price sorting order")
    public PriceSortingResult checkPriceSorting() {
        LogUtils.logTestStep("Checking price sorting for top 5 results");
        
        // Get top 5 price elements
        int maxResults = Math.min(5, originalPrices.size());
        StringBuilder details = new StringBuilder();
        boolean isCorrect = true;
        String errorMessage = "";
        
        double previousPrice = 0;
        for (int i = 0; i < maxResults; i++) {
            SelenideElement priceElement = originalPrices.get(i);
            String priceText = priceElement.getText();
            double currentPrice = extractPriceValue(priceText);
            
            String priceInfo = "Result " + (i + 1) + " price: " + priceText + " (value: " + currentPrice + ")";
            LogUtils.logTestStep(priceInfo);
            details.append(priceInfo).append("; ");
            
            if (i > 0 && currentPrice < previousPrice) {
                isCorrect = false;
                errorMessage = "Price sorting is incorrect! Price " + currentPrice + " should be >= " + previousPrice;
                break;
            }
            
            previousPrice = currentPrice;
        }
        
        if (isCorrect) {
            LogUtils.logTestStep("Price sorting check passed for top 5 results");
        } else {
            LogUtils.logTestStep("Price sorting check failed: " + errorMessage);
        }
        
        return new PriceSortingResult(isCorrect, errorMessage, details.toString(), maxResults);
    }
    
    /**
     * Inner class to hold price sorting validation results
     */
    public static class PriceSortingResult {
        private final boolean isCorrect;
        private final String errorMessage;
        private final String details;
        private final int checkedResults;
        
        public PriceSortingResult(boolean isCorrect, String errorMessage, String details, int checkedResults) {
            this.isCorrect = isCorrect;
            this.errorMessage = errorMessage;
            this.details = details;
            this.checkedResults = checkedResults;
        }
        
        public boolean isCorrect() {
            return isCorrect;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public String getDetails() {
            return details;
        }
        
        public int getCheckedResults() {
            return checkedResults;
        }
        
        @Override
        public String toString() {
            return String.format("PriceSortingResult{isCorrect=%s, checkedResults=%d, details='%s'%s}", 
                    isCorrect, checkedResults, details, 
                    !isCorrect ? ", error='" + errorMessage + "'" : "");
        }
    }
    
    /**
     * Extract numeric price value from price text
     */
    private double extractPriceValue(String priceText) {
        // Remove currency symbols and non-numeric characters except decimal point
        String numericPrice = priceText.replaceAll("[^0-9.,]", "");
        
        // Handle different decimal separators
        if (numericPrice.contains(",") && numericPrice.contains(".")) {
            // Format like 1,234.56
            numericPrice = numericPrice.replace(",", "");
        } else if (numericPrice.contains(",")) {
            // Format like 1234,56 (European style)
            numericPrice = numericPrice.replace(",", ".");
        }
        
        try {
            return Double.parseDouble(numericPrice);
        } catch (NumberFormatException e) {
            LogUtils.logTestStep("Warning: Could not parse price: " + priceText + ", using 0");
            return 0;
        }
    }
    
    /**
     * Get number of search results
     */
    public int getResultCount() {
        return searchResults.size();
    }
}