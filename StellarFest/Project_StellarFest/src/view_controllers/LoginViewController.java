package view_controllers;

import controllers.Controller;
import javafx.stage.Stage;
import view.*;
import models.User;

public class LoginViewController {
    private final LoginView view;
    private final Controller controller;
    private User loggedInUser;

    public LoginViewController(LoginView view) {
        this.view = view;
        this.controller = new Controller();
    }

    public void handleLogin(Stage primaryStage) {
        String email = view.getEmail();
        String password = view.getPassword();

        User user = controller.login(email, password);

        if (user != null) {
            loggedInUser = user; // Save the logged-in user
            view.displayMessage("Login successful! Welcome, " + user.getUsername());

            switch (user.getRole()) {
                case "Admin":
                    AdminView adminView = new AdminView();
                    adminView.start(primaryStage);
                    break;

                case "Guest":
                    GuestView guestView = new GuestView(user.getId());
                    guestView.start(primaryStage);
                    break;

                case "Vendor":
                    VendorView vendorView = new VendorView();
                    vendorView.setVendorId(user.getId()); // Set the vendor ID
                    vendorView.start(primaryStage);
                    break;


                case "Event Organizer":
                    EventOrganizerView eoView = new EventOrganizerView();
                    eoView.start(primaryStage);
                    break;

                default:
                    view.displayMessage("Unknown role: " + user.getRole());
            }
        } else {
            view.displayMessage("Invalid email or password.");
        }
    }

    public void redirectToRegister(Stage primaryStage) {
        RegisterView registerView = new RegisterView();
        registerView.start(primaryStage);
    }
}
