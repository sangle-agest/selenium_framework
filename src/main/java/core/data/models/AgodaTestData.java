package core.data.models;

import java.util.List;
import java.util.Map;

/**
 * Data model for Agoda test case data
 */
public class AgodaTestData {
    private String testName;
    private String description;
    private String destination;
    private String checkInDate;
    private String checkOutDate;
    private Occupancy occupancy;
    private String sortOption;
    private ExpectedResults expectedResults;
    private Map<String, Object> filters;
    private List<Map<String, String>> destinations; // For multi-destination tests

    // Default constructor for Gson
    public AgodaTestData() {}

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Occupancy getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Occupancy occupancy) {
        this.occupancy = occupancy;
    }

    public String getSortOption() {
        return sortOption;
    }

    public void setSortOption(String sortOption) {
        this.sortOption = sortOption;
    }

    public ExpectedResults getExpectedResults() {
        return expectedResults;
    }

    public void setExpectedResults(ExpectedResults expectedResults) {
        this.expectedResults = expectedResults;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public List<Map<String, String>> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Map<String, String>> destinations) {
        this.destinations = destinations;
    }

    @Override
    public String toString() {
        return String.format("AgodaTestData{testName='%s', destination='%s', checkInDate='%s', checkOutDate='%s', occupancy=%s}", 
                           testName, destination, checkInDate, checkOutDate, occupancy);
    }
}