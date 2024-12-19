package view;

import controllers.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Event;
import models.Invitations;

import java.util.List;
import java.util.Optional;

public class VendorView extends Application {
	private Controller controller = new Controller(); // Initialize controller
	private int vendorId; // Vendor ID is set after initialization

	// No-argument constructor for JavaFX
	public VendorView() {}

	// Setter method to provide the vendor ID
	public void setVendorId(int vendorId) {
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

		// Attach handlers
		acceptInvitation.setOnAction(e -> handleAcceptInvitation());
		viewAcceptedEvents.setOnAction(e -> handleViewAcceptedEvents());
		viewAcceptedEventDetails.setOnAction(e -> handleViewAcceptedEventDetails());
		viewInvitations.setOnAction(e -> handleViewPendingInvitations());
		manageProducts.setOnAction(e -> handleManageProducts());
	}

	private void handleAcceptInvitation() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Accept Invitation");
		dialog.setHeaderText("Enter Invitation ID to Accept:");
		Optional<String> result = dialog.showAndWait();

		result.ifPresent(invitationId -> {
			try {
				int id = Integer.parseInt(invitationId);
				boolean success = controller.acceptEventInvitation(id);
				showAlert(success ? "Invitation Accepted!" : "Failed to accept invitation.");
			} catch (NumberFormatException e) {
				showAlert("Invalid invitation ID.");
			}
		});
	}

	private void handleViewAcceptedEvents() {
		List<Event> events = controller.fetchVendorAcceptedEvents(vendorId);
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

	private void handleViewAcceptedEventDetails() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("View Event Details");
		dialog.setHeaderText("Enter Event ID to View Details:");
		Optional<String> result = dialog.showAndWait();

		result.ifPresent(eventId -> {
			try {
				int id = Integer.parseInt(eventId);
				Event event = controller.fetchVendorAcceptedEventDetails(id, vendorId);
				if (event != null) {
					showAlert("Event Details:\n" +
							"Name: " + event.getName() + "\n" +
							"Date: " + event.getDate() + "\n" +
							"Location: " + event.getLocation() + "\n" +
							"Description: " + event.getDescription());
				} else {
					showAlert("No details found for this event.");
				}
			} catch (NumberFormatException e) {
				showAlert("Invalid event ID.");
			}
		});
	}

	private void handleViewPendingInvitations() {
		if (vendorId <= 0) {
			showAlert("Invalid vendor ID.");
			return;
		}

		List<Invitations> invitations = controller.fetchPendingVendorInvitations(vendorId);
		if (invitations.isEmpty()) {
			showAlert("No pending invitations found.");
			return;
		}

		ListView<String> listView = new ListView<>();
		for (Invitations invitation : invitations) {
			listView.getItems().add("Invitation ID: " + invitation.getId() + ", Event ID: " + invitation.getEventId());
		}

		showListViewWindow("Pending Invitations", listView);
	}


	private void handleManageProducts() {
		TextInputDialog idDialog = new TextInputDialog();
		idDialog.setTitle("Manage Products");
		idDialog.setHeaderText("Enter Product ID to Update:");
		Optional<String> idResult = idDialog.showAndWait();

		idResult.ifPresent(productId -> {
			try {
				int id = Integer.parseInt(productId);
				TextInputDialog nameDialog = new TextInputDialog();
				nameDialog.setTitle("Update Product");
				nameDialog.setHeaderText("Enter New Product Name:");
				Optional<String> nameResult = nameDialog.showAndWait();

				TextInputDialog descDialog = new TextInputDialog();
				descDialog.setTitle("Update Product");
				descDialog.setHeaderText("Enter New Product Description:");
				Optional<String> descResult = descDialog.showAndWait();

				if (nameResult.isPresent() && descResult.isPresent()) {
					String name = nameResult.get().trim();
					String description = descResult.get().trim();

					if (name.isEmpty() || description.isEmpty() || description.length() > 200) {
						showAlert("Invalid product data.");
						return;
					}

					boolean success = controller.updateVendorProduct(id, name, description);
					showAlert(success ? "Product updated successfully!" : "Failed to update product.");
				}
			} catch (NumberFormatException e) {
				showAlert("Invalid product ID.");
			}
		});
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(message);
		alert.show();
	}

	private void showListViewWindow(String title, ListView<String> listView) {
		Stage stage = new Stage();
		stage.setTitle(title);
		VBox layout = new VBox(listView);
		Scene scene = new Scene(layout, 300, 400);
		stage.setScene(scene);
		stage.show();
	}
}
