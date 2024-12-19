package view_controllers;

import models.Model;
import view.ChangeProfileView;

public class ChangeProfileViewController {

    private ChangeProfileView view;
    private Model model;

    public ChangeProfileViewController(ChangeProfileView view) {
        this.view = view;
        this.model = new Model();
    }

    public void handleUpdate() {
        String email = view.getEmail();
        String username = view.getUsername();
        String password = view.getPassword();

        if ((email == null || email.isEmpty()) && 
            (username == null || username.isEmpty()) && 
            (password == null || password.isEmpty())) {
            view.displayMessage("Please fill in at least one field to update.");
            return;
        }

        int userId = getLoggedInUserId(); 

        boolean success = model.changeProfile(userId, email, username, password);

        if (success) {
            view.displayMessage("Profile updated successfully!");
        } else {
            view.displayMessage("Failed to update profile. Email or username may already be in use.");
        }
    }

    private int getLoggedInUserId() {
        return 1;
    }
}
