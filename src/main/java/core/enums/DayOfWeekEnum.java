package core.enums;

import java.time.DayOfWeek;

/**
 * Enum for Day of Week selection with utility methods
 * Provides type-safe day selection for test automation
 */
public enum DayOfWeekEnum {
    MONDAY(DayOfWeek.MONDAY, "Monday"),
    TUESDAY(DayOfWeek.TUESDAY, "Tuesday"),
    WEDNESDAY(DayOfWeek.WEDNESDAY, "Wednesday"),
    THURSDAY(DayOfWeek.THURSDAY, "Thursday"),
    FRIDAY(DayOfWeek.FRIDAY, "Friday"),
    SATURDAY(DayOfWeek.SATURDAY, "Saturday"),
    SUNDAY(DayOfWeek.SUNDAY, "Sunday");

    private final DayOfWeek dayOfWeek;
    private final String displayName;

    DayOfWeekEnum(DayOfWeek dayOfWeek, String displayName) {
        this.dayOfWeek = dayOfWeek;
        this.displayName = displayName;
    }

    /**
     * Get the Java DayOfWeek enum value
     * @return DayOfWeek
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Get the display name of the day
     * @return String display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get DayOfWeekEnum from Java DayOfWeek
     * @param dayOfWeek Java DayOfWeek
     * @return DayOfWeekEnum
     */
    public static DayOfWeekEnum fromDayOfWeek(DayOfWeek dayOfWeek) {
        for (DayOfWeekEnum day : values()) {
            if (day.dayOfWeek == dayOfWeek) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid DayOfWeek: " + dayOfWeek);
    }

    /**
     * Get DayOfWeekEnum from string name (case insensitive)
     * @param name String name of the day
     * @return DayOfWeekEnum
     */
    public static DayOfWeekEnum fromString(String name) {
        for (DayOfWeekEnum day : values()) {
            if (day.displayName.equalsIgnoreCase(name) || day.name().equalsIgnoreCase(name)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid day name: " + name);
    }

    @Override
    public String toString() {
        return displayName;
    }
}