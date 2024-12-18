package view_controllers;

import models.Model;
import view.ChangeProfileView;

public class ChangeProfileViewController {

    private ChangeProfileView view;
    private Model model;

    // Constructor to initialize the view and model
    public ChangeProfileViewController(ChangeProfileView view) {
        this.view = view;
        this.model = new Model(); // Ensure the model is properly initialized
    }

    // Handle the profile update process
    public void handleUpdate() {
        // Retrieve user input from the view
        String email = view.getEmail();
        String username = view.getUsername();
        String password = view.getPassword();

        // Validate fields (ensure they are not null)
        if ((email == null || email.isEmpty()) && 
            (username == null || username.isEmpty()) && 
            (password == null || password.isEmpty())) {
            view.displayMessage("Please fill in at least one field to update.");
            return;
        }

        // Replace with actual logged-in user ID
        int userId = getLoggedInUserId(); 

        // Attempt to update the profile
        boolean success = model.changeProfile(userId, email, username, password);

        // Display a success or failure message
        if (success) {
            view.displayMessage("Profile updated successfully!");
        } else {
            view.displayMessage("Failed to update profile. Email or username may already be in use.");
        }
    }

    // Temporary method to simulate retrieving a logged-in user ID
    private int getLoggedInUserId() {
        // Replace with actual session management logic
        return 1; // Hardcoded user ID for now
    }
}
