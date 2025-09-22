import core.utils.DateTimeUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class TestDateTimeUtils {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println("Today: " + today + " (" + today.getDayOfWeek() + ")");
        System.out.println();
        
        System.out.println("=== FRIDAY METHODS (Updated with 1-week buffer) ===");
        System.out.println("getNextFriday(): " + DateTimeUtils.getNextFriday());
        System.out.println("getNextFridayPlus3Days(): " + DateTimeUtils.getNextFridayPlus3Days());
        String[] agodaRange = DateTimeUtils.getAgodaDateRange();
        System.out.println("getAgodaDateRange(): [" + agodaRange[0] + ", " + agodaRange[1] + "]");
        System.out.println();
        
        System.out.println("=== GENERIC DAY METHODS (Should now be consistent) ===");
        System.out.println("getNextDayOfWeek(FRIDAY): " + DateTimeUtils.getNextDayOfWeek(DayOfWeek.FRIDAY));
        System.out.println("getNextDayOfWeek(MONDAY): " + DateTimeUtils.getNextDayOfWeek(DayOfWeek.MONDAY));
        System.out.println("getNextDayOfWeekPlusDays(FRIDAY, 3): " + DateTimeUtils.getNextDayOfWeekPlusDays(DayOfWeek.FRIDAY, 3));
        System.out.println();
        
        System.out.println("=== CONSISTENCY CHECK ===");
        String fridaySpecific = DateTimeUtils.getNextFriday();
        String fridayGeneric = DateTimeUtils.getNextDayOfWeek(DayOfWeek.FRIDAY);
        System.out.println("Friday-specific method: " + fridaySpecific);
        System.out.println("Generic Friday method:  " + fridayGeneric);
        System.out.println("Are they the same? " + fridaySpecific.equals(fridayGeneric));
        System.out.println();
        
        System.out.println("=== FLEXIBLE DATE RANGE ===");
        String[] flexibleRange = DateTimeUtils.getFlexibleDateRange(DayOfWeek.FRIDAY, 3);
        System.out.println("getFlexibleDateRange(FRIDAY, 3): [" + flexibleRange[0] + ", " + flexibleRange[1] + "]");
    }
}