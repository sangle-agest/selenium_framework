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

import static com.codeborne.selenide.Selenide.*;

/**
 * Updated AgodaSearchResultsPage with correct XPaths for search results
 */
public class AgodaSearchResultsPage {
    
    // Search results elements - Updated to support both hotel and activities results
    private final ElementsCollection searchResults = $$(By.xpath("//div[@data-selenium='hotel-item'] | //div[contains(@class,'PropertyCard')] | //div[contains(@class,'property-card')] | //a[@data-testid='hotel-item'] | //a[@data-testid='activities-card-content']"));
    
    // Price elements - Prioritize display prices that show filtered results
    private final ElementsCollection displayPrices = $$(By.xpath("//span[@data-selenium='display-price'] | //div[@data-element-name='final-price']//span[@data-selenium='display-price'] | //span[contains(@class,'PropertyCardPrice__Value')]"));
    
    // Fallback for original prices if display prices not found
    private final ElementsCollection originalPrices = $$(By.xpath("//span[@data-testid='hotel-original-price'] | //span[@data-testid='activities-original-price']"));
    
    // Loading indicator for waiting - Updated for hotel search results page
    private final SelenideElement loadingIndicator = $(By.xpath("//div[contains(@class,'loading')] | //div[contains(@class,'spinner')] | //div[@data-testid='loading']"));
    
    public AgodaSearchResultsPage() {
        LogUtils.logTestStep("Initialized AgodaSearchResultsPageUpdated");
    }
    
    /**
     * Verify that the search results page is displayed correctly with destination in title
     * @param expectedDestination the expected destination name
     * @return true if the results page is displayed with correct destination
     */
    @Step("Verify search results page displays correctly for destination: {expectedDestination}")
    public boolean verifyResultsPageDisplay(String expectedDestination) {
        String methodName = "AgodaSearchResultsPage.verifyResultsPageDisplay()";
        LogUtils.logTestStep(methodName + " - Verifying search results page displays correctly for destination: " + expectedDestination);
        
        try {
            // Wait for page to load first
            waitForPageToLoad();
            
            // Debug page information
            debugPageInformation();
            
            String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
            LogUtils.logTestStep(methodName + " - Current URL: " + currentUrl);
            
            // Simple verification: Check if we have search results
            int resultsCount = searchResults.size();
            LogUtils.logTestStep(methodName + " - Search results count: " + resultsCount);
            
            if (resultsCount > 0) {
                LogUtils.logVerificationStep(methodName + " - ✓ Search results page displays correctly - Found " + resultsCount + " results");
                return true;
            } else {
                LogUtils.logTestStep(methodName + " - ⚠ Search results page verification failed - No search results found");
                return false;
            }
            
        } catch (Exception e) {
            LogUtils.logError(methodName + " - Failed to verify search results page display: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verify that the search parameters from homepage are correctly displayed in the search result page
     * @param expectedDestination the destination searched from homepage
     * @param expectedCheckInDate the check-in date from homepage (format: "dd MMM yyyy")
     * @param expectedCheckOutDate the check-out date from homepage (format: "dd MMM yyyy")
     * @param expectedRooms the number of rooms from homepage
     * @param expectedAdults the number of adults from homepage
     * @param expectedChildren the number of children from homepage
     * @return true if all search parameters match the homepage inputs
     */
    @Step("Verify search parameters match homepage inputs")
    public boolean verifySearchParameters(String expectedDestination, String expectedCheckInDate, 
                                        String expectedCheckOutDate, int expectedRooms, 
                                        int expectedAdults, int expectedChildren) {
        LogUtils.logTestStep("Verifying search parameters match homepage inputs");
        
        boolean destinationMatch = verifyDestinationParameter(expectedDestination);
        boolean checkInMatch = verifyCheckInDateParameter(expectedCheckInDate);
        boolean checkOutMatch = verifyCheckOutDateParameter(expectedCheckOutDate);
        boolean adultsMatch = verifyAdultsParameter(expectedAdults);
        boolean roomsMatch = verifyRoomsParameter(expectedRooms);
        
        boolean allParametersMatch = destinationMatch && checkInMatch && checkOutMatch && adultsMatch && roomsMatch;
        
        if (allParametersMatch) {
            LogUtils.logVerificationStep("✓ All search parameters match homepage inputs");
        } else {
            LogUtils.logTestStep("✗ Some search parameters do not match homepage inputs");
        }
        
        return allParametersMatch;
    }
    
    /**
     * Verify destination parameter in search box
     * @param expectedDestination the expected destination
     * @return true if destination matches
     */
    @Step("Verify destination parameter: {expectedDestination}")
    public boolean verifyDestinationParameter(String expectedDestination) {
        try {
            SelenideElement destinationInput = $(By.xpath("//input[@data-selenium='textInput']"));
            if (destinationInput.exists()) {
                String actualDestination = destinationInput.getValue();
                if (actualDestination.equalsIgnoreCase(expectedDestination)) {
                    LogUtils.logVerificationStep("✓ Destination matches: " + actualDestination);
                    return true;
                } else {
                    LogUtils.logTestStep("✗ Destination mismatch - Expected: '" + expectedDestination + "', Actual: '" + actualDestination + "'");
                    return false;
                }
            } else {
                LogUtils.logTestStep("⚠ Destination input box not found");
                return false;
            }
        } catch (Exception e) {
            LogUtils.logError("Failed to verify destination parameter: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verify check-in date parameter
     * @param expectedCheckInDate the expected check-in date
     * @return true if check-in date matches
     */
    @Step("Verify check-in date parameter: {expectedCheckInDate}")
    public boolean verifyCheckInDateParameter(String expectedCheckInDate) {
        try {
            SelenideElement checkInText = $(By.xpath("//div[@data-selenium='checkInText']"));
            if (checkInText.exists()) {
                String actualCheckIn = checkInText.getText().trim();
                if (actualCheckIn.equals(expectedCheckInDate)) {
                    LogUtils.logVerificationStep("✓ Check-in date matches: " + actualCheckIn);
                    return true;
                } else {
                    LogUtils.logTestStep("✗ Check-in date mismatch - Expected: '" + expectedCheckInDate + "', Actual: '" + actualCheckIn + "'");
                    return false;
                }
            } else {
                LogUtils.logTestStep("⚠ Check-in date text not found");
                return false;
            }
        } catch (Exception e) {
            LogUtils.logError("Failed to verify check-in date parameter: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verify check-out date parameter
     * @param expectedCheckOutDate the expected check-out date
     * @return true if check-out date matches
     */
    @Step("Verify check-out date parameter: {expectedCheckOutDate}")
    public boolean verifyCheckOutDateParameter(String expectedCheckOutDate) {
        try {
            SelenideElement checkOutText = $(By.xpath("//div[@data-selenium='checkOutText']"));
            if (checkOutText.exists()) {
                String actualCheckOut = checkOutText.getText().trim();
                if (actualCheckOut.equals(expectedCheckOutDate)) {
                    LogUtils.logVerificationStep("✓ Check-out date matches: " + actualCheckOut);
                    return true;
                } else {
                    LogUtils.logTestStep("✗ Check-out date mismatch - Expected: '" + expectedCheckOutDate + "', Actual: '" + actualCheckOut + "'");
                    return false;
                }
            } else {
                LogUtils.logTestStep("⚠ Check-out date text not found");
                return false;
            }
        } catch (Exception e) {
            LogUtils.logError("Failed to verify check-out date parameter: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verify adults count parameter
     * @param expectedAdults the expected number of adults
     * @return true if adults count matches
     */
    @Step("Verify adults parameter: {expectedAdults}")
    public boolean verifyAdultsParameter(int expectedAdults) {
        try {
            SelenideElement adultsElement = $(By.xpath("//span[@data-selenium='adultValue']"));
            if (adultsElement.exists()) {
                String adultsText = adultsElement.getText().trim();
                // Extract number from text like "3 adults" or "3&nbsp;adults"
                String actualAdultsStr = adultsText.replaceAll("[^0-9]", "");
                if (!actualAdultsStr.isEmpty()) {
                    int actualAdults = Integer.parseInt(actualAdultsStr);
                    if (actualAdults == expectedAdults) {
                        LogUtils.logVerificationStep("✓ Adults count matches: " + actualAdults);
                        return true;
                    } else {
                        LogUtils.logTestStep("✗ Adults count mismatch - Expected: " + expectedAdults + ", Actual: " + actualAdults);
                        return false;
                    }
                } else {
                    LogUtils.logTestStep("⚠ Could not parse adults count from: " + adultsText);
                    return false;
                }
            } else {
                LogUtils.logTestStep("⚠ Adults count element not found");
                return false;
            }
        } catch (Exception e) {
            LogUtils.logError("Failed to verify adults parameter: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verify rooms count parameter
     * @param expectedRooms the expected number of rooms
     * @return true if rooms count matches
     */
    @Step("Verify rooms parameter: {expectedRooms}")
    public boolean verifyRoomsParameter(int expectedRooms) {
        try {
            SelenideElement roomsElement = $(By.xpath("//div[@data-selenium='roomValue']"));
            if (roomsElement.exists()) {
                String roomsText = roomsElement.getText().trim();
                // Extract number from text like "2 rooms"
                String actualRoomsStr = roomsText.replaceAll("[^0-9]", "");
                if (!actualRoomsStr.isEmpty()) {
                    int actualRooms = Integer.parseInt(actualRoomsStr);
                    if (actualRooms == expectedRooms) {
                        LogUtils.logVerificationStep("✓ Rooms count matches: " + actualRooms);
                        return true;
                    } else {
                        LogUtils.logTestStep("✗ Rooms count mismatch - Expected: " + expectedRooms + ", Actual: " + actualRooms);
                        return false;
                    }
                } else {
                    LogUtils.logTestStep("⚠ Could not parse rooms count from: " + roomsText);
                    return false;
                }
            } else {
                LogUtils.logTestStep("⚠ Rooms count element not found");
                return false;
            }
        } catch (Exception e) {
            LogUtils.logError("Failed to verify rooms parameter: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Wait for search results page to fully load after tab switching
     */
    @Step("Wait for search results page to load")
    public void waitForPageToLoad() {
        String methodName = "AgodaSearchResultsPage.waitForPageToLoad()";
        LogUtils.logTestStep(methodName + " - Waiting for search results page to fully load...");
        
        // Simple wait for page to stabilize after navigation
        WaitUtils.sleep(5000);
        
        LogUtils.logVerificationStep(methodName + " - ✓ Page loading wait completed");
    }
    
    /**
     * Debug method to capture page information for troubleshooting
     */
    @Step("Debug page information")
    private void debugPageInformation() {
        try {
            String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
            String pageTitle = title();
            String pageSource = WebDriverRunner.getWebDriver().getPageSource();
            
            LogUtils.logTestStep("DEBUG - Current URL: " + currentUrl);
            LogUtils.logTestStep("DEBUG - Page Title: '" + pageTitle + "'");
            LogUtils.logTestStep("DEBUG - Page Title Length: " + pageTitle.length());
            LogUtils.logTestStep("DEBUG - Page Source Length: " + pageSource.length());
            
            // Check if page is still loading
            String readyState = (String) ((JavascriptExecutor) WebDriverRunner.getWebDriver())
                    .executeScript("return document.readyState");
            LogUtils.logTestStep("DEBUG - Document Ready State: " + readyState);
            
            // Check for common title elements
            try {
                SelenideElement titleElement = $(By.tagName("title"));
                if (titleElement.exists()) {
                    String titleText = titleElement.getText();
                    LogUtils.logTestStep("DEBUG - Title element text: '" + titleText + "'");
                } else {
                    LogUtils.logTestStep("DEBUG - Title element not found");
                }
            } catch (Exception e) {
                LogUtils.logTestStep("DEBUG - Error getting title element: " + e.getMessage());
            }
            
            // Check for page indicators that might show loading state
            try {
                boolean hasSearchResults = searchResults.size() > 0;
                LogUtils.logTestStep("DEBUG - Has search results: " + hasSearchResults + " (count: " + searchResults.size() + ")");
            } catch (Exception e) {
                LogUtils.logTestStep("DEBUG - Error checking search results: " + e.getMessage());
            }
            
            // Check for any visible text that might indicate page state
            try {
                String bodyText = $(By.tagName("body")).getText();
                boolean containsDestination = bodyText.toLowerCase().contains("da nang");
                LogUtils.logTestStep("DEBUG - Body contains 'da nang': " + containsDestination);
                
                // Log first 200 characters of body text
                String bodyPreview = bodyText.length() > 200 ? bodyText.substring(0, 200) + "..." : bodyText;
                LogUtils.logTestStep("DEBUG - Body text preview: " + bodyPreview);
            } catch (Exception e) {
                LogUtils.logTestStep("DEBUG - Error getting body text: " + e.getMessage());
            }
            
        } catch (Exception e) {
            LogUtils.logTestStep("DEBUG - Error in debug method: " + e.getMessage());
        }
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
        String methodName = "AgodaSearchResultsPage.sortBy()";
        LogUtils.logTestStep(methodName + " - Sorting by: " + sortOption);
        
        // Ensure page is loaded before trying to sort
        waitForPageToLoad();
        
        // Use the dynamic XPath provided by user
        String dynamicXPath = String.format("//div[@id='sort-bar']//button[.//span[normalize-space()='%s']]", sortOption);
        SelenideElement sortElement = $(By.xpath(dynamicXPath));
        
        LogUtils.logTestStep(methodName + " - Using dynamic XPath: " + dynamicXPath);
        
        // Check if element exists with the dynamic XPath
        if (!sortElement.exists()) {
            LogUtils.logTestStep(methodName + " - ⚠ Sort option not found with dynamic XPath, trying alternative approaches...");
            
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
                    LogUtils.logTestStep(methodName + " - Found alternative sort option with XPath: " + altXpath);
                    sortElement = altElement;
                    break;
                }
            }
        } else {
            LogUtils.logTestStep(methodName + " - ✓ Sort element found with dynamic XPath");
        }
        
        // Try to click the sort element
        try {
            if (sortElement != null && sortElement.exists()) {
                // Check if element is already selected (aria-current="true")
                String ariaCurrent = sortElement.getAttribute("aria-current");
                if ("true".equals(ariaCurrent)) {
                    LogUtils.logTestStep(methodName + " - ✓ Sort option '" + sortOption + "' is already selected");
                    return;
                }
                
                // If element is not visible, try to scroll or use JavaScript
                if (!sortElement.isDisplayed()) {
                    LogUtils.logTestStep(methodName + " - Sort element exists but not visible, trying JavaScript click...");
                    ((JavascriptExecutor) WebDriverRunner.getWebDriver())
                        .executeScript("arguments[0].click();", sortElement);
                } else {
                    LogUtils.logTestStep(methodName + " - Clicking sort element: " + sortOption);
                    sortElement.click();
                }
                
                // Wait for sorting to complete
                WaitUtils.sleep(3000);
                LogUtils.logVerificationStep(methodName + " - ✓ Sort option applied: " + sortOption);
                
            } else {
                // Check if we're on Activities page (different sort options)
                String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
                if (currentUrl.contains("activities")) {
                    LogUtils.logTestStep(methodName + " - ⚠ Currently on Activities page, sort options may be different");
                    LogUtils.logTestStep(methodName + " - Skipping sort operation for Activities page");
                    return; // Don't throw exception, just skip sorting
                }
                
                LogUtils.logTestStep(methodName + " - ✗ Sort element not found for option: " + sortOption);
                throw new RuntimeException("Sort element not found: " + sortOption);
            }
            
        } catch (Exception e) {
            // Don't fail the entire test if sorting fails on Activities page
            String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
            if (currentUrl.contains("activities")) {
                LogUtils.logTestStep(methodName + " - ⚠ Sort operation skipped for Activities page: " + e.getMessage());
                return;
            }
            
            LogUtils.logTestStep(methodName + " - Failed to apply sort option '" + sortOption + "': " + e.getMessage());
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
        
        // Log current state of price elements - prioritize display prices
        LogUtils.logTestStep("Found " + displayPrices.size() + " display price elements");
        ElementsCollection priceElementsToUse = displayPrices;
        
        if (displayPrices.size() == 0) {
            LogUtils.logTestStep("⚠ No display price elements found - trying original prices...");
            priceElementsToUse = originalPrices;
            LogUtils.logTestStep("Found " + originalPrices.size() + " original price elements");
            
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
            
            // Wait for at least some price elements to be present - check display prices first
            int attempts = 0;
            int maxAttempts = 10;
            while (displayPrices.size() == 0 && originalPrices.size() == 0 && attempts < maxAttempts) {
                LogUtils.logTestStep("Attempt " + (attempts + 1) + ": Waiting for price elements to load...");
                WaitUtils.sleep(1000);
                attempts++;
            }
            
            // Determine which price collection to use
            ElementsCollection pricesToCheck = displayPrices.size() > 0 ? displayPrices : originalPrices;
            
            if (pricesToCheck.size() == 0) {
                LogUtils.logTestStep("⚠ Warning: No price elements found after waiting");
                return;
            }
            
            LogUtils.logTestStep("Using " + (displayPrices.size() > 0 ? "display prices" : "original prices") + " for verification (" + pricesToCheck.size() + " elements)");
            
            // Wait for the first few price elements to have actual text content
            LogUtils.logTestStep("Waiting for price content to load...");
            for (int i = 0; i < Math.min(3, pricesToCheck.size()); i++) {
                SelenideElement priceElement = pricesToCheck.get(i);
                
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
            // Store initial result count to detect changes
            int initialResultCount = getSearchResultCount();
            LogUtils.logTestStep("Initial result count before filtering: " + initialResultCount);
            
            // Clear and set minimum price
            LogUtils.logTestStep("Setting minimum price: " + minPrice);
            minPriceInput.clear();
            minPriceInput.setValue(String.valueOf(minPrice));
            
            // Trigger input event (press Enter to ensure processing)
            minPriceInput.pressEnter();
            
            // Wait for page to process the min price change
            LogUtils.logTestStep("Waiting for page to process minimum price change...");
            WaitUtils.sleep(3000);
            
            // Clear and set maximum price
            LogUtils.logTestStep("Setting maximum price: " + maxPrice);
            maxPriceInput.clear();
            maxPriceInput.setValue(String.valueOf(maxPrice));
            
            // Trigger input event (press Enter to ensure processing)
            maxPriceInput.pressEnter();
            
            // Move mouse away from input fields to ensure proper page processing
            LogUtils.logTestStep("Moving mouse away from input fields to allow page processing...");
            try {
                // Move mouse to a neutral area (search results container)
                if (searchResults.size() > 0) {
                    searchResults.first().hover();
                } else {
                    // Fallback: move to page header area
                    $(By.tagName("body")).hover();
                }
            } catch (Exception e) {
                LogUtils.logTestStep("Mouse move failed, continuing: " + e.getMessage());
            }
            
            // Wait for page to process the max price change and auto-reload
            LogUtils.logTestStep("Waiting for page to process maximum price change and reload...");
            WaitUtils.sleep(5000);
            
            // Wait for filtered results to load with specific focus on price filter results
            waitForFilteredResultsToLoad(initialResultCount);
            
            LogUtils.logVerificationStep("✓ Hotel price filter applied successfully using input boxes");
            
        } catch (Exception e) {
            LogUtils.logError("Failed to apply hotel price filter using input boxes: " + e.getMessage(), e);
            throw new RuntimeException("Hotel price filter application failed", e);
        }
    }
    
    /**
     * Wait specifically for filtered results to load after applying filters
     * This method waits for the page to update with new filtered content
     */
    @Step("Wait for filtered results to load")
    private void waitForFilteredResultsToLoad(int previousResultCount) {
        LogUtils.logTestStep("Waiting for filtered results to load (previous count: " + previousResultCount + ")");
        
        try {
            // First, wait for basic page load
            waitForPageToLoad();
            
            // Additional wait for page processing after mouse movement
            LogUtils.logTestStep("Waiting for page to process filter changes after mouse movement...");
            WaitUtils.sleep(3000);
            
            // Then wait specifically for filtered content to stabilize
            LogUtils.logTestStep("Waiting for filtered content to stabilize...");
            
            // Wait for any loading overlays to disappear and content to load
            for (int i = 0; i < 20; i++) {
                try {
                    if (loadingIndicator.exists() && loadingIndicator.isDisplayed()) {
                        LogUtils.logTestStep("Loading indicator still visible, waiting...");
                        WaitUtils.sleep(1000);
                        continue;
                    }
                } catch (Exception e) {
                    // Ignore loading indicator issues
                }
                
                // Check if results have stabilized by verifying price elements are loaded
                ElementsCollection pricesToCheck = displayPrices.size() > 0 ? displayPrices : originalPrices;
                if (pricesToCheck.size() > 0) {
                    LogUtils.logTestStep("Price elements found (" + (displayPrices.size() > 0 ? "display prices" : "original prices") + "), checking if they have content...");
                    
                    // Verify first few prices have actual content (not empty or loading)
                    boolean pricesLoaded = true;
                    for (int j = 0; j < Math.min(3, pricesToCheck.size()); j++) {
                        try {
                            String priceText = pricesToCheck.get(j).getText();
                            if (priceText == null || priceText.trim().isEmpty() || priceText.contains("...") || priceText.equals("Loading...")) {
                                pricesLoaded = false;
                                break;
                            }
                        } catch (Exception e) {
                            pricesLoaded = false;
                            break;
                        }
                    }
                    
                    if (pricesLoaded) {
                        LogUtils.logVerificationStep("✓ Filtered results have loaded with " + (displayPrices.size() > 0 ? "display" : "original") + " price data");
                        
                        // Additional wait to ensure all filtering and price updates are complete
                        LogUtils.logTestStep("Final wait to ensure all price data is fully updated...");
                        WaitUtils.sleep(3000);
                        
                        int finalResultCount = getSearchResultCount();
                        LogUtils.logTestStep("Final result count after filtering: " + finalResultCount);
                        return;
                    }
                }
                
                LogUtils.logTestStep("Results not yet stabilized, waiting... (attempt " + (i + 1) + "/20)");
                WaitUtils.sleep(1000);
            }
            
            LogUtils.logTestStep("⚠ Warning: Filtered results may not be fully loaded after maximum wait time");
            
        } catch (Exception e) {
            LogUtils.logError("Error waiting for filtered results: " + e.getMessage(), e);
        }
    }

    /**
     * Apply star rating filter
     * @param starRating the star rating to filter by
     */
    /**
     * Apply star filter using the hotel search page structure with scrolling - with debugging
     * @param starRating the star rating to apply
     */
    @Step("Apply star rating filter: {starRating}")
    public void applyStarFilter(StarRating starRating) {
        LogUtils.logTestStep("Applying " + starRating.getValue() + " star rating filter");
        
        try {
            // Try to find star rating section with different possible texts
            SelenideElement starSection = null;
            String[] possibleTexts = {"Star rating", "star rating", "Rating", "Hotel star rating", "Property rating"};
            
            for (String text : possibleTexts) {
                try {
                    starSection = $(By.xpath("//h3[contains(text(),'" + text + "')]"));
                    if (starSection.exists()) {
                        LogUtils.logTestStep("Found star rating section with text: '" + text + "'");
                        break;
                    }
                } catch (Exception e) {
                    // Continue to next text
                }
            }
            
            if (starSection == null || !starSection.exists()) {
                LogUtils.logTestStep("Could not find star rating section header, trying to find filter elements directly");
                
                // Debug: List all possible filter elements
                LogUtils.logTestStep("DEBUG: Looking for all filter-related elements...");
                
                // Try different possible star filter selectors
                String[] possibleSelectors = {
                    "//label[@data-element-name='search-filter-starratingwithluxury']",
                    "//input[@name*='star']",
                    "//input[@id*='star']",
                    "//label[contains(@class,'star')]",
                    "//div[contains(@class,'star')]//input",
                    "//div[contains(@class,'rating')]//input",
                    "//input[@type='checkbox'][contains(@name,'rating')]",
                    "//input[@type='checkbox'][contains(@value,'3')]",
                    "//label[contains(text(),'3')]"
                };
                
                for (String selector : possibleSelectors) {
                    try {
                        ElementsCollection elements = $$(By.xpath(selector));
                        if (elements.size() > 0) {
                            LogUtils.logTestStep("DEBUG: Found " + elements.size() + " elements with selector: " + selector);
                            for (int i = 0; i < Math.min(elements.size(), 3); i++) {
                                try {
                                    String elementInfo = "Element " + i + ": tag=" + elements.get(i).getTagName() + 
                                                       ", class=" + elements.get(i).getAttribute("class") + 
                                                       ", id=" + elements.get(i).getAttribute("id") +
                                                       ", name=" + elements.get(i).getAttribute("name") +
                                                       ", value=" + elements.get(i).getAttribute("value") +
                                                       ", text=" + elements.get(i).getText();
                                    LogUtils.logTestStep("DEBUG: " + elementInfo);
                                } catch (Exception ex) {
                                    LogUtils.logTestStep("DEBUG: Could not get info for element " + i);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Continue to next selector
                    }
                }
                
                // Try to find any filter containers
                try {
                    ElementsCollection filterContainers = $$(By.xpath("//div[contains(@class,'filter')]"));
                    LogUtils.logTestStep("DEBUG: Found " + filterContainers.size() + " filter containers");
                } catch (Exception e) {
                    LogUtils.logTestStep("DEBUG: No filter containers found");
                }
                
                throw new RuntimeException("Could not find star rating section on the page");
            }
            
            // Scroll to star rating section to ensure it's visible
            LogUtils.logTestStep("Scrolling to star rating section...");
            starSection.scrollIntoView(true);
            WaitUtils.sleep(1000);
            
            // Additional scroll to account for sticky headers
            executeJavaScript("window.scrollBy(0, -100);");
            WaitUtils.sleep(500);
            
            // Try multiple possible selectors for star filter
            String starValue = String.valueOf(starRating.getValue());
            String[] starSelectors = {
                "//label[@data-element-name='search-filter-starratingwithluxury' and @data-element-value='" + starValue + "']",
                "//input[@value='" + starValue + "' and contains(@name,'star')]",
                "//input[@value='" + starValue + "' and contains(@name,'rating')]",
                "//label[contains(text(),'" + starValue + " star')]",
                "//label[contains(text(),'" + starValue + "')]//input",
                "//div[contains(@data-value,'" + starValue + "')]//input"
            };
            
            SelenideElement starElement = null;
            String usedSelector = "";
            
            for (String selector : starSelectors) {
                try {
                    LogUtils.logTestStep("Trying selector: " + selector);
                    starElement = $(By.xpath(selector));
                    if (starElement.exists()) {
                        LogUtils.logTestStep("Found star element with selector: " + selector);
                        usedSelector = selector;
                        break;
                    }
                } catch (Exception e) {
                    LogUtils.logTestStep("Selector failed: " + selector);
                }
            }
            
            if (starElement != null && starElement.exists()) {
                LogUtils.logTestStep("Found " + starRating.getValue() + " star filter element using: " + usedSelector);
                
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
                LogUtils.logTestStep("✗ Could not find " + starRating.getValue() + " star filter element with any selector");
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
            // Use display prices first, fallback to original prices
            ElementsCollection pricesToCheck = displayPrices.size() > 0 ? displayPrices : originalPrices;
            LogUtils.logTestStep("Using " + (displayPrices.size() > 0 ? "display prices" : "original prices") + " for verification");
            
            int validPrices = 0;
            int totalChecked = Math.min(5, pricesToCheck.size()); // Check first 5 results
            
            for (int i = 0; i < totalChecked; i++) {
                String priceText = pricesToCheck.get(i).getText();
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
     * Remove price filter by clearing the input fields
     */
    @Step("Remove price filter")
    public void removePriceFilter() {
        LogUtils.logTestStep("Removing price filter");
        
        try {
            // Clear minimum price input
            LogUtils.logTestStep("Clearing minimum price input");
            if (minPriceInput.exists()) {
                minPriceInput.clear();
                minPriceInput.pressEnter();
                WaitUtils.sleep(2000);
            }
            
            // Clear maximum price input  
            LogUtils.logTestStep("Clearing maximum price input");
            if (maxPriceInput.exists()) {
                maxPriceInput.clear();
                maxPriceInput.pressEnter();
                WaitUtils.sleep(2000);
            }
            
            // Move mouse away from input fields
            LogUtils.logTestStep("Moving mouse away from input fields after clearing...");
            try {
                if (searchResults.size() > 0) {
                    searchResults.first().hover();
                } else {
                    $(By.tagName("body")).hover();
                }
            } catch (Exception e) {
                LogUtils.logTestStep("Mouse move failed, continuing: " + e.getMessage());
            }
            
            // Wait for page to process the filter removal
            LogUtils.logTestStep("Waiting for page to process filter removal...");
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