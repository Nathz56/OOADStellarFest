package view_controllers;  

import javafx.stage.Stage;
import models.Model;
import view.LoginView;
import view.RegisterView;  

public class RegisterViewController {  

    private RegisterView view;  
    private Model model;  
  
    public RegisterViewController(RegisterView view) {  
        this.view = view;  
        this.model = new Model();  
    }  

    public void handleRegister(Stage primaryStage) {    
        String email = view.getEmail();  
        String username = view.getUsername();  
        String password = view.getPassword();  
        String role = view.getRole();

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

        boolean success = model.register(email, username, password, role);  

        if (success) {  
            view.displayMessage("Registration successful!");  
        } else {  
            view.displayMessage("Registration failed. Email or username may already be in use.");  
        }
        
    	LoginView loginView = new LoginView();
    	loginView.start(primaryStage);
        
    }
    
    public void redirectToLogin(Stage primaryStage) {
        LoginView loginView = new LoginView();
        loginView.start(primaryStage);
    }
}