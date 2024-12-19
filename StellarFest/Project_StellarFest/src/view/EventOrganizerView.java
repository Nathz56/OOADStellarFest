package view;

import controllers.EventOrganizerController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

public class EventOrganizerView extends Application {

    private EventOrganizerController controller;

    private TableView<String[]> eventTable;

    public EventOrganizerView() {
        controller = new EventOrganizerController(new models.EventOrganizerModel());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Event Organizer");

        Button viewEventsButton = new Button("View Organized Event");
        Button viewEventDetailsButton = new Button("View Organized Event Details");
        Button addVendorsButton = new Button("Add Vendors");
        Button addGuestsButton = new Button("Add Guests");
        Button editEventNameButton = new Button("Edit Event Name");
        Button createEventButton = new Button("Create Event");

        viewEventsButton.setOnAction(e -> showEventsWindow());
        viewEventDetailsButton.setOnAction(e -> askForEventIdAndShowDetails());
        addVendorsButton.setOnAction(e -> askForEventIdAndAddVendors());
        addGuestsButton.setOnAction(e -> askForEventIdAndAddGuests());
        editEventNameButton.setOnAction(e -> askForEventIdAndEditName());
        createEventButton.setOnAction(e -> createEventWindow());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(
                viewEventsButton,
                viewEventDetailsButton,
                addVendorsButton,
                addGuestsButton,
                editEventNameButton,
                createEventButton
        );

        Scene scene = new Scene(vbox, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private int askForEventId(String purpose) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Event ID");
        dialog.setHeaderText("Event ID Required");
        dialog.setContentText("Please enter the Event ID to " + purpose + ":");

        return dialog.showAndWait()
                .map(Integer::parseInt)
                .orElse(-1);
    }

    private void showEventsWindow() {
        Stage stage = new Stage();
        stage.setTitle("Organized Events");

        eventTable = new TableView<>();
        TableColumn<String[], String> idColumn = new TableColumn<>("Event ID");
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));

        TableColumn<String[], String> nameColumn = new TableColumn<>("Event Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));

        TableColumn<String[], String> dateColumn = new TableColumn<>("Event Date");
        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));

        eventTable.getColumns().addAll(idColumn, nameColumn, dateColumn);

        loadEvents();

        VBox root = new VBox(10, eventTable);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }

    private void loadEvents() {
        int organizerId = 1;
        ArrayList<String[]> events = controller.getOrganizedEvents(organizerId);
        ObservableList<String[]> data = FXCollections.observableArrayList(events);
        eventTable.setItems(data);
    }

    private void askForEventIdAndShowDetails() {
        int eventId = askForEventId("view event details");
        if (eventId == -1) return;

        String[] details = controller.getEventDetails(eventId);
        ArrayList<String> guests = controller.getEventGuests(eventId);
        ArrayList<String> vendors = controller.getEventVendors(eventId);

        Stage stage = new Stage();
        stage.setTitle("Event Details");

        Label nameLabel = new Label("Name: " + details[0]);
        Label dateLabel = new Label("Date: " + details[1]);
        Label locationLabel = new Label("Location: " + details[2]);
        Label descriptionLabel = new Label("Description: " + details[3]);

        Label guestLabel = new Label("Guests:");
        ListView<String> guestListView = new ListView<>();
        guestListView.getItems().addAll(guests);

        Label vendorLabel = new Label("Vendors:");
        ListView<String> vendorListView = new ListView<>();
        vendorListView.getItems().addAll(vendors);

        VBox root = new VBox(10, nameLabel, dateLabel, locationLabel, descriptionLabel,
                guestLabel, guestListView, vendorLabel, vendorListView);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 400, 500));
        stage.show();
    }

    private void askForEventIdAndAddVendors() {
        int eventId = askForEventId("add vendors");
        if (eventId == -1) return;

        Stage stage = new Stage();
        stage.setTitle("Add Vendors to Event ID: " + eventId);

        TableView<String[]> vendorTable = new TableView<>();
        TableColumn<String[], String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));

        TableColumn<String[], String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));

        TableColumn<String[], String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));

        TableColumn<String[], String> vendorIdColumn = new TableColumn<>("Vendor ID");
        vendorIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));

        TableColumn<String[], Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellFactory(tc -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(Boolean selected, boolean empty) {
                super.updateItem(selected, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    checkBox.setOnAction(e -> getTableRow().setUserData(checkBox.isSelected()));
                }
            }
        });

        vendorTable.getColumns().addAll(idColumn, nameColumn, descriptionColumn, vendorIdColumn, selectColumn);

        ArrayList<String[]> vendors = controller.getAvailableVendors(eventId);
        ObservableList<String[]> data = FXCollections.observableArrayList(vendors);
        vendorTable.setItems(data);

        Button inviteButton = new Button("Send Invitations");
        inviteButton.setOnAction(e -> {
            ArrayList<Integer> selectedVendorIds = new ArrayList<>();
            for (int i = 0; i < vendorTable.getItems().size(); i++) {
                if (vendorTable.getColumns().get(4).getCellObservableValue(i).getValue() != null) {
                    selectedVendorIds.add(Integer.parseInt(vendorTable.getItems().get(i)[0]));
                }
            }

            if (selectedVendorIds.isEmpty()) {
                showAlert("Please select at least one vendor to invite.");
                return;
            }

            String result = controller.inviteVendors(eventId, selectedVendorIds);
            showAlert(result);
            stage.close();
        });

        VBox root = new VBox(10, new Label("Select Vendors to Invite:"), vendorTable, inviteButton);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void askForEventIdAndAddGuests() {
        int eventId = askForEventId("add guests");
        if (eventId == -1) return;

        Stage stage = new Stage();
        stage.setTitle("Add Guests to Event ID: " + eventId);

        TableView<String[]> guestTable = new TableView<>();
        TableColumn<String[], String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));

        TableColumn<String[], String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));

        TableColumn<String[], String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));

        TableColumn<String[], Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellFactory(tc -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(Boolean selected, boolean empty) {
                super.updateItem(selected, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    checkBox.setOnAction(e -> getTableRow().setUserData(checkBox.isSelected()));
                }
            }
        });

        guestTable.getColumns().addAll(idColumn, nameColumn, emailColumn, selectColumn);

        ArrayList<String[]> guests = controller.getAvailableGuests(eventId);
        ObservableList<String[]> data = FXCollections.observableArrayList(guests);
        guestTable.setItems(data);

        Button inviteButton = new Button("Send Invitations");
        inviteButton.setOnAction(e -> {
            ArrayList<Integer> selectedGuestIds = new ArrayList<>();
            for (int i = 0; i < guestTable.getItems().size(); i++) {
                if (guestTable.getColumns().get(3).getCellObservableValue(i).getValue() != null) {
                    selectedGuestIds.add(Integer.parseInt(guestTable.getItems().get(i)[0]));
                }
            }

            if (selectedGuestIds.isEmpty()) {
                showAlert("Please select at least one guest to invite.");
                return;
            }

            String result = controller.inviteGuests(eventId, selectedGuestIds);
            showAlert(result);
            stage.close();
        });

        VBox root = new VBox(10, new Label("Select Guests to Invite:"), guestTable, inviteButton);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void askForEventIdAndEditName() {
        int eventId = askForEventId("edit event name");
        if (eventId == -1) return;

        Stage stage = new Stage();
        stage.setTitle("Edit Event Name");

        TextField nameField = new TextField();
        nameField.setPromptText("New Event Name");

        Button editButton = new Button("Update Event");
        editButton.setOnAction(e -> {
            String result = controller.updateEventName(eventId, nameField.getText());
            showAlert(result);
            stage.close();
        });

        VBox root = new VBox(10, new Label("Edit Event Name:"), nameField, editButton);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 300, 150));
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void createEventWindow() {
        Stage stage = new Stage();
        stage.setTitle("Create Event");

        // Form Inputs
        TextField nameField = new TextField();
        nameField.setPromptText("Event Name");
        nameField.setPrefWidth(250);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Event Date");
        datePicker.setPrefWidth(250);

        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        locationField.setPrefWidth(250);

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        descriptionField.setPrefWidth(250);

        Button createButton = new Button("Create Event");
        createButton.setOnAction(e -> {
            String result = controller.createEvent(1, nameField.getText(),
                    datePicker.getValue() != null ? datePicker.getValue().toString() : "",
                    locationField.getText(),
                    descriptionField.getText());
            showAlert(result);
            stage.close();
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(
                new Label("Event Name:"), nameField,
                new Label("Event Date:"), datePicker,
                new Label("Location:"), locationField,
                new Label("Description:"), descriptionField,
                createButton
        );

        stage.setScene(new Scene(root, 350, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
