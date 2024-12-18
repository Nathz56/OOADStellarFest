package view;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view_controllers.LoginViewController;


public class LoginView extends Application {

    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Hyperlink registerLink;

    private LoginViewController controller;

    public LoginView() {
        controller = new LoginViewController(this);
    }

//    public static void main(String[] args) {
//        launch(args);
//    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("StellarFest Login");

        // Setup form UI
        emailField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login");

        loginButton.setOnAction(e -> controller.handleLogin(primaryStage));
        
        registerLink = new Hyperlink("Don't have an acoount? Register");
        registerLink.setOnAction(e -> controller.redirectToRegister(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(new Label("Email"), emailField, new Label("Password"), passwordField, loginButton, registerLink);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
    


    public void displayMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
}
