package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.CalendarUtil;
import info.ryandorman.simplescheduler.common.ComboBoxOption;
import info.ryandorman.simplescheduler.common.AlertUtil;
import info.ryandorman.simplescheduler.dao.AppointmentDao;
import info.ryandorman.simplescheduler.dao.AppointmentDaoImpl;
import info.ryandorman.simplescheduler.dao.ContactDao;
import info.ryandorman.simplescheduler.dao.ContactDaoImpl;
import info.ryandorman.simplescheduler.model.Appointment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardViewController implements Initializable {

    AppointmentDao appointmentDao = new AppointmentDaoImpl();
    ContactDao contactDao = new ContactDaoImpl();

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
        setupDatePickers();
        setupAggregationComboBox();
        setupContactComboBox();
        setupScheduleTableView();
    }

    @FXML
    public void onDateFilter() {
        ZonedDateTime startDate = startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endDate = endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault());

        if (startDate.compareTo(endDate) < 0) {
            loadAppointmentsInWindow(startDate, endDate);
            // populateContactSchedule();
            // populateCustomerAppointments();
            // populateUserWorkload();
        } else {
            AlertUtil.warning("Date Input", "Invalid Filter Range",
                    "Make sure your start date occurs at least one day before your end date.");
        }

    }

    private void setupDatePickers() {
        startDatePicker.setValue(CalendarUtil.getFirstDayOfYear().toLocalDate());
        endDatePicker.setValue(CalendarUtil.getLastDayOfYear().toLocalDate());
    }

    private void setupAggregationComboBox() {
        // Options = By Type, By Month, By Type and Month
        ObservableList<ComboBoxOption> aggregationOptions = FXCollections.observableArrayList();
        aggregationOptions.add(new ComboBoxOption(1, "Type", "type"));
        aggregationOptions.add(new ComboBoxOption(2, "Month", "Month"));
        aggregationOptions.add(new ComboBoxOption(3, "Type and Month", "typeAndMonth"));

        aggregationComboBox.setConverter(ComboBoxOption.getComboBoxConverter(aggregationOptions));
        aggregationComboBox.setItems(aggregationOptions);

        // change listener to update bar graph
    }

    private void setupContactComboBox() {
        ObservableList<ComboBoxOption> contactOptions = contactDao.getAll()
                .stream()
                .map(con -> new ComboBoxOption(con.getId(), con.getName(), con))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        contactComboBox.setConverter(ComboBoxOption.getComboBoxConverter(contactOptions));
        contactComboBox.setItems(contactOptions);

        // Change listener to update schedule
    }

    private void setupScheduleTableView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yy h:mm a");

        // Setup Schedule Table View Columns
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
