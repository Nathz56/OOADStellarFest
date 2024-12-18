package view_controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import view.ChangeProfileView;

public class ChangeProfileViewController {
    private ChangeProfileView view;
    private int userId; // The ID of the user whose profile is being updated

    public ChangeProfileViewController(ChangeProfileView view, int userId) {
        this.view = view;
        this.userId = userId; // Pass the current vendor's ID
    }

    public void handleUpdate() {
        String newEmail = view.getEmail();
        String newUsername = view.getUsername();
        String newPassword = view.getPassword();

        if (validateInputs(newEmail, newUsername, newPassword)) {
            if (updateUserProfile(userId, newEmail, newUsername, newPassword)) {
                view.displayMessage("Profile updated successfully!");
            } else {
                view.displayMessage("Failed to update profile. Please try again.");
            }
        } else {
            view.displayMessage("Please fill all fields correctly.");
        }
    }

    private boolean validateInputs(String email, String username, String password) {
        // Simple validation to check non-empty fields
        return email != null && !email.trim().isEmpty() &&
               username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty();
    }

    private boolean updateUserProfile(int userId, String email, String username, String password) {
        String url = "jdbc:mysql://localhost:3306/stelarfestt"; // Update with your DB URL
        String dbUser = "root"; // Your database username
        String dbPassword = ""; // Your database password

        String query = "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, userId); // Ensure the user is updated by ID

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Return true if the update succeeded

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
