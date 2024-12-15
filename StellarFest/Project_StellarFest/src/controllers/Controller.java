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

    
    
    // Login a user
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

    // Register a new user
    public boolean register(String email, String username, String password, String role) {
        if (email == null || email.isEmpty() || username == null || username.isEmpty()
                || password == null || password.isEmpty() || role == null || role.isEmpty()) {
            System.out.println("All fields must be filled for registration.");
            return false;
        }
        return model.register(email, username, password, role);
    }

    // Change user profile
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

//    //acc inviations
//    
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

    
    //show acc inv
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

            stmt.setInt(1, userId);  // Filter by userId
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

        
}

