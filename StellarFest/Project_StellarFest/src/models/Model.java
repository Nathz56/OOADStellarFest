package models;

import java.sql.*;

public class Model {

    private Connection connection;

    // Constructor to establish database connection
    public Model() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/stellarfest?useSSL=false&allowPublicKeyRetrieval=true",
                "root", 
                "" // Replace with your database password if necessary
            );
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    // Login a user
    public User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error during login query.");
            e.printStackTrace();
        }
        return null;
    }

    // Register a new user
    public boolean register(String email, String username, String password, String role) {
        String query = "INSERT INTO users (email, username, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error during registration query.");
            e.printStackTrace();
        }
        return false;
    }

    // Change user profile
    public boolean changeProfile(int userId, String email, String username, String password) {
        String query = "UPDATE users SET email = COALESCE(?, email), username = COALESCE(?, username), " +
                       "password = COALESCE(?, password) WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setInt(4, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error during profile update query.");
            e.printStackTrace();
        }
        return false;
    }
}
