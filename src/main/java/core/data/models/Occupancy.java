package core.data.models;

/**
 * Data model for occupancy information
 */
public class Occupancy {
    private int rooms;
    private int adults;
    private int children;

    // Default constructor for Gson
    public Occupancy() {}

    public Occupancy(int rooms, int adults, int children) {
        this.rooms = rooms;
        this.adults = adults;
        this.children = children;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return String.format("Occupancy{rooms=%d, adults=%d, children=%d}", rooms, adults, children);
    }
}