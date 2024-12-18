package view;

import java.util.List;

import controllers.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
//import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Event;
import models.Invitations;

public class GuestView extends Application{

	private int userId;
	
	public GuestView(int userId) {
        this.userId = userId;
    }
	
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Selamat datang di menu Guest");
		
		Button viewInvitationsButton = new Button("View Invitations");
	    Button viewAcceptedEventsButton = new Button("View Accepted Events");
	    Button changeProfileButton = new Button("Change Profile");
		

	    viewInvitationsButton.setOnAction(e -> showInvitations(primaryStage));
        viewAcceptedEventsButton.setOnAction(e -> showAcceptedEvents(primaryStage));
        changeProfileButton.setOnAction(e -> openChangeProfileView(primaryStage, userId));
//        backButton.setOnAction(e -> backMainMenu());
        
	    	
        VBox layout = new VBox(5, viewInvitationsButton, viewAcceptedEventsButton, changeProfileButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
	
	}
	
	private void openChangeProfileView(Stage primaryStage, int userId) {
	    Stage profileStage = new Stage();
	    ChangeProfileView profileView = new ChangeProfileView(userId);
	    profileView.start(profileStage);
	}
	
	private void showInvitations(Stage primaryStage) {
	    List<Invitations> invitations = Controller.fetchInvitations(userId);
	    VBox layout = new VBox(10);

	    for (Invitations invitation : invitations) {
	        Label invitationLabel = new Label(
	            "Event ID: " + invitation.getEventId() + 
	            " | Status: " + invitation.getStatus()
	        );
	        Button acceptButton = new Button("Accept");

	        acceptButton.setOnAction(e -> {
	            if (Controller.acceptInvitation(invitation.getId())) {
	                new Alert(Alert.AlertType.INFORMATION, "Accepted invitation for Event ID " + invitation.getEventId()).showAndWait();
	                showInvitations(primaryStage);
	            } else {
	                new Alert(Alert.AlertType.ERROR, "Error accepting invitation.").showAndWait();
	            }
	        });

	        layout.getChildren().addAll(invitationLabel, acceptButton);
	    }

	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> start(primaryStage));
	    layout.getChildren().add(backButton);

	    primaryStage.setScene(new Scene(layout, 400, 400));
	}

	
	private void showAcceptedEvents(Stage primaryStage) {
	    List<Event> acceptedEvents = Controller.fetchAcceptedEvents(userId); 
	    VBox layout = new VBox(10);

	    if (acceptedEvents.isEmpty()) {
	        layout.getChildren().add(new Label("No accepted events available."));
	    } else {
	        for (Event event : acceptedEvents) {
	            Button eventButton = new Button(event.getName());
	            eventButton.setOnAction(e -> showEventDetails(primaryStage, event)); 
	            layout.getChildren().add(eventButton);
	        }
	    }

	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> start(primaryStage));
	    layout.getChildren().add(backButton);

	    primaryStage.setScene(new Scene(layout, 400, 400));
	}

	private void showEventDetails(Stage primaryStage, Event event) {
	    VBox layout = new VBox(10);

	    Label nameLabel = new Label("Name: " + event.getName());
	    Label dateLabel = new Label("Date: " + event.getDate());
	    Label locationLabel = new Label("Location: " + event.getLocation());
	    Label descriptionLabel = new Label("Description: " + event.getDescription());

	    layout.getChildren().addAll(nameLabel, dateLabel, locationLabel, descriptionLabel);

	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> showAcceptedEvents(primaryStage));
	    layout.getChildren().add(backButton);

	    primaryStage.setScene(new Scene(layout, 400, 400));
	}

	

}
