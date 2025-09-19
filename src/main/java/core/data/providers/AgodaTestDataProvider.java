package core.data.providers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import core.data.models.AgodaTestData;
import core.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * DataProvider class for handling Agoda test data from JSON files
 */
public class AgodaTestDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(AgodaTestDataProvider.class);
    private static final String AGODA_TEST_DATA_PATH = "/test_data/agoda/agoda_test_data.json";
    private static JsonObject testDataRoot;
    private static final Gson gson = new Gson();

    /**
     * Load test data from JSON file
     */
    private static void loadTestData() {
        if (testDataRoot == null) {
            try (InputStream inputStream = AgodaTestDataProvider.class.getResourceAsStream(AGODA_TEST_DATA_PATH);
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                
                if (inputStream == null) {
                    throw new RuntimeException("Could not find test data file: " + AGODA_TEST_DATA_PATH);
                }
                
                testDataRoot = gson.fromJson(reader, JsonObject.class);
                logger.info("Successfully loaded Agoda test data from: {}", AGODA_TEST_DATA_PATH);
                
            } catch (IOException e) {
                logger.error("Failed to load test data from: {}", AGODA_TEST_DATA_PATH, e);
                throw new RuntimeException("Failed to load test data", e);
            }
        }
    }

    /**
     * Get test data for a specific test case
     * @param testCaseId The test case identifier (e.g., "TC01_SearchAndSortHotel")
     * @return AgodaTestData object containing the test data
     */
    public static AgodaTestData getTestData(String testCaseId) {
        loadTestData();
        
        JsonObject testCases = testDataRoot.getAsJsonObject("testCases");
        JsonObject testCaseData = testCases.getAsJsonObject(testCaseId);
        
        if (testCaseData == null) {
            throw new RuntimeException("Test case not found: " + testCaseId);
        }
        
        AgodaTestData data = gson.fromJson(testCaseData, AgodaTestData.class);
        logger.info("Retrieved test data for: {}", testCaseId);
        
        return data;
    }

    /**
     * Log test data information in a structured format
     * @param testData The test data to log
     */
    public static void logTestDataInfo(AgodaTestData testData) {
        LogUtils.logTestStep("Starting " + testData.getTestName());
        
        // Log basic test information
        if (testData.getDestination() != null) {
            LogUtils.logTestData("Destination", testData.getDestination());
        }
        
        if (testData.getCheckInDate() != null) {
            LogUtils.logTestData("Check-in Date", testData.getCheckInDate());
        }
        
        if (testData.getCheckOutDate() != null) {
            LogUtils.logTestData("Check-out Date", testData.getCheckOutDate());
        }
        
        // Log occupancy information
        if (testData.getOccupancy() != null) {
            LogUtils.logTestData("Rooms", String.valueOf(testData.getOccupancy().getRooms()));
            LogUtils.logTestData("Adults", String.valueOf(testData.getOccupancy().getAdults()));
            LogUtils.logTestData("Children", String.valueOf(testData.getOccupancy().getChildren()));
        }
        
        // Log sort option
        if (testData.getSortOption() != null) {
            LogUtils.logTestData("Sort Option", testData.getSortOption());
        }
        
        // Log filters if present
        if (testData.getFilters() != null && !testData.getFilters().isEmpty()) {
            testData.getFilters().forEach((key, value) -> 
                LogUtils.logTestData("Filter - " + key, String.valueOf(value))
            );
        }
        
        logger.info("Test data logged for: {}", testData.getTestName());
    }

    /**
     * Get all available test case IDs
     * @return Array of test case IDs
     */
    public static String[] getAvailableTestCases() {
        loadTestData();
        
        JsonObject testCases = testDataRoot.getAsJsonObject("testCases");
        return testCases.keySet().toArray(new String[0]);
    }

    /**
     * Validate that required test data fields are present
     * @param testData The test data to validate
     * @param requiredFields Array of required field names
     * @return true if all required fields are present and not null
     */
    public static boolean validateTestData(AgodaTestData testData, String... requiredFields) {
        for (String field : requiredFields) {
            switch (field.toLowerCase()) {
                case "destination":
                    if (testData.getDestination() == null || testData.getDestination().trim().isEmpty()) {
                        logger.error("Missing required field: destination");
                        return false;
                    }
                    break;
                case "checkindate":
                    if (testData.getCheckInDate() == null || testData.getCheckInDate().trim().isEmpty()) {
                        logger.error("Missing required field: checkInDate");
                        return false;
                    }
                    break;
                case "checkoutdate":
                    if (testData.getCheckOutDate() == null || testData.getCheckOutDate().trim().isEmpty()) {
                        logger.error("Missing required field: checkOutDate");
                        return false;
                    }
                    break;
                case "occupancy":
                    if (testData.getOccupancy() == null) {
                        logger.error("Missing required field: occupancy");
                        return false;
                    }
                    break;
                default:
                    logger.warn("Unknown validation field: {}", field);
            }
        }
        
        logger.info("Test data validation passed for: {}", testData.getTestName());
        return true;
    }
}