package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private int id;
    private String name, location, description;
    private Timestamp date;
    private List<Integer> guestIds;
    private List<Integer> vendorIds;

    public Event(int id, String name, Timestamp date, String location, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.guestIds = new ArrayList<>();
        this.vendorIds = new ArrayList<>();
    }


    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public Timestamp getDate() { return date; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public List<Integer> getGuestIds() {
        return guestIds;
    }
    public List<Integer> getVendorIds() {
        return vendorIds;
    }
}
