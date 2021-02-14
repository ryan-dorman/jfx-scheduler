package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.CalendarUtil;
import info.ryandorman.simplescheduler.dao.AppointmentDao;
import info.ryandorman.simplescheduler.dao.AppointmentDaoImpl;
import info.ryandorman.simplescheduler.model.Appointment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentsViewController implements Initializable {

    private final AppointmentDao appointmentDao = new AppointmentDaoImpl();

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
    private TableColumn<Appointment, String> startColumn;

    @FXML
    private TableColumn<Appointment, String> endColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    // Radio Buttons
    @FXML
    private RadioButton allRadioButton;

    @FXML
    private  RadioButton thisWeekRadioButton;

    @FXML
    private RadioButton thisMonthRadioButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        // Setup Appointments Table View Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(appointmentData ->
                new SimpleStringProperty(appointmentData.getValue().getContact().getName()));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(appointmentData ->
                new SimpleStringProperty(appointmentData.getValue().getStart().format(formatter)));
        endColumn.setCellValueFactory(appointmentData ->
                new SimpleStringProperty(appointmentData.getValue().getEnd().format(formatter)));
        customerIdColumn.setCellValueFactory(appointmentData ->
                new SimpleObjectProperty<>(appointmentData.getValue().getCustomer().getId()));

        // Set default sort
        startColumn.setSortType(TableColumn.SortType.ASCENDING);
        appointmentsTable.getSortOrder().add(startColumn);

        // Setup radio buttons, default to all appointments shown
        ToggleGroup filters = new ToggleGroup();
        filters.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton selected = (RadioButton) filters.getSelectedToggle();
            if (selected != null) {
                switch (selected.getText()) {
                    case "This Week":
                        filterAppointments(CalendarUtil.getFirstDayOfWeek(), CalendarUtil.getLastDayOfWeek());
                        break;
                    case "This Month":
                        filterAppointments(CalendarUtil.getFirstDayOfMonth(), CalendarUtil.getLastDayOfMonth());
                        break;
                    default:
                        loadAppointments();
                }
            }
        });
        allRadioButton.setToggleGroup(filters);
        thisWeekRadioButton.setToggleGroup(filters);
        thisMonthRadioButton.setToggleGroup(filters);
        allRadioButton.setSelected(true);
    }

    @FXML
    public void onCreate() {}

    @FXML
    public void onUpdate() {}

    @FXML
    public void onDelete() {}

    private void loadAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentDao.getAll());
        appointmentsTable.setItems(appointments);
    }

    private void filterAppointments(ZonedDateTime start, ZonedDateTime end) {
        List<Appointment> filteredAppointments = appointmentDao.getByDateTimeWindow(start, end);
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(filteredAppointments);
        appointmentsTable.setItems(appointments);
    }
}
