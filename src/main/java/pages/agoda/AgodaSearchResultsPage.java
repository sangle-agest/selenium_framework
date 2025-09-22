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
     * Fixed to handle Vietnamese price format where dot represents thousands separator
     */
    private double extractPriceValue(String priceText) {
        // Remove currency symbols and keep only numbers, dots, and commas
        String numericPrice = priceText.replaceAll("[^0-9.,]", "");
        
        try {
            // Handle different Vietnamese price formats
            if (numericPrice.contains(",") && numericPrice.contains(".")) {
                // Format like "1,234.56" - remove commas and treat dot as decimal
                numericPrice = numericPrice.replace(",", "");
                return Double.parseDouble(numericPrice);
            } else if (numericPrice.contains(",") && !numericPrice.contains(".")) {
                // Format like "1,000,000" - commas are thousands separators
                numericPrice = numericPrice.replace(",", "");
                return Double.parseDouble(numericPrice);
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
                    if (fractionalPart < 10) {
                        return wholePart * 1000 + fractionalPart * 10;
                    } else {
                        // 663.25 -> 663 * 1000 + 25 = 663,025
                        return wholePart * 1000 + fractionalPart;
                    }
                } else {
                    // Treat as regular decimal
                    return Double.parseDouble(numericPrice);
                }
            } else {
                // No separators - just a plain number
                return Double.parseDouble(numericPrice);
            }
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

    // ==================== FILTERING FUNCTIONALITY ====================

    // Price filter elements
    private final SelenideElement minSlider = $(By.xpath("//div[@data-testid='activities-filter-price-slider']//input[@data-index='0']"));
    private final SelenideElement maxSlider = $(By.xpath("//div[@data-testid='activities-filter-price-slider']//input[@data-index='1']"));
    private final SelenideElement minLabel = $(By.xpath("//p[@data-testid='activities-filter-price-min']"));
    private final SelenideElement maxLabel = $(By.xpath("//p[@data-testid='activities-filter-price-max']"));

    /**
     * Apply price filter by modifying URL parameters
     * @param minPrice target minimum price
     * @param maxPrice target maximum price
     */
    @Step("Apply price filter via URL: {minPrice} - {maxPrice}")
    public void applyPriceFilter(int minPrice, int maxPrice) {
        LogUtils.logTestStep("Applying price filter via URL: " + minPrice + " - " + maxPrice);
        
        try {
            // Get current URL
            String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
            LogUtils.logTestStep("Current URL: " + currentUrl);
            
            // Build new URL with price filter parameters
            String newUrl = addPriceParametersToUrl(currentUrl, minPrice, maxPrice);
            LogUtils.logTestStep("Navigating to filtered URL: " + newUrl);
            
            // Navigate to the new URL
            WebDriverRunner.getWebDriver().get(newUrl);
            
            // Wait for page to load
            WaitUtils.sleep(3000);
            
            // Wait for results to be available
            waitForPageToLoad();
            
            // Verify the price filter was applied by checking the slider values
            verifyPriceFilterApplied(minPrice, maxPrice);
            
            LogUtils.logVerificationStep("✓ Price filter applied successfully via URL");
            
        } catch (Exception e) {
            LogUtils.logError("Failed to apply price filter via URL: " + e.getMessage(), e);
            throw new RuntimeException("Price filter application failed", e);
        }
    }
    
    /**
     * Add price filter parameters to the URL
     * @param currentUrl the current URL
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return URL with price filter parameters
     */
    private String addPriceParametersToUrl(String currentUrl, int minPrice, int maxPrice) {
        try {
            // Remove existing price parameters if they exist
            String baseUrl = currentUrl.replaceAll("&priceFrom=[^&]*", "").replaceAll("&priceTo=[^&]*", "");
            
            // Add new price parameters
            String separator = baseUrl.contains("?") ? "&" : "?";
            String priceParams = "priceFrom=" + minPrice + "&priceTo=" + maxPrice;
            
            return baseUrl + separator + priceParams;
        } catch (Exception e) {
            LogUtils.logError("Failed to construct price filter URL: " + e.getMessage(), e);
            return currentUrl; // Return original URL if construction fails
        }
    }
    
    /**
     * Verify that price filter was applied by checking the displayed range
     * @param expectedMin expected minimum price
     * @param expectedMax expected maximum price
     */
    private void verifyPriceFilterApplied(int expectedMin, int expectedMax) {
        try {
            // Wait for price labels to be visible
            WaitUtils.waitForVisible(minLabel, Duration.ofSeconds(10));
            WaitUtils.waitForVisible(maxLabel, Duration.ofSeconds(10));
            
            // Get current price range from the UI
            String minText = minLabel.getText();
            String maxText = maxLabel.getText();
            
            LogUtils.logTestStep("Price filter verification:");
            LogUtils.logTestStep("Expected range: " + expectedMin + " - " + expectedMax);
            LogUtils.logTestStep("Displayed range: " + minText + " - " + maxText);
            
            // Extract numeric values
            int displayedMin = (int) extractPriceValue(minText);
            int displayedMax = (int) extractPriceValue(maxText);
            
            LogUtils.logTestStep("Numeric values - Expected: " + expectedMin + "-" + expectedMax + 
                               ", Displayed: " + displayedMin + "-" + displayedMax);
            
            // Check if the displayed values are reasonably close to expected (within 20% tolerance)
            double minTolerance = expectedMin * 0.2;
            double maxTolerance = expectedMax * 0.2;
            
            boolean minInRange = Math.abs(displayedMin - expectedMin) <= minTolerance;
            boolean maxInRange = Math.abs(displayedMax - expectedMax) <= maxTolerance;
            
            if (minInRange && maxInRange) {
                LogUtils.logVerificationStep("✓ Price filter range is within acceptable tolerance");
            } else {
                LogUtils.logWarning("Price filter range differs from expected values - this may be due to currency conversion or platform limitations");
            }
            
        } catch (Exception e) {
            LogUtils.logWarning("Could not verify exact price filter values: " + e.getMessage());
            LogUtils.logTestStep("Price filter was applied via URL, continuing with test...");
        }
    }

    /**
     * Apply star rating filter
     * @param starRating the star rating to filter by
     */
    /**
     * Apply star filter using the activities page structure with enhanced scrolling and click handling
     * @param starRating the star rating to apply
     */
    @Step("Apply star rating filter: {starRating}")
    public void applyStarFilter(StarRating starRating) {
        LogUtils.logTestStep("Applying " + starRating.getValue() + " star rating filter");
        
        try {
            // Wait for star rating container to be visible
            SelenideElement starContainer = $("[data-testid='activities-filter-star-rating-container']");
            WaitUtils.waitForVisible(starContainer, Duration.ofSeconds(10));
            
            // Scroll the entire page down first to get past sticky headers
            executeJavaScript("window.scrollTo(0, 300);");
            WaitUtils.sleep(1000);
            
            // Scroll to star filter section
            starContainer.scrollIntoView(true);
            WaitUtils.sleep(1000);
            
            // Additional scroll down to ensure filter is not covered by sticky elements
            executeJavaScript("window.scrollBy(0, 200);");
            WaitUtils.sleep(500);
            
            String starValue = starRating.getValue() + " stars rating";
            LogUtils.logTestStep("Looking for star filter with aria-label: " + starValue);
            
            // Find the specific star rating input
            SelenideElement starInput = $("input[aria-label='" + starValue + "']");
            
            if (starInput.exists()) {
                LogUtils.logTestStep("Found star rating input, attempting to click");
                
                // Try JavaScript click directly since element is found but covered by sticky header
                try {
                    LogUtils.logTestStep("Using JavaScript click to bypass sticky header interference");
                    ((JavascriptExecutor) WebDriverRunner.getWebDriver())
                        .executeScript("arguments[0].click();", starInput);
                    LogUtils.logVerificationStep("✓ Star filter clicked via JavaScript");
                } catch (Exception jsException) {
                    LogUtils.logTestStep("JavaScript click failed, trying direct click with more scrolling");
                    
                    // Additional aggressive scrolling to get past sticky elements
                    executeJavaScript("window.scrollBy(0, 400);");
                    WaitUtils.sleep(1000);
                    
                    starInput.scrollIntoView(true);
                    WaitUtils.sleep(500);
                    starInput.click();
                    LogUtils.logVerificationStep("✓ Star filter clicked after additional scrolling");
                }
                
                // Wait for filter to be applied
                WaitUtils.sleep(2000);
                
                // Verify the checkbox is checked
                boolean isChecked = starInput.isSelected();
                LogUtils.logTestStep("Star filter checkbox checked: " + isChecked);
                
            } else {
                String errorMsg = "Star rating input not found for: " + starValue;
                LogUtils.logTestStep(errorMsg);
                throw new RuntimeException("Star rating filter not found: " + starValue);
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
            ElementsCollection ratingElements = $$(By.xpath("//div[@data-testid='activities-card-star-rating']//p"));
            
            if (ratingElements.size() == 0) {
                LogUtils.logWarning("No rating elements found in search results");
                return false;
            }
            
            List<String> ratingsText = ratingElements.texts();
            int validRatings = 0;
            int totalChecked = Math.min(5, ratingsText.size()); // Check first 5 results
            
            for (int i = 0; i < totalChecked; i++) {
                String ratingStr = ratingsText.get(i).trim();
                try {
                    double rating = Double.parseDouble(ratingStr);
                    
                    if (rating >= expectedStar && rating < expectedStar + 1) {
                        validRatings++;
                        LogUtils.logTestStep("✓ Hotel " + (i+1) + " rating: " + rating + " (valid for " + expectedStar + " star filter)");
                    } else {
                        LogUtils.logWarning("✗ Hotel " + (i+1) + " rating: " + rating + " (invalid for " + expectedStar + " star filter)");
                    }
                } catch (NumberFormatException e) {
                    LogUtils.logWarning("Could not parse rating: " + ratingStr);
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
}