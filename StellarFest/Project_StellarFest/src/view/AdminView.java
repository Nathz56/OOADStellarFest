package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import controllers.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Event;
import models.User;

public class AdminView extends Application {


	
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Menu");

        Button viewAllEventsButton = new Button("View All Events");
        Button viewEventDetailsButton = new Button("View Event Details");
        Button deleteEventButton = new Button("Delete Event");
        Button viewAllUsersButton = new Button("View All Users");
        Button deleteUserButton = new Button("Delete User");

        viewAllEventsButton.setOnAction(e -> showAllEvents(primaryStage));
        viewEventDetailsButton.setOnAction(e -> showEventDetails(primaryStage));
        deleteEventButton.setOnAction(e -> deleteEvent(primaryStage));
        viewAllUsersButton.setOnAction(e -> showAllUsers(primaryStage));
        deleteUserButton.setOnAction(e -> deleteUser(primaryStage));

        VBox layout = new VBox(10, viewAllEventsButton, viewEventDetailsButton, deleteEventButton, viewAllUsersButton, deleteUserButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAllEvents(Stage primaryStage) {
        List<Event> events = Controller.fetchAllEvents();
        VBox layout = new VBox(10);

        if (events.isEmpty()) {
            layout.getChildren().add(new Label("No events available."));
        } else {
            for (Event event : events) {
                Label eventLabel = new Label("ID: " + event.getId() + " | Name: " + event.getName());
                layout.getChildren().add(eventLabel);
            }
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> start(primaryStage));
        layout.getChildren().add(backButton);

        primaryStage.setScene(new Scene(layout, 400, 400));
    }



    private void showEventDetails(Stage primaryStage) {
        VBox layout = new VBox(10);

        Label idLabel = new Label("Enter Event ID:");
        TextField idField = new TextField();
        Button fetchDetailsButton = new Button("View the Event Details");

        Label eventDetailsLabel = new Label();
        Label guestDetailsLabel = new Label();
        Label vendorDetailsLabel = new Label();

        fetchDetailsButton.setOnAction(e -> {
            try {
                int eventId = Integer.parseInt(idField.getText());
                Event event = Controller.fetchEventDetails(eventId);

                if (event != null) {
                    // Display event details
                    StringBuilder eventDetails = new StringBuilder(
                            "Name: " + event.getName() + "\n" +
                                    "Date: " + event.getDate() + "\n" +
                                    "Location: " + event.getLocation() + "\n" +
                                    "Description: " + event.getDescription() + "\n"
                    );

                    // Display guest details
                    StringBuilder guestDetails = new StringBuilder("Guests:\n");
                    if (event.getGuestIds() != null && !event.getGuestIds().isEmpty()) {
                        for (Integer guestId : event.getGuestIds()) {
                            guestDetails.append(" - Guest ID: ").append(guestId).append("\n");
                        }
                    } else {
                        guestDetails.append("No guests assigned.\n");
                    }

                    // Display vendor details
                    StringBuilder vendorDetails = new StringBuilder("Vendors:\n");
                    if (event.getVendorIds() != null && !event.getVendorIds().isEmpty()) {
                        for (Integer vendorId : event.getVendorIds()) {
                            vendorDetails.append(" - Vendor ID: ").append(vendorId).append("\n");
                        }
                    } else {
                        vendorDetails.append("No vendors assigned.\n");
                    }

                    eventDetailsLabel.setText(eventDetails.toString());
                    guestDetailsLabel.setText(guestDetails.toString());
                    vendorDetailsLabel.setText(vendorDetails.toString());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Event not found!").showAndWait();
                }
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid Event ID!").showAndWait();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> start(primaryStage));

        layout.getChildren().addAll(idLabel, idField, fetchDetailsButton, eventDetailsLabel, guestDetailsLabel, vendorDetailsLabel, backButton);
        primaryStage.setScene(new Scene(layout, 400, 600));
    }



    private void deleteEvent(Stage primaryStage) {
        VBox layout = new VBox(10);

        Label idLabel = new Label("Enter Event ID to Delete:");
        TextField idField = new TextField();
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(e -> {
            int eventId = Integer.parseInt(idField.getText());
            boolean success = Controller.deleteEvent(eventId);

            if (success) {
                new Alert(Alert.AlertType.INFORMATION, "Event deleted successfully!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete event!").showAndWait();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> start(primaryStage));

        layout.getChildren().addAll(idLabel, idField, deleteButton, backButton);
        primaryStage.setScene(new Scene(layout, 400, 400));
    }


    private void showAllUsers(Stage primaryStage) {
        List<User> users = Controller.fetchAllUsers();
        VBox layout = new VBox(10);

        if (users.isEmpty()) {
            layout.getChildren().add(new Label("No users available."));
        } else {
            for (User user : users) {
                Label userLabel = new Label("ID: " + user.getId() + " | Name: " + user.getUsername());
                layout.getChildren().add(userLabel);
            }
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> start(primaryStage));
        layout.getChildren().add(backButton);

        primaryStage.setScene(new Scene(layout, 400, 400));
    }

    private void deleteUser(Stage primaryStage) {
        VBox layout = new VBox(10);

        Label idLabel = new Label("Enter User ID to Delete:");
        TextField idField = new TextField();
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(e -> {
            int userId = Integer.parseInt(idField.getText());
            boolean success = Controller.deleteUser(userId);

            if (success) {
                new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete user!").showAndWait();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> start(primaryStage));

        layout.getChildren().addAll(idLabel, idField, deleteButton, backButton);
        primaryStage.setScene(new Scene(layout, 400, 400));
    }

}
