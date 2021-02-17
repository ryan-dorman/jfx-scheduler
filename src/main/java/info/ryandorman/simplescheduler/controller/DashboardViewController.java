package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.ComboBoxOption;
import info.ryandorman.simplescheduler.dao.AppointmentDao;
import info.ryandorman.simplescheduler.dao.AppointmentDaoImpl;
import info.ryandorman.simplescheduler.model.Appointment;
import info.ryandorman.simplescheduler.model.Contact;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardViewController implements Initializable {

    AppointmentDao appointmentDao = new AppointmentDaoImpl();

    private List<Appointment> appointments = new ArrayList<>();

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<ComboBoxOption> aggregationComboBox;

    @FXML
    private ComboBox<ComboBoxOption> contactComboBox;

    @FXML
    private TableView<Appointment> scheduleTable;

    @FXML
    private TableColumn<Appointment, Integer> idColumn;

    @FXML
    private TableColumn<Appointment, String> startColumn;

    @FXML
    private TableColumn<Appointment, String> endColumn;

    @FXML
    private  TableColumn<Appointment, String> titleColumn;

    @FXML
    private  TableColumn<Appointment, String> typeColumn;

    @FXML
    private  TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private  TableColumn<Appointment, Integer> customerIdColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupScheduleTableView();
    }

    private void setupDatePickers() {

    }

    private void setupScheduleTableView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yy h:mm a");

        // Setup Appointments Table View Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        startColumn.setCellValueFactory(appointmentData ->
                new SimpleStringProperty(appointmentData.getValue().getStart().format(formatter)));
        endColumn.setCellValueFactory(appointmentData ->
                new SimpleStringProperty(appointmentData.getValue().getEnd().format(formatter)));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        customerIdColumn.setCellValueFactory(appointmentData ->
                new SimpleObjectProperty<>(appointmentData.getValue().getCustomer().getId()));
    }

    private void loadAppointmentsInWindow(ZonedDateTime start, ZonedDateTime end) {
        appointments.addAll(appointmentDao.getByStartDateTimeWindow(start, end));
    }

}
