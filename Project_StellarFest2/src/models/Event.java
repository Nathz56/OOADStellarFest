package models;

import java.sql.Timestamp;

public class Event {
    private int id;
    private String name, location, description;
    private Timestamp date;

    public Event(int id, String name, Timestamp date, String location, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public Timestamp getDate() { return date; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
}
