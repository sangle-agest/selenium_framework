# TC03 XPath Capture Guide

## Overview
This document outlines the XPath elements that need to be manually captured from the Agoda website to complete the TC03 test case implementation. The test infrastructure has been created, but the actual element locators need to be captured from the live website.

## Test Case: TC03 - Search, Filter, and Verify Hotel Details Successfully

### Test Flow Summary:
1. Search for hotels in Da Nang (3 days from next Friday, 2 rooms, 4 adults)
2. Apply swimming pool filter
3. Select 5th hotel and verify details
4. Go back and verify filter still active
5. Hover over 1st hotel for reviews
6. Verify review popup categories
7. Select 1st hotel for final verification

---

## Required XPath Elements to Capture

### 1. Swimming Pool Filter Elements (AgodaSearchResultsPage.java)

**Location**: Search results page after performing hotel search
**File to update**: `/src/main/java/pages/agoda/AgodaSearchResultsPage.java`

```java
// Current placeholder XPaths that need to be replaced:
private static final String SWIMMING_POOL_FILTER_XPATH = "//placeholder-swimming-pool-filter";
private static final String SWIMMING_POOL_FILTER_CHECKBOX_XPATH = "//placeholder-swimming-pool-checkbox";
```

**XPaths needed:**
- **Swimming Pool Filter Button/Section**: The clickable element to access swimming pool filtering options
- **Swimming Pool Checkbox**: The actual checkbox or toggle to enable swimming pool filter

**How to capture:**
1. Navigate to Agoda.com
2. Search for hotels in Da Nang
3. Look for facility/amenity filters section
4. Find the swimming pool filter option
5. Inspect the element and copy the XPath

---

### 2. Hotel Detail Page Elements (AgodaHotelDetailPage.java)

**Location**: Individual hotel detail page
**File to update**: `/src/main/java/pages/agoda/AgodaHotelDetailPage.java`

```java
// Current placeholder XPaths that need to be replaced:
private static final String HOTEL_NAME_XPATH = "//placeholder-hotel-name";
private static final String HOTEL_LOCATION_XPATH = "//placeholder-hotel-location";
private static final String AMENITIES_SECTION_XPATH = "//placeholder-amenities-section";
private static final String BACK_TO_RESULTS_XPATH = "//placeholder-back-to-results";
```

**XPaths needed:**
- **Hotel Name**: The main hotel title/name on the detail page
- **Hotel Location**: The location/address information
- **Amenities Section**: The section containing all hotel facilities/amenities
- **Back to Results Button**: Button to return to search results

**How to capture:**
1. From search results, click on any hotel to open detail page
2. Inspect each required element
3. Copy the XPath for each element

---

### 3. Review Popup Elements (AgodaSearchResultsPage.java)

**Location**: Search results page when hovering over hotel cards
**File to update**: `/src/main/java/pages/agoda/AgodaSearchResultsPage.java`

```java
// Current placeholder XPaths that need to be replaced:
private static final String REVIEW_POPUP_XPATH = "//placeholder-review-popup";
private static final String REVIEW_CATEGORIES_XPATH = "//placeholder-review-categories";
```

**XPaths needed:**
- **Review Popup Container**: The popup/tooltip that appears when hovering over hotels
- **Review Categories**: Elements containing review categories (Cleanliness, Facilities, Service, Location, Value for money)

**How to capture:**
1. On search results page, hover over a hotel card
2. Wait for review popup to appear
3. Inspect the popup container and category elements
4. Copy the XPaths

---

### 4. Hotel Selection Elements (AgodaSearchResultsPage.java)

**Location**: Search results page - hotel list items
**File to update**: `/src/main/java/pages/agoda/AgodaSearchResultsPage.java`

```java
// Current placeholder XPaths that need to be replaced:
private static final String HOTEL_CARD_XPATH = "//placeholder-hotel-card";
```

**XPaths needed:**
- **Hotel Card/Item**: Individual hotel result items in the search results list

**How to capture:**
1. On search results page, inspect individual hotel result cards
2. Find a common XPath that can identify any hotel card by position
3. The XPath should be parameterizable (e.g., using position or index)

---

## Implementation Steps After XPath Capture

### Step 1: Update AgodaSearchResultsPage.java
Replace the placeholder XPaths with actual captured XPaths:

```java
// Replace these placeholders with real XPaths:
private static final String SWIMMING_POOL_FILTER_XPATH = "//your-captured-xpath";
private static final String SWIMMING_POOL_FILTER_CHECKBOX_XPATH = "//your-captured-xpath";
private static final String REVIEW_POPUP_XPATH = "//your-captured-xpath";
private static final String REVIEW_CATEGORIES_XPATH = "//your-captured-xpath";
private static final String HOTEL_CARD_XPATH = "//your-captured-xpath";
```

### Step 2: Update AgodaHotelDetailPage.java
Replace the placeholder XPaths with actual captured XPaths:

```java
// Replace these placeholders with real XPaths:
private static final String HOTEL_NAME_XPATH = "//your-captured-xpath";
private static final String HOTEL_LOCATION_XPATH = "//your-captured-xpath";
private static final String AMENITIES_SECTION_XPATH = "//your-captured-xpath";
private static final String BACK_TO_RESULTS_XPATH = "//your-captured-xpath";
```

### Step 3: Test the Implementation
1. Run the TC03 test case: `tc03_testSearchFilterAndVerifyHotelDetails()`
2. Monitor the test execution and logs
3. Fix any element identification issues
4. Adjust XPaths if elements are not found or interactions fail

---

## Test Data Configuration

The test data has already been configured in `/src/test/resources/agoda_test_data.json`:

```json
{
  "TC03_SearchFilterAndVerifyHotelDetails": {
    "destination": "Da Nang",
    "checkInDate": "3 days from next Friday",
    "checkOutDate": "5 days from next Friday", 
    "occupancy": {
      "rooms": 2,
      "adults": 4,
      "children": 0
    },
    "facilityFilters": {
      "swimmingPool": true
    },
    "selectHotelPosition": 5,
    "hoverHotelPosition": 1,
    "expectedReviewCategories": [
      "Cleanliness",
      "Facilities", 
      "Service",
      "Location",
      "Value for money"
    ]
  }
}
```

---

## Notes and Tips

### XPath Best Practices:
1. **Avoid absolute XPaths** - Use relative XPaths that are less likely to break
2. **Use stable attributes** - Prefer id, class, data attributes over position-based selectors
3. **Test XPaths in browser** - Use browser console to verify XPaths work: `$x("//your-xpath")`
4. **Consider multiple alternatives** - Some elements might need fallback locators

### Common XPath Patterns:
- `//div[@class='hotel-name']` - By class attribute
- `//h2[contains(@class, 'title')]` - Partial class match
- `//span[text()='Swimming Pool']` - By text content
- `//button[contains(text(), 'Back')]` - Partial text match
- `(//div[@class='hotel-card'])[5]` - Select 5th element by position

### Troubleshooting:
- If elements are not found, check if they load dynamically
- Some elements might need explicit waits
- Verify element visibility and clickability
- Check for iframe contexts

---

## Current Implementation Status

✅ **Completed:**
- TC03 test method structure
- AgodaHotelDetailPage.java page object
- Enhanced AgodaSearchResultsPage.java with TC03 methods  
- Test data configuration
- Helper verification methods
- Allure step annotations

⚠️ **Pending:**
- Manual XPath capture (documented above)
- XPath replacement in page object files
- Test execution and validation

🎯 **Ready for Testing:**
Once XPaths are captured and updated, the TC03 test case will be fully functional and ready for execution.