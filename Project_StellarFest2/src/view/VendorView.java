package view;

import controllers.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Event;
import models.Invitations;

import java.util.List;
import java.util.Optional;

public class VendorView extends Application {
    private Controller controller = new Controller(); // Initialize controller
    private int vendorId; // Assuming vendorId is passed after login
    
    public VendorView(int vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vendor Dashboard");

        Button acceptInvitation = new Button("Accept Invitation");
        Button viewAcceptedEvents = new Button("View Accepted Events");
        Button viewAcceptedEventDetails = new Button("View Accepted Event Details");
        Button viewInvitations = new Button("View Invitations");
        Button manageProducts = new Button("Manage Products");

        VBox layout = new VBox(10, acceptInvitation, viewAcceptedEvents, viewAcceptedEventDetails, viewInvitations, manageProducts);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Event Handlers
        acceptInvitation.setOnAction(e -> handleAcceptInvitation());
        viewAcceptedEvents.setOnAction(e -> handleViewAcceptedEvents());
        viewAcceptedEventDetails.setOnAction(e -> handleViewAcceptedEventDetails());
        viewInvitations.setOnAction(e -> handleViewPendingInvitations());
        manageProducts.setOnAction(e -> handleManageProducts());
    }

    // 1. Accept an Invitation
    private void handleAcceptInvitation() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Accept Invitation");
        dialog.setHeaderText("Enter Invitation ID to Accept:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(invitationId -> {
            boolean success = Controller.acceptEventInvitation(Integer.parseInt(invitationId));
            showAlert(success ? "Invitation Accepted!" : "Failed to accept invitation.");
        });
    }

    // 2. View Accepted Events
    private void handleViewAcceptedEvents() {
        List<Event> events = Controller.fetchVendorAcceptedEvents(vendorId);
        if (events.isEmpty()) {
            showAlert("No accepted events found.");
            return;
        }
        ListView<String> listView = new ListView<>();
        for (Event event : events) {
            listView.getItems().add(event.getId() + ": " + event.getName());
        }

        showListViewWindow("Accepted Events", listView);
    }

    // 3. View Accepted Event Details
    private void handleViewAcceptedEventDetails() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Event Details");
        dialog.setHeaderText("Enter Event ID to View Details:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(eventId -> {
            Event event = Controller.fetchVendorAcceptedEventDetails(Integer.parseInt(eventId), vendorId);
            if (event != null) {
                showAlert("Event Details:\n" +
                          "Name: " + event.getName() + "\n" +
                          "Date: " + event.getDate() + "\n" +
                          "Location: " + event.getLocation() + "\n" +
                          "Description: " + event.getDescription());
            } else {
                showAlert("No details found for this event.");
            }
        });
    }

    // 4. View Pending Invitations
    private void handleViewPendingInvitations() {
        List<Invitations> invitations = Controller.fetchPendingVendorInvitations(vendorId);
        if (invitations.isEmpty()) {
            showAlert("No pending invitations found.");
            return;
        }

        ListView<String> listView = new ListView<>();
        for (Invitations invitation : invitations) {
            listView.getItems().add("Invitation ID: " + invitation.getId() +
                                    ", Event ID: " + invitation.getEventId());
        }

        showListViewWindow("Pending Invitations", listView);
    }

    // 5. Manage Product Information
    private void handleManageProducts() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Manage Products");
        idDialog.setHeaderText("Enter Product ID to Update:");
        Optional<String> idResult = idDialog.showAndWait();

        idResult.ifPresent(productId -> {
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Update Product");
            nameDialog.setHeaderText("Enter New Product Name:");
            Optional<String> nameResult = nameDialog.showAndWait();

            TextInputDialog descDialog = new TextInputDialog();
            descDialog.setTitle("Update Product");
            descDialog.setHeaderText("Enter New Product Description:");
            Optional<String> descResult = descDialog.showAndWait();

            if (nameResult.isPresent() && descResult.isPresent()) {
                boolean success = Controller.updateVendorProduct(
                        Integer.parseInt(productId),
                        nameResult.get(),
                        descResult.get()
                );
                showAlert(success ? "Product updated successfully!" : "Failed to update product.");
            }
        });
    }

    // Helper method to show an alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    // Helper method to display a list in a new window
    private void showListViewWindow(String title, ListView<String> listView) {
        Stage stage = new Stage();
        stage.setTitle(title);
        VBox layout = new VBox(listView);
        Scene scene = new Scene(layout, 300, 400);
        stage.setScene(scene);
        stage.show();
    }
}