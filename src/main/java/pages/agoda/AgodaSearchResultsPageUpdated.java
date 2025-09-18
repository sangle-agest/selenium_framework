package pages.agoda;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import core.utils.LogUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.*;

/**
 * Updated AgodaSearchResultsPage with correct XPaths for search results
 */
public class AgodaSearchResultsPageUpdated {
    private static final Logger logger = LoggerFactory.getLogger(AgodaSearchResultsPageUpdated.class);
    
    // Search results elements
    private final ElementsCollection searchResults = $$(By.xpath("//a[@data-testid='activities-card-content']"));
    private final ElementsCollection originalPrices = $$(By.xpath("//span[@data-testid='activities-original-price']"));
    
    public AgodaSearchResultsPageUpdated() {
        logger.info("Initialized AgodaSearchResultsPageUpdated");
    }
    
    /**
     * Verify search results are displayed
     */
    @Step("Verify search results are displayed")
    public void verifySearchResults() {
        LogUtils.logTestStep("Verifying search results are displayed");
        
        int resultCount = searchResults.size();
        Assert.assertTrue(resultCount > 0, "No search results found! Expected > 0 but found: " + resultCount);
        
        LogUtils.logTestStep("Search results verified: " + resultCount + " results found");
    }
    
    /**
     * Sort results by price (lowest first)
     */
    @Step("Sort by: {sortOption}")
    public void sortBy(String sortOption) {
        LogUtils.logTestStep("Sorting by: " + sortOption);
        
        // Dynamic XPath for sort option
        String xpath = String.format("//label[@data-testid='activities-sort-option']//input[@aria-label='%s']", sortOption);
        SelenideElement sortElement = $(By.xpath(xpath));
        
        sortElement.click();
        LogUtils.logTestStep("Applied sort: " + sortOption);
        
        // Wait for results to update
        sleep(2000);
    }
    
    /**
     * Verify price sorting (top 5 results should be in ascending order)
     */
    @Step("Verify price sorting is correct")
    public void verifyPriceSorting() {
        LogUtils.logTestStep("Verifying price sorting for top 5 results");
        
        // Get top 5 price elements
        int maxResults = Math.min(5, originalPrices.size());
        
        double previousPrice = 0;
        for (int i = 0; i < maxResults; i++) {
            SelenideElement priceElement = originalPrices.get(i);
            String priceText = priceElement.getText();
            double currentPrice = extractPriceValue(priceText);
            
            LogUtils.logTestStep("Result " + (i + 1) + " price: " + priceText + " (value: " + currentPrice + ")");
            
            if (i > 0) {
                Assert.assertTrue(currentPrice >= previousPrice, 
                    "Price sorting is incorrect! Price " + currentPrice + " should be >= " + previousPrice);
            }
            
            previousPrice = currentPrice;
        }
        
        LogUtils.logTestStep("Price sorting verification passed for top 5 results");
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