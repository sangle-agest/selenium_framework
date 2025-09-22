package core.enums;

/**
 * Enum for hotel star ratings used in filtering
 * Supports star ratings from 1 to 5 stars
 */
public enum StarRating {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int value;

    StarRating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Get StarRating from integer value
     * @param value the star rating value (1-5)
     * @return corresponding StarRating enum
     * @throws IllegalArgumentException if value is not between 1-5
     */
    public static StarRating fromValue(int value) {
        for (StarRating rating : StarRating.values()) {
            if (rating.getValue() == value) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid star rating value: " + value + ". Must be between 1 and 5.");
    }

    @Override
    public String toString() {
        return value + " star" + (value > 1 ? "s" : "");
    }
}