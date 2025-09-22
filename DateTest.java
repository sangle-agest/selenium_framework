import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.time.format.DateTimeFormatter;

public class DateTest {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println("Today: " + today + " (" + today.getDayOfWeek() + ")");
        System.out.println();
        
        // Test our updated Friday methods
        System.out.println("=== FRIDAY METHODS (Updated with 1-week buffer) ===");
        LocalDate nextFriday = today.plusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        LocalDate checkOut = nextFriday.plusDays(3);
        System.out.println("getNextFriday(): " + nextFriday);
        System.out.println("getNextFridayPlus3Days(): " + checkOut);
        System.out.println("getAgodaDateRange(): [" + nextFriday + ", " + checkOut + "]");
        System.out.println();
        
        // Test generic day methods (these might be inconsistent)
        System.out.println("=== GENERIC DAY METHODS (Current logic) ===");
        LocalDate nextFridayGeneric = today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        LocalDate nextMondayGeneric = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        System.out.println("getNextDayOfWeek(FRIDAY): " + nextFridayGeneric);
        System.out.println("getNextDayOfWeek(MONDAY): " + nextMondayGeneric);
        System.out.println("getNextDayOfWeekPlusDays(FRIDAY, 3): " + nextFridayGeneric.plusDays(3));
        System.out.println();
        
        // Show the inconsistency
        System.out.println("=== INCONSISTENCY CHECK ===");
        System.out.println("Friday-specific method: " + nextFriday);
        System.out.println("Generic Friday method:  " + nextFridayGeneric);
        System.out.println("Are they the same? " + nextFriday.equals(nextFridayGeneric));
        System.out.println();
        
        // Test flexible date range
        System.out.println("=== FLEXIBLE DATE RANGE ===");
        String checkInFlexible = nextFridayGeneric.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String checkOutFlexible = nextFridayGeneric.plusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("getFlexibleDateRange(FRIDAY, 3): [" + checkInFlexible + ", " + checkOutFlexible + "]");
    }
}