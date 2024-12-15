package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view_controllers.ChangeProfileViewController;

public class ChangeProfileView extends Application {

    private TextField emailField, usernameField;
    private PasswordField passwordField;
    private Button updateButton;

    private ChangeProfileViewController controller;

    public ChangeProfileView() {
        controller = new ChangeProfileViewController(this);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Change Profile");

        emailField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();

        updateButton = new Button("Update Profile");
        updateButton.setOnAction(e -> controller.handleUpdate());

        VBox layout = new VBox(10, new Label("New Email"), emailField,
                                    new Label("New Username"), usernameField,
                                    new Label("New Password"), passwordField,
                                    updateButton);

        Scene scene = new Scene(layout, 300, 300);
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

    public void displayMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
