package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view_controllers.ChangeProfileViewController;

public class VendorView extends Application {
    private int vendorId; // Vendor's ID for context

    public VendorView(int vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vendor Dashboard");

        // Existing buttons
        Button acceptInvitation = new Button("Accept Invitation");
        Button viewAcceptedEvents = new Button("View Accepted Events");
        Button viewAcceptedEventDetails = new Button("View Accepted Event Details");
        Button viewInvitations = new Button("View Invitations");
        Button manageVendors = new Button("Manage Vendors");

        // New button: Change Profile
        Button changeProfileButton = new Button("Change Profile");
        changeProfileButton.setOnAction(e -> openChangeProfileView());

        // Layout
        VBox layout = new VBox(10, acceptInvitation, viewAcceptedEvents, viewAcceptedEventDetails, 
                               viewInvitations, manageVendors, changeProfileButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openChangeProfileView() {
        Stage profileStage = new Stage();
        ChangeProfileView changeProfileView = new ChangeProfileView(vendorId); // Pass vendorId
        changeProfileView.start(profileStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
