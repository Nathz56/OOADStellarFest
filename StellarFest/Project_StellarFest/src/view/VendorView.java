package view;

import controllers.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Event;

public class VendorView extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Selamat datang di menu Vendor");
		
		Button acceptInvitation = new Button("Accept Invitation");
	    Button viewAcceptedEvents = new Button("View Accepted Events");
	    Button viewAcceptedEventDetails = new Button ("View Accepted Event Details");
	    Button viewInvitations = new Button ("View Invitation");
	    Button manageVendors = new Button ("Manage Vendor");
	    
	    VBox layout = new VBox(5, acceptInvitation, viewAcceptedEvents, viewAcceptedEventDetails,viewInvitations, manageVendors);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}

}
