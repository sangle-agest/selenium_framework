import java.time.LocalDate;

public class TestDateTimeFix {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println("Today: " + today + " (" + today.getDayOfWeek() + ")");
        System.out.println();
        
        // Test the NEW logic that should now be used for both public and private methods
        System.out.println("=== UPDATED LOGIC (1-week buffer applied to all methods) ===");
        
        // This simulates what happens with <NEXT_FRIDAY> placeholder parsing
        System.out.println("Expected behavior for <NEXT_FRIDAY> placeholder:");
        LocalDate fridayWithBuffer = today.plusWeeks(1).with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.FRIDAY));
        LocalDate sundayWithBuffer = fridayWithBuffer.plusDays(3); // <PLUS_3_DAYS> from Friday
        
        System.out.println("Check-in (Friday): " + fridayWithBuffer + " -> " + fridayWithBuffer.getMonth());
        System.out.println("Check-out (+3 days): " + sundayWithBuffer + " -> " + sundayWithBuffer.getMonth());
        System.out.println();
        
        System.out.println("Both dates should now be in OCTOBER, not September!");
        System.out.println("This should fix the Agoda date picker issue.");
    }
}