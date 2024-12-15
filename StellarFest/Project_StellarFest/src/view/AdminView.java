package view;

import java.util.List;

import controllers.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminView extends Application{


	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Selamat datang di menu Admin");
		
		Button viewAllEventsButton = new Button("View All Events");
	    Button viewEventDetailsButton = new Button("View Events Details");
	    Button deleteEvent = new Button ("Delete Event");
	    Button viewAllUsers = new Button ("View All Users");
	    Button deleteUsers = new Button ("Delete Users");
	    
	    VBox layout = new VBox(5, viewAllEventsButton, viewEventDetailsButton, deleteEvent,  viewAllUsers, deleteUsers);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
}
