package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView();
        loginView.start(primaryStage);
    }
}
