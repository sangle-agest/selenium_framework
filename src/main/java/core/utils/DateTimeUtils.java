package core.utils;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * DateTimeUtils provides utility methods for date and time operations
 */
public class DateTimeUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);
    
    // Common date/time formatters
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    public static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Get current date as string
     * @return Current date in yyyy-MM-dd format
     */
    @Step("Get current date")
    public static String getCurrentDate() {
        return getCurrentDate(DATE_FORMAT);
    }

    /**
     * Get current date with custom format
     * @param formatter Date formatter
     * @return Formatted current date
     */
    @Step("Get current date with format")
    public static String getCurrentDate(DateTimeFormatter formatter) {
        try {
            String date = LocalDate.now().format(formatter);
            logger.debug("Current date: {}", date);
            return date;
        } catch (Exception e) {
            logger.error("Failed to get current date: {}", e.getMessage());
            throw new RuntimeException("Failed to get current date", e);
        }
    }

    /**
     * Get current time as string
     * @return Current time in HH:mm:ss format
     */
    @Step("Get current time")
    public static String getCurrentTime() {
        return getCurrentTime(TIME_FORMAT);
    }

    /**
     * Get current time with custom format
     * @param formatter Time formatter
     * @return Formatted current time
     */
    @Step("Get current time with format")
    public static String getCurrentTime(DateTimeFormatter formatter) {
        try {
            String time = LocalTime.now().format(formatter);
            logger.debug("Current time: {}", time);
            return time;
        } catch (Exception e) {
            logger.error("Failed to get current time: {}", e.getMessage());
            throw new RuntimeException("Failed to get current time", e);
        }
    }

    /**
     * Get current date and time as string
     * @return Current datetime in yyyy-MM-dd HH:mm:ss format
     */
    @Step("Get current datetime")
    public static String getCurrentDateTime() {
        return getCurrentDateTime(DATETIME_FORMAT);
    }

    /**
     * Get current date and time with custom format
     * @param formatter DateTime formatter
     * @return Formatted current datetime
     */
    @Step("Get current datetime with format")
    public static String getCurrentDateTime(DateTimeFormatter formatter) {
        try {
            String datetime = LocalDateTime.now().format(formatter);
            logger.debug("Current datetime: {}", datetime);
            return datetime;
        } catch (Exception e) {
            logger.error("Failed to get current datetime: {}", e.getMessage());
            throw new RuntimeException("Failed to get current datetime", e);
        }
    }

    /**
     * Get timestamp for file naming
     * @return Timestamp in yyyyMMdd_HHmmss format
     */
    @Step("Get timestamp")
    public static String getTimestamp() {
        return getCurrentDateTime(TIMESTAMP_FORMAT);
    }

    /**
     * Add days to current date
     * @param days Number of days to add (can be negative)
     * @return Date after adding days
     */
    @Step("Add {days} days to current date")
    public static String addDaysToCurrentDate(long days) {
        return addDaysToCurrentDate(days, DATE_FORMAT);
    }

    /**
     * Add days to current date with custom format
     * @param days Number of days to add (can be negative)
     * @param formatter Date formatter
     * @return Date after adding days
     */
    @Step("Add {days} days to current date with format")
    public static String addDaysToCurrentDate(long days, DateTimeFormatter formatter) {
        try {
            String newDate = LocalDate.now().plusDays(days).format(formatter);
            logger.debug("Date after adding {} days: {}", days, newDate);
            return newDate;
        } catch (Exception e) {
            logger.error("Failed to add days to current date: {}", e.getMessage());
            throw new RuntimeException("Failed to add days to current date", e);
        }
    }

    /**
     * Add days to specific date
     * @param dateString Date string
     * @param days Number of days to add
     * @param formatter Date formatter
     * @return Date after adding days
     */
    @Step("Add {days} days to date {dateString}")
    public static String addDaysToDate(String dateString, long days, DateTimeFormatter formatter) {
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            String newDate = date.plusDays(days).format(formatter);
            logger.debug("Date {} after adding {} days: {}", dateString, days, newDate);
            return newDate;
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date {}: {}", dateString, e.getMessage());
            throw new RuntimeException("Failed to parse date: " + dateString, e);
        } catch (Exception e) {
            logger.error("Failed to add days to date: {}", e.getMessage());
            throw new RuntimeException("Failed to add days to date", e);
        }
    }

    /**
     * Subtract days from current date
     * @param days Number of days to subtract
     * @return Date after subtracting days
     */
    @Step("Subtract {days} days from current date")
    public static String subtractDaysFromCurrentDate(long days) {
        return addDaysToCurrentDate(-days);
    }

    /**
     * Get date difference in days
     * @param startDate Start date string
     * @param endDate End date string
     * @param formatter Date formatter
     * @return Number of days between dates
     */
    @Step("Get difference in days between {startDate} and {endDate}")
    public static long getDaysBetween(String startDate, String endDate, DateTimeFormatter formatter) {
        try {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            long days = ChronoUnit.DAYS.between(start, end);
            logger.debug("Days between {} and {}: {}", startDate, endDate, days);
            return days;
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse dates: {}", e.getMessage());
            throw new RuntimeException("Failed to parse dates", e);
        } catch (Exception e) {
            logger.error("Failed to calculate days between dates: {}", e.getMessage());
            throw new RuntimeException("Failed to calculate days between dates", e);
        }
    }

    /**
     * Check if date is today
     * @param dateString Date string
     * @param formatter Date formatter
     * @return True if date is today
     */
    @Step("Check if date {dateString} is today")
    public static boolean isToday(String dateString, DateTimeFormatter formatter) {
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            boolean today = date.equals(LocalDate.now());
            logger.debug("Date {} is today: {}", dateString, today);
            return today;
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date {}: {}", dateString, e.getMessage());
            throw new RuntimeException("Failed to parse date: " + dateString, e);
        }
    }

    /**
     * Check if date is in the past
     * @param dateString Date string
     * @param formatter Date formatter
     * @return True if date is in the past
     */
    @Step("Check if date {dateString} is in the past")
    public static boolean isPastDate(String dateString, DateTimeFormatter formatter) {
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            boolean past = date.isBefore(LocalDate.now());
            logger.debug("Date {} is in the past: {}", dateString, past);
            return past;
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date {}: {}", dateString, e.getMessage());
            throw new RuntimeException("Failed to parse date: " + dateString, e);
        }
    }

    /**
     * Check if date is in the future
     * @param dateString Date string
     * @param formatter Date formatter
     * @return True if date is in the future
     */
    @Step("Check if date {dateString} is in the future")
    public static boolean isFutureDate(String dateString, DateTimeFormatter formatter) {
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            boolean future = date.isAfter(LocalDate.now());
            logger.debug("Date {} is in the future: {}", dateString, future);
            return future;
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date {}: {}", dateString, e.getMessage());
            throw new RuntimeException("Failed to parse date: " + dateString, e);
        }
    }

    /**
     * Format date from one format to another
     * @param dateString Date string in source format
     * @param sourceFormat Source date formatter
     * @param targetFormat Target date formatter
     * @return Date string in target format
     */
    @Step("Convert date format from {sourceFormat} to {targetFormat}")
    public static String convertDateFormat(String dateString, DateTimeFormatter sourceFormat, DateTimeFormatter targetFormat) {
        try {
            LocalDate date = LocalDate.parse(dateString, sourceFormat);
            String convertedDate = date.format(targetFormat);
            logger.debug("Converted date {} to {}", dateString, convertedDate);
            return convertedDate;
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse date {} with format {}: {}", dateString, sourceFormat, e.getMessage());
            throw new RuntimeException("Failed to parse date: " + dateString, e);
        } catch (Exception e) {
            logger.error("Failed to convert date format: {}", e.getMessage());
            throw new RuntimeException("Failed to convert date format", e);
        }
    }

    /**
     * Get epoch timestamp in milliseconds
     * @return Current epoch timestamp
     */
    @Step("Get epoch timestamp")
    public static long getEpochTimestamp() {
        long timestamp = Instant.now().toEpochMilli();
        logger.debug("Current epoch timestamp: {}", timestamp);
        return timestamp;
    }

    /**
     * Convert epoch timestamp to formatted date
     * @param epochMillis Epoch timestamp in milliseconds
     * @param formatter Date formatter
     * @return Formatted date string
     */
    @Step("Convert epoch timestamp to date")
    public static String epochToDate(long epochMillis, DateTimeFormatter formatter) {
        try {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
            String formattedDate = dateTime.format(formatter);
            logger.debug("Converted epoch {} to date {}", epochMillis, formattedDate);
            return formattedDate;
        } catch (Exception e) {
            logger.error("Failed to convert epoch to date: {}", e.getMessage());
            throw new RuntimeException("Failed to convert epoch to date", e);
        }
    }

    /**
     * Get first day of current month
     * @return First day of month in yyyy-MM-dd format
     */
    @Step("Get first day of current month")
    public static String getFirstDayOfMonth() {
        return getFirstDayOfMonth(DATE_FORMAT);
    }

    /**
     * Get first day of current month with format
     * @param formatter Date formatter
     * @return First day of month
     */
    @Step("Get first day of current month with format")
    public static String getFirstDayOfMonth(DateTimeFormatter formatter) {
        try {
            String firstDay = LocalDate.now().withDayOfMonth(1).format(formatter);
            logger.debug("First day of current month: {}", firstDay);
            return firstDay;
        } catch (Exception e) {
            logger.error("Failed to get first day of month: {}", e.getMessage());
            throw new RuntimeException("Failed to get first day of month", e);
        }
    }

    /**
     * Get last day of current month
     * @return Last day of month in yyyy-MM-dd format
     */
    @Step("Get last day of current month")
    public static String getLastDayOfMonth() {
        return getLastDayOfMonth(DATE_FORMAT);
    }

    /**
     * Get last day of current month with format
     * @param formatter Date formatter
     * @return Last day of month
     */
    @Step("Get last day of current month with format")
    public static String getLastDayOfMonth(DateTimeFormatter formatter) {
        try {
            LocalDate today = LocalDate.now();
            String lastDay = today.withDayOfMonth(today.lengthOfMonth()).format(formatter);
            logger.debug("Last day of current month: {}", lastDay);
            return lastDay;
        } catch (Exception e) {
            logger.error("Failed to get last day of month: {}", e.getMessage());
            throw new RuntimeException("Failed to get last day of month", e);
        }
    }
}