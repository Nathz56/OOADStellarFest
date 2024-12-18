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

public class EOView extends Application {
	private int eoId;

	public EOView(int eoId) {
		super();
		this.eoId = eoId;
	}

	@Override
	public void start(Stage primaryStage){
		
		
		primaryStage.setTitle("Selamat datang di menu EO");
		
		Button viewOrganizedEvents= new Button("View Organized Event");
	    Button viewOrganizedEventsDetails = new Button("View Organized Event Details");
	    Button addVendors = new Button ("Add Vendors");
	    Button addGuests = new Button ("Add Guests");
	    Button editEventName = new Button ("Edit Event Name");
	    Button createEvent = new Button ("Create Event");
	    Button changeProfileButton = new Button("Change Profile");
	    
	    changeProfileButton.setOnAction(e -> openChangeProfileView(primaryStage));
	    
	    VBox layout = new VBox(5, viewOrganizedEvents, viewOrganizedEventsDetails, addVendors, addGuests, editEventName, createEvent, changeProfileButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	private void openChangeProfileView(Stage primaryStage) {
	    Stage profileStage = new Stage();
	    ChangeProfileView profileView = new ChangeProfileView(eoId);
	    profileView.start(profileStage);
	}

}
