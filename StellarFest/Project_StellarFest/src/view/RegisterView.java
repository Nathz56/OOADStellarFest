package view;  

import javafx.application.Application;  
import javafx.scene.Scene;  
import javafx.scene.control.*;  
import javafx.scene.layout.VBox;  
import javafx.stage.Stage;  
import view_controllers.RegisterViewController;  

public class RegisterView extends Application {  

    private TextField emailField, usernameField;  
    private PasswordField passwordField;  
    private ComboBox<String> roleBox;  
    private Button registerButton;  

    private RegisterViewController controller;  

    public RegisterView() {  
        controller = new RegisterViewController(this);  
    }  

    @Override  
    public void start(Stage primaryStage) {  
        primaryStage.setTitle("Register");  

        emailField = new TextField();  
        usernameField = new TextField();  
        passwordField = new PasswordField();  
        roleBox = new ComboBox<>();  
        roleBox.getItems().addAll("Event Organizer", "Vendor", "Guest", "Admin");  

        registerButton = new Button("Register");  
        registerButton.setOnAction(e -> controller.handleRegister());  

        VBox layout = new VBox(10, new Label("Email"), emailField,   
                                    new Label("Username"), usernameField,   
                                    new Label("Password"), passwordField,   
                                    new Label("Role"), roleBox,   
                                    registerButton);  

        Scene scene = new Scene(layout, 300, 300); // Adjusted height  
        primaryStage.setScene(scene);  
        primaryStage.show();  
    }  

    public String getEmail() {  
        return emailField.getText();  
    }  

    public String getUsername() {  
        return usernameField.getText();  
    }  

    public String getPassword() {  
        return passwordField.getText();  
    }  

    public String getRole() {  
        return roleBox.getValue();  
    }  

    public void displayMessage(String message) {  
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);  
        alert.showAndWait();  
    }  

    public static void main(String[] args) {  
        launch(args);  
    }  
}