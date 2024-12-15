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

public class GuestView extends Application{

	
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Selamat datang di menu Guest");
		
		Button viewInvitationsButton = new Button("View Invitations");
	    Button viewAcceptedEventsButton = new Button("View Accepted Events");
		

//	    viewInvitationsButton.setOnAction(e -> showInvitations(primaryStage));
//        viewAcceptedEventsButton.setOnAction(e -> showAcceptedEvents());
//        backButton.setOnAction(e -> backMainMenu());
        
	    	
        VBox layout = new VBox(5, viewInvitationsButton, viewAcceptedEventsButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
	
	}
	
//	private void showInvitations(Stage primaryStage) {
//		
//		List<Event> invitations = Controller.fetchInvitations();
//		
//		 VBox layout = new VBox(10);
//	        for (Event event : invitations) {
//	            Label eventLabel = new Label(event.getName() + " - " + event.getDate());
//	            Button acceptButton = new Button("Accept");
//	            acceptButton.setOnAction(e -> {
//	                if (Controller.acceptInvitation(event.getId())) {
//	                    new Alert(Alert.AlertType.INFORMATION, "Accepted invitation for " + event.getName()).showAndWait();
//	                    showInvitations(primaryStage);
//	                } else {
//	                    new Alert(Alert.AlertType.ERROR, "Error accepting invitation.").showAndWait();
//	                }
//	            });
//	            layout.getChildren().addAll(eventLabel, acceptButton);
//	        }
//	        Button backButton = new Button("Back");
//	        backButton.setOnAction(e -> start(primaryStage)); // Go back to the main menu
//	        layout.getChildren().add(backButton);
//
//	        primaryStage.setScene(new Scene(layout, 300, 400));
//	}
	
//	private void showAcceptedEvents() {
//		List<Event> acceptedEvents = Controller.fetchAcceptedEvents();
//	}


}
