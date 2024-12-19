package models;

import java.sql.*;
import java.util.ArrayList;

public class EventOrganizerModel {
    private Connection connection;

    public EventOrganizerModel() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    ""
            );
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> getOrganizedEvents(int organizerId) {
        ArrayList<String[]> events = new ArrayList<>();
        String query = "SELECT id, name, date, location FROM events WHERE organizer_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, organizerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                events.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("location")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public String[] getEventDetails(int eventId) {
        String query = "SELECT name, date, location, description FROM events WHERE id = ?";
        String[] details = new String[4];

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                details[0] = rs.getString("name");
                details[1] = rs.getString("date");
                details[2] = rs.getString("location");
                details[3] = rs.getString("description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public boolean addAttendee(int eventId, String attendeeName, String attendeeType) {
        String table = attendeeType.equalsIgnoreCase("vendor") ? "vendor" : "guests";
        String query = "INSERT INTO " + table + " (event_id, name) VALUES (?, ?)";

        System.out.println("Adding " + attendeeType + " to event ID: " + eventId);
        System.out.println("Executing Query: " + query);

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ps.setString(2, attendeeName);

            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows Affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error adding " + attendeeType + " to event.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEventName(int eventId, String newName) {
        String query = "UPDATE events SET name = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, newName);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createEvent(int organizerId, String name, String date, String location, String description) {
        String query = "INSERT INTO events (organizer_id, name, date, location, description) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, organizerId);
            ps.setString(2, name);
            ps.setDate(3, Date.valueOf(date));
            ps.setString(4, location);
            ps.setString(5, description);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getEventGuests(int eventId) {
        ArrayList<String> guests = new ArrayList<>();
        String query = "SELECT name FROM guests WHERE event_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                guests.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching guests.");
            e.printStackTrace();
        }
        return guests;
    }

    // Fetch Vendors for an Event
    public ArrayList<String> getEventVendors(int eventId) {
        ArrayList<String> vendors = new ArrayList<>();
        String query = "SELECT name FROM vendors WHERE event_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                vendors.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching vendors.");
            e.printStackTrace();
        }
        return vendors;
    }

    public ArrayList<String[]> getAvailableVendors(int eventId) {
        ArrayList<String[]> vendors = new ArrayList<>();
        String query = "SELECT id, name, description, vendorId FROM vendor " +
                "WHERE id NOT IN (SELECT vendor_id FROM event_vendors WHERE event_id = ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                vendors.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("vendorId")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error fetching available vendors.");
            e.printStackTrace();
        }
        return vendors;
    }

    public boolean sendVendorInvitation(int eventId, int vendorId) {
        String query = "INSERT INTO event_vendors (event_id, vendor_id) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ps.setInt(2, vendorId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error sending vendor invitation.");
            e.printStackTrace();
        }
        return false;
    }


    public ArrayList<String[]> getAvailableGuests(int eventId) {
        ArrayList<String[]> guests = new ArrayList<>();
        String query = "SELECT id, username, email FROM users " +
                "WHERE role = 'Guest' " +
                "AND id NOT IN (SELECT guest_id FROM event_guests WHERE event_id = ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                guests.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("username"),
                        rs.getString("email")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error fetching available guests.");
            e.printStackTrace();
        }
        return guests;
    }

    public boolean sendGuestInvitation(int eventId, int guestId) {
        String query = "INSERT INTO event_guests (event_id, guest_id) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ps.setInt(2, guestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error sending guest invitation.");
            e.printStackTrace();
        }
        return false;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true", "root", "");
    }
}
