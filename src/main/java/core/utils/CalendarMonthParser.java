package core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for parsing calendar month information dynamically
 */
public class CalendarMonthParser {
    
    private static final Pattern VIETNAMESE_MONTH_PATTERN = Pattern.compile("Tháng\\s*(\\d+).*?(\\d{4})");
    private static final Pattern ENGLISH_MONTH_PATTERN = Pattern.compile("(\\w+)\\s+(\\d{4})");
    
    /**
     * Parse month and year from calendar caption text
     * Supports Vietnamese format: "Tháng 10 2025"
     * Supports English format: "October 2025"
     */
    public static MonthInfo parseMonthInfo(String monthText) {
        if (monthText == null || monthText.trim().isEmpty()) {
            return null;
        }
        
        // Try Vietnamese format first
        Matcher vietnameseMatcher = VIETNAMESE_MONTH_PATTERN.matcher(monthText);
        if (vietnameseMatcher.find()) {
            int month = Integer.parseInt(vietnameseMatcher.group(1));
            int year = Integer.parseInt(vietnameseMatcher.group(2));
            return new MonthInfo(month, year);
        }
        
        // Try English format
        Matcher englishMatcher = ENGLISH_MONTH_PATTERN.matcher(monthText);
        if (englishMatcher.find()) {
            String monthName = englishMatcher.group(1);
            int year = Integer.parseInt(englishMatcher.group(2));
            int month = parseEnglishMonthName(monthName);
            if (month > 0) {
                return new MonthInfo(month, year);
            }
        }
        
        return null;
    }
    
    /**
     * Calculate navigation difference between current and target month
     */
    public static int calculateMonthDifference(MonthInfo current, int targetYear, int targetMonth) {
        if (current == null) return 0;
        return (targetYear - current.year) * 12 + (targetMonth - current.month);
    }
    
    /**
     * Parse English month name to number
     */
    private static int parseEnglishMonthName(String monthName) {
        switch (monthName.toLowerCase()) {
            case "january": case "jan": return 1;
            case "february": case "feb": return 2;
            case "march": case "mar": return 3;
            case "april": case "apr": return 4;
            case "may": return 5;
            case "june": case "jun": return 6;
            case "july": case "jul": return 7;
            case "august": case "aug": return 8;
            case "september": case "sep": return 9;
            case "october": case "oct": return 10;
            case "november": case "nov": return 11;
            case "december": case "dec": return 12;
            default: return -1;
        }
    }
    
    /**
     * Immutable data class for month information
     */
    public static class MonthInfo {
        public final int month;
        public final int year;
        
        public MonthInfo(int month, int year) {
            this.month = month;
            this.year = year;
        }
        
        @Override
        public String toString() {
            return year + "-" + String.format("%02d", month);
        }
    }
}