package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import models.Model;
import models.Event;
import models.User;
import models.Invitations;

public class Controller {
	private Model model;

	// Constructor to initialize the Model
	public Controller() {
		try {
			model = new Model(); // Ensure Model is properly initialized
		} catch (Exception e) {
			System.out.println("Error initializing the model.");
			e.printStackTrace();
		}
	}

	public User login(String email, String password) {
		if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
			System.out.println("Email or password cannot be null or empty.");
			return null;
		}
		try {
			return model.login(email, password);
		} catch (Exception e) {
			System.out.println("Error during login.");
			e.printStackTrace();
			return null;
		}
	}

	public boolean register(String email, String username, String password, String role) {
		if (email == null || email.isEmpty() || username == null || username.isEmpty()
				|| password == null || password.isEmpty() || role == null || role.isEmpty()) {
			System.out.println("All fields must be filled for registration.");
			return false;
		}
		return model.register(email, username, password, role);
	}

	public boolean changeProfile(int userId, String email, String username, String password) {
		if (userId <= 0) {
			System.out.println("Invalid user ID.");
			return false;
		}
		return model.changeProfile(userId, email, username, password);
	}

	public static List<Invitations> fetchInvitations(int userId) {
		List<Invitations> invitations = new ArrayList<>();
		String query = "SELECT * FROM invitations WHERE status = 'Pending' AND userid = ?";

		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
				"root",
				"");
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, userId); // Filter invitations by userId
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Invitations invitation = new Invitations(
							rs.getInt("id"),
							rs.getInt("eventid"),
							rs.getInt("userid"),
							rs.getString("status")
					);
					invitations.add(invitation);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return invitations;
	}

	public static boolean acceptInvitation(int id) {
		String query = "UPDATE invitations SET status = 'ACCEPTED' WHERE id = ?";

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true", "root", "");
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, id);
			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Invitation accepted!");
				return true;
			} else {
				System.out.println("No invitation found with the provided ID.");
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error while accepting the invitation.");
			return false;

		}
	}

	public static List<Event> fetchAcceptedEvents(int userId) {
		List<Event> events = new ArrayList<>();
		String query = "SELECT e.id, e.name, e.date, e.location, e.description " +
				"FROM events e " +
				"JOIN invitations i ON e.id = i.eventid " +
				"WHERE i.status = 'ACCEPTED' AND i.userid = ?";

		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
				"root",
				"");
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					events.add(new Event(
							rs.getInt("id"),
							rs.getString("name"),
							rs.getTimestamp("date"),
							rs.getString("location"),
							rs.getString("description")
					));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}

	//vendor controller

	public boolean acceptEventInvitation(int invitationId) {
		if (invitationId <= 0) {
			System.out.println("Invalid invitation ID.");
			return false;
		}

		String query = "UPDATE invitations SET status = 'ACCEPTED' WHERE id = ?";

		try (Connection conn = model.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, invitationId);
			int rowsUpdated = stmt.executeUpdate();

			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Event> fetchVendorAcceptedEvents(int vendorId) {
		if (vendorId <= 0) {
			System.out.println("Invalid vendor ID.");
			return new ArrayList<>();
		}

		List<Event> events = new ArrayList<>();
		String query = "SELECT e.id, e.name, e.date, e.location, e.description " +
				"FROM events e " +
				"JOIN invitations i ON e.id = i.eventid " +
				"WHERE i.status = 'ACCEPTED' AND i.userid = ?";

		try (Connection conn = model.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, vendorId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					events.add(new Event(
							rs.getInt("id"),
							rs.getString("name"),
							rs.getTimestamp("date"),
							rs.getString("location"),
							rs.getString("description")
					));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return events;
	}

	public Event fetchVendorAcceptedEventDetails(int eventId, int vendorId) {
		if (eventId <= 0 || vendorId <= 0) {
			System.out.println("Invalid event or vendor ID.");
			return null;
		}

		String query = "SELECT e.id, e.name, e.date, e.location, e.description " +
				"FROM events e " +
				"JOIN invitations i ON e.id = i.eventid " +
				"WHERE e.id = ? AND i.userid = ? AND i.status = 'ACCEPTED'";

		try (Connection conn = model.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, eventId);
			stmt.setInt(2, vendorId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new Event(
							rs.getInt("id"),
							rs.getString("name"),
							rs.getTimestamp("date"),
							rs.getString("location"),
							rs.getString("description")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Invitations> fetchPendingVendorInvitations(int vendorId) {
		if (vendorId <= 0) {
			System.out.println("Invalid vendor ID passed to fetchPendingVendorInvitations.");
			return new ArrayList<>();
		}

		List<Invitations> invitations = new ArrayList<>();
		String query = "SELECT * FROM invitations WHERE userid = ? AND status = 'Pending'";

		try (Connection conn = model.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, vendorId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					invitations.add(new Invitations(
							rs.getInt("id"),
							rs.getInt("eventid"),
							rs.getInt("userid"),
							rs.getString("status")
					));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return invitations;
	}

	public boolean updateVendorProduct(int productId, String productName, String productDescription) {
		if (productId <= 0 || productName == null || productName.trim().isEmpty() ||
				productDescription == null || productDescription.length() > 200) {
			System.out.println("Invalid product data.");
			return false;
		}

		String query = "UPDATE vendor SET name = ?, description = ? WHERE id = ?";

		try (Connection conn = model.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, productName);
			stmt.setString(2, productDescription);
			stmt.setInt(3, productId);

			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<User> fetchVendors() {
		List<User> vendors = new ArrayList<>();
		String query = "SELECT * FROM users WHERE role = 'Vendor'";

		try (Connection conn = model.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				vendors.add(new User(
						rs.getInt("id"),
						rs.getString("email"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("role")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vendors;
	}


	public boolean sendInvitation(int vendorId, int eventId) {
		String query = "INSERT INTO invitations (userid, eventid, status) VALUES (?, ?, 'Pending')";

		try (Connection conn = model.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, vendorId);
			stmt.setInt(2, eventId);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}






//admin controller dari sini

	public static List<Event> fetchAllEvents() {
		List<Event> events = new ArrayList<>();
		String query = "SELECT * FROM events";

		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
				"root",
				"");
			 PreparedStatement stmt = conn.prepareStatement(query);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				events.add(new Event(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getTimestamp("date"),
						rs.getString("location"),
						rs.getString("description")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return events;

	}

	public static Event fetchEventDetails(int eventId) {
		String eventQuery = "SELECT * FROM events WHERE id = ?";
		String guestQuery = "SELECT guest_id FROM event_guests WHERE event_id = ?";
		String vendorQuery = "SELECT vendor_id FROM event_vendors WHERE event_id = ?";
		Event event = null;

		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
				"root",
				"");
			 PreparedStatement eventStmt = conn.prepareStatement(eventQuery);
			 PreparedStatement guestStmt = conn.prepareStatement(guestQuery);
			 PreparedStatement vendorStmt = conn.prepareStatement(vendorQuery)) {

			// Fetch event details
			eventStmt.setInt(1, eventId);
			try (ResultSet eventRs = eventStmt.executeQuery()) {
				if (eventRs.next()) {
					event = new Event(
							eventRs.getInt("id"),
							eventRs.getString("name"),
							eventRs.getTimestamp("date"),
							eventRs.getString("location"),
							eventRs.getString("description")
					);
				}
			}

			if (event != null) {
				// Fetch associated guests
				guestStmt.setInt(1, eventId);
				try (ResultSet guestRs = guestStmt.executeQuery()) {
					List<Integer> guestIds = new ArrayList<>();
					while (guestRs.next()) {
						guestIds.add(guestRs.getInt("guestId"));
					}

				}

				// Fetch associated vendors
				vendorStmt.setInt(1, eventId);
				try (ResultSet vendorRs = vendorStmt.executeQuery()) {
					List<Integer> vendorIds = new ArrayList<>();
					while (vendorRs.next()) {
						vendorIds.add(vendorRs.getInt("vendorId"));
					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return event;
	}




	public static List<User> fetchAllUsers() {
		List<User> users = new ArrayList<>();
		String query = "SELECT * FROM users";

		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
				"root",
				"");
			 PreparedStatement stmt = conn.prepareStatement(query);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				users.add(new User(
						rs.getInt("id"),
						rs.getString("email"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("role")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	public static boolean deleteEvent(int eventId) {
		String query = "DELETE FROM events WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
				"root",
				"");
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, eventId);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteUser(int userId) {
		String query = "DELETE FROM users WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
				"root",
				"");
			 PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, userId);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}



