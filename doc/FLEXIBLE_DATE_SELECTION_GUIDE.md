# Flexible Date Selection Implementation

## Overview

The `selectDates` method in `AgodaHomePage` has been refactored to support flexible date selection while maintaining backward compatibility. This enhancement allows test automation to easily adapt to different test scenarios without hardcoding specific dates.

## Key Improvements

### 1. **DayOfWeekEnum Creation**
- **File**: `src/main/java/core/enums/DayOfWeekEnum.java`
- **Purpose**: Type-safe day selection with utility methods
- **Features**:
  - Wraps Java's `DayOfWeek` enum
  - Provides display names and conversion methods
  - Supports string-based day parsing

### 2. **Enhanced DateTimeUtils**
- **File**: `src/main/java/core/utils/DateTimeUtils.java`
- **New Methods Added**:
  - `getNextDayOfWeek(DayOfWeek targetDay)` - Get next occurrence of any day
  - `getNextDayOfWeekPlusDays(DayOfWeek targetDay, int plusDays)` - Add duration
  - `getFlexibleDateRange(DayOfWeek targetDay, int duration)` - Complete date range
  - `getCurrentDayOfWeek()` - Get current day
  - `formatDateRangeForLogging()` - Enhanced logging support

### 3. **Refactored AgodaHomePage Methods**

#### **selectDates() - Backward Compatibility**
```java
// Original method signature - unchanged behavior
public AgodaHomePage selectDates()
```
- Maintains existing functionality (Friday + 3 days)
- No impact on existing tests

#### **selectDates(DayOfWeek, int) - New Flexible Method**
```java
// New flexible method with DayOfWeek enum
public AgodaHomePage selectDates(DayOfWeek startDay, int duration)
```
- **Parameters**:
  - `startDay`: Any day of week (e.g., `DayOfWeek.SUNDAY`)
  - `duration`: Number of days for stay (e.g., `5`)

#### **selectDates(DayOfWeekEnum, int) - Type-Safe Method**
```java
// Type-safe method with custom enum
public AgodaHomePage selectDates(DayOfWeekEnum startDay, int duration)
```
- **Parameters**:
  - `startDay`: Custom enum (e.g., `DayOfWeekEnum.SUNDAY`)
  - `duration`: Number of days for stay

### 4. **Enhanced searchHotels Methods**

#### **Original Method - Backward Compatible**
```java
public AgodaSearchResultsPage searchHotels(String destination)
```

#### **New Flexible Methods**
```java
// With DayOfWeek
public AgodaSearchResultsPage searchHotels(String destination, DayOfWeek startDay, int duration)

// With DayOfWeekEnum
public AgodaSearchResultsPage searchHotels(String destination, DayOfWeekEnum startDay, int duration)
```

## Usage Examples

### **Example 1: Sunday + 5 Days**
```java
// Test case: Search for hotels starting Sunday for 5 days
AgodaHomePage homePage = new AgodaHomePage();
homePage.navigateToHomepage();

// Method 1: Using DayOfWeek enum
AgodaSearchResultsPage results = homePage.searchHotels("Phuket", DayOfWeek.SUNDAY, 5);

// Method 2: Using DayOfWeekEnum
AgodaSearchResultsPage results = homePage.searchHotels("Phuket", DayOfWeekEnum.SUNDAY, 5);

// Method 3: Step-by-step
homePage.enterDestination("Phuket");
homePage.selectDates(DayOfWeek.SUNDAY, 5);
homePage.configureTravelers();
AgodaSearchResultsPage results = homePage.clickSearch();
```

### **Example 2: Wednesday + 7 Days**
```java
// Flexible date selection for different scenarios
homePage.selectDates(DayOfWeek.WEDNESDAY, 7);
// or
homePage.selectDates(DayOfWeekEnum.WEDNESDAY, 7);
```

### **Example 3: Backward Compatibility**
```java
// Existing code continues to work unchanged
homePage.selectDates(); // Still uses Friday + 3 days
homePage.searchHotels("Bangkok"); // Still uses Friday + 3 days
```

## Test Implementation Examples

### **TC03_FlexibleDateSearchTest.java**
```java
@Test
public void testSearchHotelsSundayFiveDays() {
    String destination = "Phuket";
    DayOfWeek startDay = DayOfWeek.SUNDAY;
    int duration = 5;
    
    AgodaHomePage homePage = new AgodaHomePage();
    homePage.navigateToHomepage();
    
    // Use flexible date selection
    AgodaSearchResultsPage results = homePage.searchHotels(destination, startDay, duration);
    
    Assert.assertTrue(results.areResultsDisplayed());
}
```

### **Enhanced TC02 with Flexible Dates**
```java
@Test
public void testSearchAndFilterHotelFlexibleDates() {
    // Sunday + 5 days instead of Friday + 3 days
    AgodaSearchResultsPage results = homePage.searchHotels("Phuket", DayOfWeek.SUNDAY, 5);
    
    results.applyResortFilter();
    results.applyFiveStarFilter();
}
```

## DateTimeUtils Enhanced Methods

### **Core Flexible Methods**
```java
// Get next Sunday
String date = DateTimeUtils.getNextDayOfWeek(DayOfWeek.SUNDAY);
// Returns: "2025-09-21" (example)

// Get Sunday + 5 days
String endDate = DateTimeUtils.getNextDayOfWeekPlusDays(DayOfWeek.SUNDAY, 5);
// Returns: "2025-09-26" (example)

// Get complete date range
String[] dateRange = DateTimeUtils.getFlexibleDateRange(DayOfWeek.SUNDAY, 5);
// Returns: ["2025-09-21", "2025-09-26"] (example)
```

### **Logging Support**
```java
String[] dateRange = DateTimeUtils.getFlexibleDateRange(DayOfWeek.SUNDAY, 5);
String logMessage = DateTimeUtils.formatDateRangeForLogging(dateRange, DayOfWeek.SUNDAY, 5);
// Returns: "Date Range: 2025-09-21 (SUNDAY) to 2025-09-26 (5 days duration)"
```

## Benefits

### **1. Maintainability**
- **Easy Test Data Changes**: Change from Friday+3 to Sunday+5 with one parameter change
- **No Hardcoded Dates**: All dates calculated dynamically
- **Type Safety**: Enum-based day selection prevents typos

### **2. Scalability**
- **Multiple Scenarios**: Support different booking patterns
- **Test Parameterization**: Easy to create data-driven tests
- **Framework Reusability**: Other page objects can use same patterns

### **3. Backward Compatibility**
- **Existing Tests Unchanged**: No impact on current test suite
- **Gradual Migration**: Teams can adopt new methods at their own pace
- **Zero Breaking Changes**: All existing functionality preserved

## Test Suite Configuration

### **Updated agoda-test-suite.xml**
```xml
<test name="TC03 - Flexible Date Search" preserve-order="true">
    <parameter name="testDescription" value="Demonstrate flexible date selection"/>
    <classes>
        <class name="tests.agoda.TC03_FlexibleDateSearchTest"/>
    </classes>
</test>
```

## Execution Commands

### **Run Traditional Tests**
```bash
mvn clean test -Dtest=TC01_SearchAndSortHotelTest  # Uses Friday + 3 days
mvn clean test -Dtest=TC02_SearchAndFilterHotelTest  # Uses Friday + 3 days
```

### **Run Flexible Date Tests**
```bash
mvn clean test -Dtest=TC03_FlexibleDateSearchTest  # Uses Sunday + 5 days, Wednesday + 7 days
mvn clean test -Dtest=TC02_SearchAndFilterHotelTest#testSearchAndFilterHotelFlexibleDates  # Uses Sunday + 5 days
```

### **Run Complete Agoda Suite**
```bash
mvn clean test -DsuiteXmlFile=agoda-test-suite.xml
```

## Summary

This implementation provides a robust, flexible, and maintainable solution for date selection in test automation:

- ✅ **Flexible**: Support any day of week + any duration
- ✅ **Type-Safe**: Enum-based approach prevents errors  
- ✅ **Backward Compatible**: No breaking changes
- ✅ **Well-Documented**: Comprehensive logging and Allure steps
- ✅ **Maintainable**: Easy to modify test scenarios
- ✅ **Scalable**: Framework can be extended for other booking sites

The refactoring transforms hardcoded date logic into a flexible, parameterized system that can easily adapt to changing test requirements while maintaining full backward compatibility.