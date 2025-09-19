package core.data.models;

import java.util.List;
import java.util.Map;

/**
 * Data model for expected test results
 */
public class ExpectedResults {
    private Integer minSearchResults;
    private Integer maxPrice;
    private String sortOrder;

    // Default constructor for Gson
    public ExpectedResults() {}

    public Integer getMinSearchResults() {
        return minSearchResults;
    }

    public void setMinSearchResults(Integer minSearchResults) {
        this.minSearchResults = minSearchResults;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return String.format("ExpectedResults{minSearchResults=%d, maxPrice=%d, sortOrder='%s'}", 
                           minSearchResults, maxPrice, sortOrder);
    }
}