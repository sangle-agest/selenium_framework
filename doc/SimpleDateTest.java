import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

public class SimpleDateTest {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println("Today: " + today + " (" + today.getDayOfWeek() + ")");
        System.out.println();
        
        // Test the NEW logic that DateTimeUtils SHOULD be using now
        System.out.println("=== NEW LOGIC (1-week buffer) ===");
        LocalDate nextFridayWithBuffer = today.plusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        LocalDate nextMondayWithBuffer = today.plusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        System.out.println("Next Friday (with 1-week buffer): " + nextFridayWithBuffer);
        System.out.println("Next Monday (with 1-week buffer): " + nextMondayWithBuffer);
        System.out.println("Next Friday + 3 days: " + nextFridayWithBuffer.plusDays(3));
        System.out.println();
        
        // Test the OLD logic for comparison
        System.out.println("=== OLD LOGIC (no buffer) ===");
        LocalDate nextFridayOld = today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        LocalDate nextMondayOld = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        System.out.println("Next Friday (old logic): " + nextFridayOld);
        System.out.println("Next Monday (old logic): " + nextMondayOld);
        System.out.println("Next Friday + 3 days (old): " + nextFridayOld.plusDays(3));
        System.out.println();
        
        // Show the difference
        System.out.println("=== COMPARISON ===");
        System.out.println("Difference in days (Friday): " + (nextFridayWithBuffer.toEpochDay() - nextFridayOld.toEpochDay()));
        System.out.println("Difference in days (Monday): " + (nextMondayWithBuffer.toEpochDay() - nextMondayOld.toEpochDay()));
        
        // Check if our updated methods are working correctly
        System.out.println("\n=== VERIFICATION ===");
        System.out.println("All new dates should be in October or later (not September):");
        System.out.println("Friday with buffer: " + nextFridayWithBuffer + " -> Month: " + nextFridayWithBuffer.getMonth());
        System.out.println("Monday with buffer: " + nextMondayWithBuffer + " -> Month: " + nextMondayWithBuffer.getMonth());
    }
}