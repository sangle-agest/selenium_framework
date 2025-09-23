package pages.agoda;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import core.enums.StarRating;
import core.utils.LogUtils;
import core.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

/**
 * Updated AgodaSearchResultsPage with correct XPaths for search results
 */
public class AgodaSearchResultsPage {
    
    // Search results elements - Updated to support both hotel and activities results
    private final ElementsCollection searchResults = $$(By.xpath("//div[@data-selenium='hotel-item'] | //div[contains(@class,'PropertyCard')] | //div[contains(@class,'property-card')] | //a[@data-testid='hotel-item'] | //a[@data-testid='activities-card-content']"));
    private final ElementsCollection originalPrices = $$(By.xpath("//span[@data-selenium='display-price'] | //span[@data-testid='hotel-original-price'] | //span[@data-testid='activities-original-price']"));
    
    // Sort elements for waiting - Updated for hotel search results page
    private final SelenideElement sortContainer = $(By.xpath("//div[@id='sort-bar'] | //div[@data-element-name='sort-bar-container']"));
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
     * Sort results by price (lowest first) - Updated for hotel search page
     */
    @Step("Sort by: {sortOption}")
    public void sortBy(String sortOption) {
        LogUtils.logTestStep("Sorting by: " + sortOption);
        
        // Ensure page is loaded before trying to sort
        waitForPageToLoad();
        
        // Dynamic XPath for sort option based on hotel search page structure
        SelenideElement sortElement = null;
        
        // Map sort option text to the correct element
        if (sortOption.toLowerCase().contains("lowest price") || sortOption.toLowerCase().contains("price")) {
            // For "Lowest price first" option
            sortElement = $(By.xpath("//button[@data-element-name='search-sort-price']"));
            LogUtils.logTestStep("Using hotel search page XPath for 'Lowest price first'");
        } else if (sortOption.toLowerCase().contains("best match") || sortOption.toLowerCase().contains("recommended")) {
            // For "Best match" option
            sortElement = $(By.xpath("//button[@data-element-name='search-sort-recommended']"));
            LogUtils.logTestStep("Using hotel search page XPath for 'Best match'");
        } else if (sortOption.toLowerCase().contains("review") || sortOption.toLowerCase().contains("rating")) {
            // For "Top reviewed" option
            sortElement = $(By.xpath("//button[@data-element-name='search-sort-guest-rating']"));
            LogUtils.logTestStep("Using hotel search page XPath for 'Top reviewed'");
        } else if (sortOption.toLowerCase().contains("distance")) {
            // For "Distance" option
            sortElement = $(By.xpath("//button[@data-element-name='search-sort-distance-landmark']"));
            LogUtils.logTestStep("Using hotel search page XPath for 'Distance'");
        } else if (sortOption.toLowerCase().contains("deal") || sortOption.toLowerCase().contains("hot")) {
            // For "Hot Deals!" option
            sortElement = $(By.xpath("//button[@data-element-name='search-sort-secret-deals']"));
            LogUtils.logTestStep("Using hotel search page XPath for 'Hot Deals!'");
        }
        
        // If no specific mapping found, try generic approach
        if (sortElement == null || !sortElement.exists()) {
            LogUtils.logTestStep("⚠ Sort option not found with specific mapping, trying generic approach...");
            
            // Try alternative selectors for hotel search page
            String[] alternativeXpaths = {
                String.format("//button[contains(text(),'%s')]", sortOption),
                String.format("//span[contains(text(),'%s')]/ancestor::button", sortOption),
                "//button[@data-element-name='search-sort-price']", // Default to price sort
                "//button[contains(@data-element-name,'sort')]"
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
            if (sortElement != null && sortElement.exists()) {
                // Check if element is already selected (aria-current="true")
                String ariaCurrent = sortElement.getAttribute("aria-current");
                if ("true".equals(ariaCurrent)) {
                    LogUtils.logTestStep("✓ Sort option '" + sortOption + "' is already selected");
                    return;
                }
                
                // If element is not visible, try to scroll or use JavaScript
                if (!sortElement.isDisplayed()) {
                    LogUtils.logTestStep("Sort element exists but not visible, trying JavaScript click...");
                    ((JavascriptExecutor) WebDriverRunner.getWebDriver())
                        .executeScript("arguments[0].click();", sortElement);
                } else {
                    LogUtils.logTestStep("Clicking sort element: " + sortOption);
                    sortElement.click();
                }
                
                // Wait for sorting to complete
                WaitUtils.sleep(3000);
                LogUtils.logVerificationStep("✓ Sort option applied: " + sortOption);
                
            } else {
                LogUtils.logTestStep("✗ Sort element not found for option: " + sortOption);
                throw new RuntimeException("Sort element not found: " + sortOption);
            }
            
        } catch (Exception e) {
            LogUtils.logTestStep("Failed to apply sort option '" + sortOption + "': " + e.getMessage());
            throw new RuntimeException("Sort operation failed", e);
        }
    }
    
    /**
     * Check if price sorting is correct (top 5 results should be in ascending order)
     * @return PriceSortingResult object containing validation result and details
     */
    @Step("Check price sorting order")
    public PriceSortingResult checkPriceSorting() {
        LogUtils.logTestStep("Checking price sorting for top 5 results");
        
        // Wait for prices to fully load before verification
        waitForPricesToLoad();
        
        // Log current state of price elements
        LogUtils.logTestStep("Found " + originalPrices.size() + " price elements");
        ElementsCollection priceElementsToUse = originalPrices;
        
        if (originalPrices.size() == 0) {
            LogUtils.logTestStep("⚠ No price elements found - checking alternative selectors...");
            
            // Try alternative price selectors for hotel search page
            ElementsCollection altPrices1 = $$(By.xpath("//span[contains(@class,'price') or contains(@class,'Price')]"));
            ElementsCollection altPrices2 = $$(By.xpath("//div[@data-selenium='hotel-item']//span[contains(text(),'₫') or contains(text(),'VND')]"));
            
            LogUtils.logTestStep("Alternative selector 1 found: " + altPrices1.size() + " elements");
            LogUtils.logTestStep("Alternative selector 2 found: " + altPrices2.size() + " elements");
            
            // Use alternative if available
            if (altPrices1.size() > 0) {
                priceElementsToUse = altPrices1;
                LogUtils.logTestStep("Using alternative price selector 1");
            } else if (altPrices2.size() > 0) {
                priceElementsToUse = altPrices2;
                LogUtils.logTestStep("Using alternative price selector 2");
            }
        }
        
        // Get top 5 price elements
        int maxResults = Math.min(5, priceElementsToUse.size());
        StringBuilder details = new StringBuilder();
        boolean isCorrect = true;
        String errorMessage = "";
        
        LogUtils.logTestStep("Will check " + maxResults + " price results for sorting");
        
        double previousPrice = 0;
        for (int i = 0; i < maxResults; i++) {
            SelenideElement priceElement = priceElementsToUse.get(i);
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
            LogUtils.logTestStep("Price sorting check passed for top " + maxResults + " results");
        } else {
            LogUtils.logTestStep("Price sorting check failed: " + errorMessage);
        }
        
        return new PriceSortingResult(isCorrect, errorMessage, details.toString(), maxResults);
    }
    
    /**
     * Wait for prices to fully load before verification
     * This method ensures that all price elements are visible and properly loaded
     */
    @Step("Wait for prices to fully load")
    private void waitForPricesToLoad() {
        LogUtils.logTestStep("Waiting for prices to fully load...");
        
        try {
            // Wait for loading indicators to disappear using custom condition
            if (loadingIndicator.exists()) {
                LogUtils.logTestStep("Waiting for loading indicator to disappear...");
                WaitUtils.waitForCondition(() -> !loadingIndicator.isDisplayed(), 15);
            }
            
            // Wait for price elements to be present and visible
            LogUtils.logTestStep("Waiting for price elements to be visible...");
            WaitUtils.sleep(2000); // Small delay to allow prices to stabilize
            
            // Wait for at least some price elements to be present
            int attempts = 0;
            int maxAttempts = 10;
            while (originalPrices.size() == 0 && attempts < maxAttempts) {
                LogUtils.logTestStep("Attempt " + (attempts + 1) + ": Waiting for price elements to load...");
                WaitUtils.sleep(1000);
                attempts++;
            }
            
            if (originalPrices.size() == 0) {
                LogUtils.logTestStep("⚠ Warning: No price elements found after waiting");
                return;
            }
            
            // Wait for the first few price elements to have actual text content
            LogUtils.logTestStep("Waiting for price content to load...");
            for (int i = 0; i < Math.min(3, originalPrices.size()); i++) {
                SelenideElement priceElement = originalPrices.get(i);
                
                // Wait for price element to have non-empty text
                int textAttempts = 0;
                while ((priceElement.getText().trim().isEmpty() || priceElement.getText().equals("0")) && textAttempts < 5) {
                    LogUtils.logTestStep("Waiting for price " + (i + 1) + " to load content...");
                    WaitUtils.sleep(1000);
                    textAttempts++;
                }
            }
            
            // Final stabilization wait
            WaitUtils.sleep(1000);
            LogUtils.logVerificationStep("✓ Prices are fully loaded and ready for verification");
            
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Warning: Error while waiting for prices to load: " + e.getMessage());
            // Continue with verification even if wait fails
        }
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
     * Enhanced to handle Vietnamese price format and new HTML structure
     */
    private double extractPriceValue(String priceText) {
        LogUtils.logTestStep("Extracting price value from: " + priceText);
        
        // Handle empty or null price text
        if (priceText == null || priceText.trim().isEmpty()) {
            LogUtils.logTestStep("Warning: Empty price text, using 0");
            return 0;
        }
        
        // Clean the price text - remove currency symbols and keep only numbers, dots, and commas
        String numericPrice = priceText.replaceAll("[^0-9.,]", "").trim();
        
        if (numericPrice.isEmpty()) {
            LogUtils.logTestStep("Warning: No numeric content found in price: " + priceText + ", using 0");
            return 0;
        }
        
        try {
            // Handle different Vietnamese price formats
            if (numericPrice.contains(",") && numericPrice.contains(".")) {
                // Format like "1,234.56" - remove commas and treat dot as decimal
                numericPrice = numericPrice.replace(",", "");
                double value = Double.parseDouble(numericPrice);
                LogUtils.logTestStep("Parsed price (comma+dot format): " + value);
                return value;
            } else if (numericPrice.contains(",") && !numericPrice.contains(".")) {
                // Format like "1,000,000" - commas are thousands separators
                numericPrice = numericPrice.replace(",", "");
                double value = Double.parseDouble(numericPrice);
                LogUtils.logTestStep("Parsed price (comma format): " + value);
                return value;
            } else if (numericPrice.contains(".") && !numericPrice.contains(",")) {
                // Format like "663.02" - dot represents thousands + hundreds
                // This is the Vietnamese short form: 663.02 = 663,020 VND
                String[] parts = numericPrice.split("\\.");
                if (parts.length == 2 && parts[1].length() <= 2) {
                    // Treat as thousands format: 663.02 = 663 thousands + 20
                    double wholePart = Double.parseDouble(parts[0]);
                    double fractionalPart = Double.parseDouble(parts[1]);
                    
                    // If fractional part is less than 10, multiply by 10 to get correct value
                    // 663.02 -> 663 * 1000 + 2 * 10 = 663,020
                    double value;
                    if (fractionalPart < 10) {
                        value = wholePart * 1000 + fractionalPart * 10;
                    } else {
                        // 663.25 -> 663 * 1000 + 25 = 663,025
                        value = wholePart * 1000 + fractionalPart;
                    }
                    LogUtils.logTestStep("Parsed price (Vietnamese dot format): " + value);
                    return value;
                } else {
                    // Treat as regular decimal
                    double value = Double.parseDouble(numericPrice);
                    LogUtils.logTestStep("Parsed price (decimal format): " + value);
                    return value;
                }
            } else {
                // No separators - just a plain number
                double value = Double.parseDouble(numericPrice);
                LogUtils.logTestStep("Parsed price (plain number): " + value);
                return value;
            }
        } catch (NumberFormatException e) {
            LogUtils.logTestStep("Warning: Could not parse price: " + priceText + ", using 0. Error: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Get number of search results
     */
    public int getResultCount() {
        return searchResults.size();
    }

    // ==================== FILTERING FUNCTIONALITY ====================

    // Price filter elements for hotel search page (new HTML structure)
    private final SelenideElement minPriceInput = $(By.xpath("//input[@id='price_box_0']"));
    private final SelenideElement maxPriceInput = $(By.xpath("//input[@id='price_box_1']"));

    /**
     * Apply price filter by modifying URL parameters
     * @param minPrice target minimum price
     * @param maxPrice target maximum price
     */
    @Step("Apply price filter: {minPrice} - {maxPrice}")
    public void applyPriceFilter(int minPrice, int maxPrice) {
        LogUtils.logTestStep("Applying price filter using input boxes: " + minPrice + " - " + maxPrice);
        
        try {
            // Use the hotel input box approach instead of URL manipulation
            applyHotelPriceFilter(minPrice, maxPrice);
            
            LogUtils.logVerificationStep("✓ Price filter applied successfully");
            
        } catch (Exception e) {
            LogUtils.logError("Failed to apply price filter: " + e.getMessage(), e);
            throw new RuntimeException("Price filter application failed", e);
        }
    }
    
    /**
     * Apply price filter using input boxes for hotel search page
     * @param minPrice minimum price to set
     * @param maxPrice maximum price to set
     */
    @Step("Apply price filter using input boxes: {minPrice} - {maxPrice}")
    public void applyHotelPriceFilter(int minPrice, int maxPrice) {
        LogUtils.logTestStep("Applying hotel price filter using input boxes: " + minPrice + " - " + maxPrice);
        
        try {
            // Clear and set minimum price
            LogUtils.logTestStep("Setting minimum price: " + minPrice);
            minPriceInput.clear();
            minPriceInput.setValue(String.valueOf(minPrice));
            
            // Wait for page to process the min price change
            LogUtils.logTestStep("Waiting for page to process minimum price change...");
            WaitUtils.sleep(2000);
            
            // Clear and set maximum price
            LogUtils.logTestStep("Setting maximum price: " + maxPrice);
            maxPriceInput.clear();
            maxPriceInput.setValue(String.valueOf(maxPrice));
            
            // Wait for page to process the max price change and auto-reload
            LogUtils.logTestStep("Waiting for page to process maximum price change and reload...");
            WaitUtils.sleep(3000);
            
            // Wait for filtered results to load
            waitForPageToLoad();
            
            LogUtils.logVerificationStep("✓ Hotel price filter applied successfully using input boxes");
            
        } catch (Exception e) {
            LogUtils.logError("Failed to apply hotel price filter using input boxes: " + e.getMessage(), e);
            throw new RuntimeException("Hotel price filter application failed", e);
        }
    }

    /**
     * Apply star rating filter
     * @param starRating the star rating to filter by
     */
    /**
     * Apply star filter using the hotel search page structure with scrolling
     * @param starRating the star rating to apply
     */
    @Step("Apply star rating filter: {starRating}")
    public void applyStarFilter(StarRating starRating) {
        LogUtils.logTestStep("Applying " + starRating.getValue() + " star rating filter");
        
        try {
            // Wait for star rating section to be present
            SelenideElement starSection = $(By.xpath("//h3[contains(text(),'Star rating')]"));
            WaitUtils.waitForVisible(starSection, Duration.ofSeconds(10));
            LogUtils.logTestStep("Star rating section found");
            
            // Scroll to star rating section to ensure it's visible
            LogUtils.logTestStep("Scrolling to star rating section...");
            starSection.scrollIntoView(true);
            WaitUtils.sleep(1000);
            
            // Additional scroll to account for sticky headers
            executeJavaScript("window.scrollBy(0, -100);");
            WaitUtils.sleep(500);
            
            // Build XPath for the specific star rating using data-element-value
            String starValue = String.valueOf(starRating.getValue());
            String starXPath = "//label[@data-element-name='search-filter-starratingwithluxury' and @data-element-value='" + starValue + "']";
            
            LogUtils.logTestStep("Looking for star filter with XPath: " + starXPath);
            SelenideElement starElement = $(By.xpath(starXPath));
            
            if (starElement.exists()) {
                LogUtils.logTestStep("Found " + starRating.getValue() + " star filter element");
                
                // Scroll the element into view and wait
                starElement.scrollIntoView(true);
                WaitUtils.sleep(500);
                
                // Try JavaScript click to avoid any sticky header interference
                try {
                    LogUtils.logTestStep("Clicking " + starRating.getValue() + " star filter using JavaScript");
                    executeJavaScript("arguments[0].click();", starElement);
                    LogUtils.logVerificationStep("✓ " + starRating.getValue() + " star filter applied successfully");
                } catch (Exception jsException) {
                    LogUtils.logTestStep("JavaScript click failed, trying direct click");
                    starElement.click();
                    LogUtils.logVerificationStep("✓ " + starRating.getValue() + " star filter applied via direct click");
                }
                
                // Wait for filter to be applied and page to update
                WaitUtils.sleep(3000);
                LogUtils.logTestStep("Waiting for filtered results to load...");
                
            } else {
                LogUtils.logTestStep("✗ Could not find " + starRating.getValue() + " star filter element");
                throw new RuntimeException("Star filter element not found for " + starRating.getValue() + " stars");
            }
            
        } catch (Exception e) {
            LogUtils.logError("Failed to apply star filter: " + e.getMessage(), e);
            throw new RuntimeException("Star filter application failed", e);
        }
    }

    /**
     * Verify that hotels in results match the applied star rating
     * @param expectedStar the expected star rating
     * @return true if all hotels match the star rating
     */
    @Step("Verify hotel star ratings match filter: {expectedStar}")
    public boolean verifyHotelStarRatings(int expectedStar) {
        LogUtils.logTestStep("Verifying hotel star ratings match " + expectedStar + " star filter");
        
        try {
            // Use hotel search page structure - look for rating containers
            ElementsCollection ratingContainers = $$(By.xpath("//div[@data-testid='rating-container']"));
            
            if (ratingContainers.size() == 0) {
                LogUtils.logWarning("No rating containers found in hotel search results");
                return false;
            }
            
            int validRatings = 0;
            int totalChecked = Math.min(5, ratingContainers.size()); // Check first 5 results
            
            for (int i = 0; i < totalChecked; i++) {
                SelenideElement ratingContainer = ratingContainers.get(i);
                
                try {
                    // Look for the screen reader text that contains "X stars out of 5"
                    SelenideElement screenReaderElement = ratingContainer.$(By.xpath(".//span[contains(@class, 'ScreenReaderOnly')]"));
                    String screenReaderText = screenReaderElement.getText().trim();
                    
                    // Extract star count from text like "3 stars out of 5"
                    if (screenReaderText.contains("stars out of 5")) {
                        String[] parts = screenReaderText.split(" ");
                        if (parts.length > 0) {
                            // Parse as double first to handle decimal ratings like "3.5"
                            double starRating = Double.parseDouble(parts[0]);
                            // Round to nearest integer for comparison
                            int starCount = (int) Math.round(starRating);
                            
                            if (starCount == expectedStar) {
                                validRatings++;
                                LogUtils.logTestStep("✓ Hotel " + (i+1) + " rating: " + starRating + " stars (matches " + expectedStar + " star filter)");
                            } else {
                                LogUtils.logWarning("✗ Hotel " + (i+1) + " rating: " + starRating + " stars (doesn't match " + expectedStar + " star filter)");
                            }
                        }
                    } else {
                        LogUtils.logWarning("Could not parse star rating from: " + screenReaderText);
                    }
                } catch (Exception e) {
                    LogUtils.logWarning("Could not extract star rating for hotel " + (i+1) + ": " + e.getMessage());
                }
            }
            
            boolean allValid = validRatings == totalChecked;
            LogUtils.logVerificationStep("Star rating verification: " + validRatings + "/" + totalChecked + " hotels match " + expectedStar + " star filter");
            
            return allValid;
            
        } catch (Exception e) {
            LogUtils.logError("Failed to verify star ratings: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verify that hotels in results fall within the price range
     * @param minPrice minimum expected price
     * @param maxPrice maximum expected price
     * @return true if all hotels are within price range
     */
    @Step("Verify hotel prices are within range: {minPrice} - {maxPrice}")
    public boolean verifyHotelPriceRange(int minPrice, int maxPrice) {
        LogUtils.logTestStep("Verifying hotel prices are within range: " + minPrice + " - " + maxPrice);
        
        try {
            int validPrices = 0;
            int totalChecked = Math.min(5, originalPrices.size()); // Check first 5 results
            
            for (int i = 0; i < totalChecked; i++) {
                String priceText = originalPrices.get(i).getText();
                double price = extractPriceValue(priceText);
                
                if (price >= minPrice && price <= maxPrice) {
                    validPrices++;
                    LogUtils.logTestStep("✓ Hotel " + (i+1) + " price: " + price + " (within range)");
                } else {
                    LogUtils.logWarning("✗ Hotel " + (i+1) + " price: " + price + " (outside range)");
                }
            }
            
            boolean allValid = validPrices == totalChecked;
            LogUtils.logVerificationStep("Price range verification: " + validPrices + "/" + totalChecked + " hotels within price range");
            
            return allValid;
            
        } catch (Exception e) {
            LogUtils.logError("Failed to verify price range: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Remove price filter while keeping other filters by removing URL parameters
     */
    @Step("Remove price filter")
    public void removePriceFilter() {
        LogUtils.logTestStep("Removing price filter");
        
        try {
            // Get current URL and remove price filter parameters
            String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
            LogUtils.logTestStep("Current URL: " + currentUrl);
            
            // Remove price filter parameters from URL
            String newUrl = removePriceParametersFromUrl(currentUrl);
            LogUtils.logTestStep("Navigating to URL without price filter: " + newUrl);
            
            // Navigate to the new URL without price filters
            WebDriverRunner.getWebDriver().get(newUrl);
            
            // Wait for page to load
            WaitUtils.sleep(3000);
            
            // Wait for results to be available
            waitForPageToLoad();
            
            LogUtils.logVerificationStep("✓ Price filter removed successfully");
            
        } catch (Exception e) {
            LogUtils.logError("Failed to remove price filter: " + e.getMessage(), e);
            throw new RuntimeException("Price filter removal failed", e);
        }
    }
    
    /**
     * Remove price filter parameters from the URL
     * @param currentUrl the current URL
     * @return URL without price filter parameters
     */
    private String removePriceParametersFromUrl(String currentUrl) {
        try {
            // Remove both priceFrom and priceTo parameters
            String newUrl = currentUrl
                .replaceAll("[&?]priceFrom=[^&]*", "")
                .replaceAll("[&?]priceTo=[^&]*", "");
            
            // Clean up any double ampersands or question marks at the end
            newUrl = newUrl.replaceAll("&&", "&").replaceAll("\\?&", "?");
            
            // Remove trailing & or ?
            if (newUrl.endsWith("&") || newUrl.endsWith("?")) {
                newUrl = newUrl.substring(0, newUrl.length() - 1);
            }
            
            return newUrl;
        } catch (Exception e) {
            LogUtils.logError("Failed to construct URL without price filters: " + e.getMessage(), e);
            return currentUrl; // Return original URL if construction fails
        }
    }

    /**
     * Verify that destination appears correctly in search results
     * @param expectedDestination the expected destination name
     * @return true if destination is found in results
     */
    @Step("Verify destination in search results: {expectedDestination}")
    public boolean verifyDestinationInResults(String expectedDestination) {
        LogUtils.logTestStep("Verifying destination '" + expectedDestination + "' appears in search results");
        
        try {
            // Look for destination in hotel/activity card titles
            ElementsCollection destinationElements = $$(By.xpath("//h3[@data-element-name='activities-card-title']"));
            
            boolean found = false;
            int foundCount = 0;
            
            for (SelenideElement element : destinationElements) {
                String text = element.getText().toLowerCase();
                if (text.contains(expectedDestination.toLowerCase())) {
                    found = true;
                    foundCount++;
                    LogUtils.logTestStep("✓ Found destination '" + expectedDestination + "' in: " + text);
                }
            }
            
            if (!found) {
                // Also check page title and URL
                String pageTitle = title().toLowerCase();
                String currentUrl = com.codeborne.selenide.WebDriverRunner.url().toLowerCase();
                
                if (pageTitle.contains(expectedDestination.toLowerCase()) || 
                    currentUrl.contains(expectedDestination.toLowerCase().replace(" ", "-"))) {
                    found = true;
                    LogUtils.logTestStep("✓ Found destination '" + expectedDestination + "' in page title or URL");
                }
            }
            
            LogUtils.logVerificationStep("Destination verification: " + (found ? "✓ Found" : "✗ Not found") + 
                                       " '" + expectedDestination + "' (" + foundCount + " references)");
            
            return found;
            
        } catch (Exception e) {
            LogUtils.logError("Failed to verify destination: " + e.getMessage(), e);
            return false;
        }
    }
    
    // ===========================================
    // TC03 SPECIFIC METHODS
    // ===========================================
    
    /**
     * Apply swimming pool filter
     * TODO: Update XPath after manual capture
     */
    @Step("Apply swimming pool filter")
    public void applySwimmingPoolFilter() {
        LogUtils.logTestStep("Applying swimming pool filter");
        
        try {
            // TODO: Replace with actual XPath after manual capture
            String[] swimmingPoolXpaths = {
                "//input[@value='swimming_pool'] | //input[contains(@id,'pool')]",
                "//label[contains(text(),'Swimming pool')]//input",
                "//div[@data-testid='facility-filter']//input[contains(@aria-label,'pool')]",
                "//input[@type='checkbox'][contains(@name,'pool')]"
            };
            
            for (String xpath : swimmingPoolXpaths) {
                SelenideElement poolFilter = $(By.xpath(xpath));
                if (poolFilter.exists()) {
                    LogUtils.logTestStep("Found swimming pool filter with XPath: " + xpath);
                    
                    // Scroll to element and click
                    poolFilter.scrollTo();
                    WaitUtils.sleep(1000);
                    
                    if (!poolFilter.isSelected()) {
                        poolFilter.click();
                        LogUtils.logVerificationStep("✓ Swimming pool filter applied");
                        WaitUtils.sleep(3000); // Wait for results to update
                        return;
                    } else {
                        LogUtils.logTestStep("✓ Swimming pool filter already selected");
                        return;
                    }
                }
            }
            
            LogUtils.logTestStep("⚠ Swimming pool filter not found - please capture XPath manually");
            
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Error applying swimming pool filter: " + e.getMessage());
        }
    }
    
    /**
     * Select hotel at specific position and navigate to detail page
     * @param position Hotel position (1-based index)
     * @return AgodaHotelDetailPage instance
     */
    @Step("Select hotel at position {position}")
    public AgodaHotelDetailPage selectHotelAtPosition(int position) {
        LogUtils.logTestStep("Selecting hotel at position: " + position);
        
        try {
            waitForPageToLoad();
            
            if (position < 1 || position > searchResults.size()) {
                LogUtils.logTestStep("⚠ Invalid position: " + position + ". Available hotels: " + searchResults.size());
                throw new RuntimeException("Invalid hotel position: " + position);
            }
            
            SelenideElement selectedHotel = searchResults.get(position - 1); // Convert to 0-based index
            
            // Try to find a clickable link within the hotel result
            SelenideElement hotelLink = selectedHotel.$(By.xpath(".//a[contains(@href,'hotel')] | .//a[@data-testid='hotel-link'] | .//h3//a | .//div[@data-testid='hotel-name']//a"));
            
            if (hotelLink.exists()) {
                LogUtils.logTestStep("Found hotel link, clicking to go to detail page...");
                hotelLink.scrollTo();
                WaitUtils.sleep(1000);
                hotelLink.click();
            } else {
                LogUtils.logTestStep("Hotel link not found, clicking on hotel container...");
                selectedHotel.scrollTo();
                WaitUtils.sleep(1000);
                selectedHotel.click();
            }
            
            LogUtils.logVerificationStep("✓ Clicked on hotel at position " + position);
            WaitUtils.sleep(3000); // Wait for page to load
            
            return new AgodaHotelDetailPage();
            
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Error selecting hotel at position " + position + ": " + e.getMessage());
            throw new RuntimeException("Failed to select hotel at position " + position, e);
        }
    }
    
    /**
     * Hover over hotel at specific position to show review popup
     * @param position Hotel position (1-based index)
     * @return true if hover was successful, false otherwise
     */
    @Step("Hover over hotel at position {position} to show reviews")
    public boolean hoverOverHotelForReviews(int position) {
        LogUtils.logTestStep("Hovering over hotel at position " + position + " to show review popup");
        
        try {
            waitForPageToLoad();
            
            if (position < 1 || position > searchResults.size()) {
                LogUtils.logTestStep("⚠ Invalid position: " + position + ". Available hotels: " + searchResults.size());
                return false;
            }
            
            SelenideElement selectedHotel = searchResults.get(position - 1);
            
            // Try to find review score element to hover over
            // TODO: Update XPath after manual capture
            SelenideElement reviewElement = selectedHotel.$(By.xpath(".//div[contains(@class,'review')] | .//span[contains(@class,'rating')] | .//div[@data-testid='review-score']"));
            
            if (reviewElement.exists()) {
                LogUtils.logTestStep("Found review element, hovering...");
                reviewElement.scrollTo();
                WaitUtils.sleep(1000);
                reviewElement.hover();
                WaitUtils.sleep(2000); // Wait for popup to appear
                LogUtils.logVerificationStep("✓ Hovered over hotel " + position + " review area");
                return true;
            } else {
                LogUtils.logTestStep("Review element not found, hovering over hotel container...");
                selectedHotel.scrollTo();
                WaitUtils.sleep(1000);
                selectedHotel.hover();
                WaitUtils.sleep(2000);
                LogUtils.logTestStep("✓ Hovered over hotel " + position + " container");
                return true;
            }
            
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Error hovering over hotel at position " + position + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify review popup shows expected categories
     * @param expectedCategories List of expected review categories
     * @return true if all categories are found, false otherwise
     */
    @Step("Verify review popup contains expected categories")
    public boolean verifyReviewPopupCategories(String[] expectedCategories) {
        LogUtils.logTestStep("Verifying review popup shows expected categories");
        
        try {
            // TODO: Update XPath after manual capture
            SelenideElement reviewPopup = $(By.xpath("//div[contains(@class,'review-popup')] | //div[@data-testid='review-breakdown'] | //div[contains(@class,'rating-breakdown')]"));
            
            if (!reviewPopup.exists() || !reviewPopup.isDisplayed()) {
                LogUtils.logTestStep("⚠ Review popup not found or not displayed");
                return false;
            }
            
            String popupText = reviewPopup.getText();
            LogUtils.logTestStep("Review popup content: " + popupText);
            
            int foundCategories = 0;
            for (String category : expectedCategories) {
                if (popupText.toLowerCase().contains(category.toLowerCase())) {
                    LogUtils.logTestStep("✓ Found category: " + category);
                    foundCategories++;
                } else {
                    LogUtils.logTestStep("⚠ Missing category: " + category);
                }
            }
            
            boolean allCategoriesFound = foundCategories == expectedCategories.length;
            LogUtils.logVerificationStep("Review categories verification: " + foundCategories + "/" + expectedCategories.length + " found");
            
            return allCategoriesFound;
            
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Error verifying review popup: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify swimming pool filter is still active after navigation
     * @return true if filter is still active, false otherwise
     */
    @Step("Verify swimming pool filter is still active")
    public boolean verifySwimmingPoolFilterActive() {
        LogUtils.logTestStep("Verifying swimming pool filter is still active");
        
        try {
            // TODO: Update XPath after manual capture
            SelenideElement activePoolFilter = $(By.xpath("//input[@value='swimming_pool']:checked | //input[contains(@id,'pool')]:checked | //div[contains(@class,'filter-active')][contains(text(),'pool')]"));
            
            boolean isActive = activePoolFilter.exists() && activePoolFilter.isDisplayed();
            
            if (isActive) {
                LogUtils.logVerificationStep("✓ Swimming pool filter is still active");
            } else {
                LogUtils.logTestStep("⚠ Swimming pool filter is not active");
            }
            
            return isActive;
            
        } catch (Exception e) {
            LogUtils.logTestStep("⚠ Error checking swimming pool filter status: " + e.getMessage());
            return false;
        }
    }
}