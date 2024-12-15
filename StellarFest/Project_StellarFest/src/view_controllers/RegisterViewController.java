package view_controllers;  

import models.Model;  
import view.RegisterView;  

public class RegisterViewController {  

    private RegisterView view;  
    private Model model;  

    // Constructor to initialize the view and model  
    public RegisterViewController(RegisterView view) {  
        this.view = view;  
        this.model = new Model(); // Ensure the model is properly initialized  
    }  

    // Handle the registration process  
    public void handleRegister() {  
        // Retrieve user input from the view  
        String email = view.getEmail();  
        String username = view.getUsername();  
        String password = view.getPassword();  
        String role = view.getRole();  

        // Validate fields (ensure they are not null or empty)  
        if (email == null || email.isEmpty()) {  
            view.displayMessage("Email cannot be empty.");  
            return;  
        }  
        if (username == null || username.isEmpty()) {  
            view.displayMessage("Username cannot be empty.");  
            return;  
        }  
        if (password == null || password.isEmpty()) {  
            view.displayMessage("Password cannot be empty.");  
            return;  
        }  
        if (role == null || role.isEmpty()) {  
            view.displayMessage("Please select a role.");  
            return;  
        }  

        // Attempt to register the user  
        boolean success = model.register(email, username, password, role);  

        // Display a success or failure message  
        if (success) {  
            view.displayMessage("Registration successful!");  
            return;
        } else {  
            view.displayMessage("Registration failed. Email or username may already be in use.");  
        }  
    }  
}