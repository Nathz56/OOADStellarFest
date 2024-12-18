package controllers;

import models.EventOrganizerModel;

import java.util.ArrayList;

public class EventOrganizerController {
    private final EventOrganizerModel model;

    public EventOrganizerController(EventOrganizerModel model) {
        this.model = model;
    }

    // 1. Get Organized Events
    public ArrayList<String[]> getOrganizedEvents(int organizerId) {
        return model.getOrganizedEvents(organizerId);
    }

    // 2. Get Event Details
    public String[] getEventDetails(int eventId) {
        return model.getEventDetails(eventId);
    }

    // 3. Add Vendor or Guest
    public String addAttendee(int eventId, String attendeeName, String attendeeType) {
        if (attendeeName == null || attendeeName.isEmpty()) {
            return "Invalid input: Attendee name cannot be empty.";
        }
        boolean result = model.addAttendee(eventId, attendeeName, attendeeType);
        return result ? attendeeType + " added successfully!" : "Failed to add " + attendeeType + ".";
    }


    // 4. Update Event Name
    public String updateEventName(int eventId, String newName) {
        boolean result = model.updateEventName(eventId, newName);
        return result ? "Event name updated successfully!" : "Failed to update event name.";
    }

    // 5. Create Event
    public String createEvent(int organizerId, String name, String date, String location, String description) {
        if (name.isEmpty() || date == null || location.length() < 5 || description.length() > 200) {
            return "Invalid input: Ensure all fields are filled correctly.";
        }
        boolean result = model.createEvent(organizerId, name, date, location, description);
        return result ? "Event created successfully!" : "Failed to create event.";
    }

    public ArrayList<String> getEventGuests(int eventId) {
        return model.getEventGuests(eventId);
    }

    public ArrayList<String> getEventVendors(int eventId) {
        return model.getEventVendors(eventId);
    }

    public ArrayList<String[]> getAvailableVendors(int eventId) {
        return model.getAvailableVendors(eventId);
    }

    public String inviteVendors(int eventId, ArrayList<Integer> vendorIds) {
        if (vendorIds.isEmpty()) {
            return "Please select at least one vendor to invite.";
        }

        for (int vendorId : vendorIds) {
            boolean success = model.inviteVendor(eventId, vendorId);
            if (!success) {
                return "Failed to invite vendor with ID: " + vendorId;
            }
        }
        return "Vendors invited successfully!";
    }

    public ArrayList<String[]> getAvailableGuests(int eventId) {
        return model.getAvailableGuests(eventId);
    }

    public String inviteGuests(int eventId, ArrayList<Integer> guestIds) {
        if (guestIds.isEmpty()) {
            return "Please select at least one guest to invite.";
        }

        for (int guestId : guestIds) {
            boolean success = model.inviteGuest(eventId, guestId);
            if (!success) {
                return "Failed to invite guest with ID: " + guestId;
            }
        }
        return "Guests invited successfully!";
    }


}
