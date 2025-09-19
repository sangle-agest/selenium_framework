# Dynamic Date Placeholder System

## Overview
The Selenium Framework now supports dynamic date placeholders in test data JSON files, allowing tests to use flexible date expressions instead of hardcoded dates. This eliminates the need to manually update test dates and makes tests more maintainable.

## Features Implemented

### 1. DateTimeUtils Enhancement
- **File**: `src/main/java/core/utils/DateTimeUtils.java`
- **New Method**: `parseDatePlaceholder(String dateExpression, String referenceDate)`
- **Purpose**: Converts date placeholder expressions into actual dates

### 2. AgodaTestDataProvider Integration
- **File**: `src/main/java/core/data/providers/AgodaTestDataProvider.java`
- **Enhancement**: Automatically processes date placeholders when loading test data
- **Feature**: Uses check-in date as reference for check-out date calculations

### 3. Test Data JSON Update
- **File**: `src/test/resources/test_data/agoda/agoda_test_data.json`
- **Change**: Replaced hardcoded dates with dynamic placeholders

## Supported Date Placeholders

### Basic Date Placeholders
| Placeholder | Description | Example Result |
|------------|-------------|----------------|
| `<TODAY>` | Current date | 2025-09-19 |
| `<TOMORROW>` | Next day | 2025-09-20 |
| `<YESTERDAY>` | Previous day | 2025-09-18 |

### Day of Week Placeholders
| Placeholder | Description | Example Result |
|------------|-------------|----------------|
| `<NEXT_FRIDAY>` | Next Friday from today | 2025-09-26 |
| `<NEXT_MONDAY>` | Next Monday from today | 2025-09-22 |
| `<NEXT_TUESDAY>` | Next Tuesday from today | 2025-09-23 |
| `<NEXT_WEDNESDAY>` | Next Wednesday from today | 2025-09-24 |
| `<NEXT_THURSDAY>` | Next Thursday from today | 2025-09-25 |
| `<NEXT_SATURDAY>` | Next Saturday from today | 2025-09-20 |
| `<NEXT_SUNDAY>` | Next Sunday from today | 2025-09-21 |

### Relative Date Placeholders
| Placeholder | Description | Example Result |
|------------|-------------|----------------|
| `<PLUS_X_DAYS>` | X days from reference date | `<PLUS_3_DAYS>` = +3 days |
| `<MINUS_X_DAYS>` | X days before reference date | `<MINUS_2_DAYS>` = -2 days |
| `<PLUS_X_WEEKS>` | X weeks from reference date | `<PLUS_2_WEEKS>` = +14 days |
| `<PLUS_X_MONTHS>` | X months from reference date | `<PLUS_1_MONTHS>` = +1 month |

### Monthly Placeholders
| Placeholder | Description | Example Result |
|------------|-------------|----------------|
| `<NEXT_WEEK>` | One week from today | 2025-09-26 |
| `<NEXT_MONTH>` | One month from today | 2025-10-19 |
| `<FIRST_DAY_OF_MONTH>` | First day of current month | 2025-09-01 |
| `<LAST_DAY_OF_MONTH>` | Last day of current month | 2025-09-30 |
| `<FIRST_DAY_OF_NEXT_MONTH>` | First day of next month | 2025-10-01 |
| `<LAST_DAY_OF_NEXT_MONTH>` | Last day of next month | 2025-10-31 |

### Legacy Support
| Placeholder | Description | Example Result |
|------------|-------------|----------------|
| `<NEXT_FROM_CHECK_IN_DATE_X_DAY>` | X days from check-in date | `<NEXT_FROM_CHECK_IN_DATE_3_DAY>` |

## Usage Examples

### JSON Test Data
```json
{
  "testCases": {
    "TC01_SearchAndSortHotel": {
      "testName": "TC01: Search and Sort Hotel Successfully",
      "destination": "Da Nang",
      "checkInDate": "<NEXT_FRIDAY>",
      "checkOutDate": "<PLUS_3_DAYS>",
      "occupancy": {
        "rooms": 2,
        "adults": 3,
        "children": 0
      }
    }
  }
}
```

### Runtime Processing
When the test runs:
1. **Check-in Date**: `<NEXT_FRIDAY>` → `2025-09-26` (next Friday from today)
2. **Check-out Date**: `<PLUS_3_DAYS>` → `2025-09-29` (3 days after check-in date)

### Debug Logging
The system provides detailed debug logs:
```
DEBUG [main] core.utils.DateTimeUtils - Processing placeholder: NEXT_FRIDAY
DEBUG [main] core.utils.DateTimeUtils - Parsed placeholder '<NEXT_FRIDAY>' to date: 2025-09-26
DEBUG [main] c.d.providers.AgodaTestDataProvider - Processed check-in date placeholder '<NEXT_FRIDAY>' -> '2025-09-26'
DEBUG [main] core.utils.DateTimeUtils - Processing placeholder: PLUS_3_DAYS
DEBUG [main] core.utils.DateTimeUtils - Using reference date: 2025-09-26
DEBUG [main] core.utils.DateTimeUtils - Parsed placeholder '<PLUS_3_DAYS>' to date: 2025-09-29
```

## Reference Date Logic

### Check-in Date Processing
- Uses current date as base for calculation
- Example: `<NEXT_FRIDAY>` calculates from today's date

### Check-out Date Processing
- Uses check-in date as reference for calculation
- Example: `<PLUS_3_DAYS>` calculates from the processed check-in date
- This ensures check-out date is always relative to check-in date

## Benefits

1. **Maintenance Free**: No need to update test dates manually
2. **Always Valid**: Dates are always in the future and valid for booking
3. **Flexible**: Easy to modify test duration by changing placeholders
4. **Readable**: Placeholders clearly express intent (e.g., "next Friday + 3 days")
5. **Consistent**: Same logic across all test data files

## Error Handling

### Unsupported Placeholders
If an unsupported placeholder is used, the system throws a clear error message:
```
RuntimeException: Unsupported date placeholder: INVALID_PLACEHOLDER. 
Supported placeholders: TODAY, TOMORROW, YESTERDAY, NEXT_FRIDAY, ...
```

### Invalid Date Expressions
- Non-placeholder strings (not enclosed in `< >`) are returned as-is
- This maintains backward compatibility with existing hardcoded dates

## Extension Points

### Adding New Placeholders
To add new placeholders, modify the switch statement in `DateTimeUtils.parseDatePlaceholder()`:

```java
case "NEXT_WEEKEND":
    resultDate = getNextDayOfWeek(baseDate, DayOfWeek.SATURDAY);
    break;
```

### Custom Date Patterns
The system supports pattern-based placeholders:
- `PLUS_X_DAYS` pattern handles any number of days
- `PLUS_X_WEEKS` pattern handles any number of weeks
- `PLUS_X_MONTHS` pattern handles any number of months

## Testing Verification

The implementation has been tested with:
- ✅ TC01_SearchAndSortHotelTest successfully using dynamic dates
- ✅ Proper date calculations verified in logs
- ✅ Reference date functionality working correctly
- ✅ Error handling for unsupported placeholders
- ✅ Backward compatibility with non-placeholder dates

## Date Format
All processed dates use the format: `yyyy-MM-dd` (e.g., `2025-09-26`)