package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.model.Appointment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class AppointmentsViewController implements Initializable {

    // Appointments Table
    @FXML
    TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> idColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, String> contactColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> startColumn;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> endColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    // Radio Buttons
    // TODO: filter options All, This Week, This Month

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup Appointments Table View Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(appointmentData ->
                new SimpleStringProperty(appointmentData.getValue().getContact().getName()));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdColumn.setCellValueFactory(appointmentData ->
                new SimpleObjectProperty<>(appointmentData.getValue().getCustomer().getId()));

        loadAppointments();
    }

    private void loadAppointments() {
        // TODO: AppointmentsDao.getAll
    }
}
