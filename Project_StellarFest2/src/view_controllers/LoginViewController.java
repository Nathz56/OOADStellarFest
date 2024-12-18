package view_controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import controllers.Controller;
import javafx.stage.Stage;
import view.AdminView;
import view.EOView;
import view.GuestView;
import view.LoginView;
import view.RegisterView;
import view.VendorView;
import models.User;

public class LoginViewController {
    private LoginView view;
    private Controller controller;

    public LoginViewController(LoginView view) {
        this.view = view;
        this.controller = new Controller();
    }

    public void handleLogin(Stage primaryStage) {
        String email = view.getEmail();
        String password = view.getPassword();

        User user = controller.login(email, password);

        if (user != null) {
            view.displayMessage("Login successful! Welcome, " + user.getUsername());
            
            if("Admin".equals(user.getRole())) {
            	AdminView adminView = new AdminView(user.getId());
            	adminView.start(primaryStage);
            }else if("Guest".equals(user.getRole())) {
            	GuestView guestView = new GuestView(user.getId());
                guestView.start(primaryStage);
            }else if("Vendor".equals(user.getRole())) {
            	VendorView vendorView = new VendorView(user.getId()); // Pass vendorId
                vendorView.start(primaryStage);
            }else if("Event Organizer".equals(user.getRole())) {
            	EOView eoView = new EOView(user.getId());
            	eoView.start(primaryStage);
            }
        } else {
            view.displayMessage("Invalid email or password.");
        }
    }
    public void redirectToRegister(Stage primaryStage) {
        RegisterView registerView = new RegisterView();
        registerView.start(primaryStage);
    }
    
};

