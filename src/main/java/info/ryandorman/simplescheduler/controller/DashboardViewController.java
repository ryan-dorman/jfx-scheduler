package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.AlertUtil;
import info.ryandorman.simplescheduler.common.CalendarUtil;
import info.ryandorman.simplescheduler.common.ComboBoxOption;
import info.ryandorman.simplescheduler.common.DatePickerInput;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardViewController implements Initializable {

    private final List<Appointment> appointments = new ArrayList<>();

    AppointmentDao appointmentDao = new AppointmentDaoImpl();
    ContactDao contactDao = new ContactDaoImpl();

    @FXML
    private DatePickerInput startDatePicker;

    @FXML
    private DatePickerInput endDatePicker;

    @FXML
    private ComboBox<ComboBoxOption> aggregationComboBox;

    @FXML
    private BarChart<String, Number> appointmentBarChart;

    @FXML
    private CategoryAxis appointmentXAxis;

    @FXML
    private NumberAxis appointmentYAxis;

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
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupDatePickers();
        setupAggregationComboBox();
        setupContactComboBox();
        setupScheduleTableView();
        onDateFilter();
    }

    @FXML
    public void onDateFilter() {
        ZonedDateTime startDate = startDatePicker.valueProperty().getValue().atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endDate = endDatePicker.valueProperty().getValue().atStartOfDay(ZoneId.systemDefault());

        if (startDate.compareTo(endDate) < 0) {
            loadAppointmentsInWindow(startDate, endDate);
            populateCustomerAppointments();
            populateUserWorkload();
            populateContactSchedule();
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
        ObservableList<ComboBoxOption> aggregationOptions = FXCollections.observableArrayList();
        ComboBoxOption type = new ComboBoxOption(1, "Type", "type");
        ComboBoxOption month = new ComboBoxOption(2, "Month", "month");
        ComboBoxOption typeAndMonth = new ComboBoxOption(3, "Type by Month", "typeByMonth");

        aggregationOptions.add(type);
        aggregationOptions.add(month);
        aggregationOptions.add(typeAndMonth);

        aggregationComboBox.setConverter(ComboBoxOption.getComboBoxConverter(aggregationOptions));
        aggregationComboBox.valueProperty().addListener((obs, oldVale, newValue) -> {
            if (newValue != null) {
                populateCustomerAppointments();
            }
        });

        aggregationComboBox.setItems(aggregationOptions);
        aggregationComboBox.setValue(type);
    }

    private void setupContactComboBox() {
        ObservableList<ComboBoxOption> contactOptions = contactDao.getAll()
                .stream()
                .map(con -> new ComboBoxOption(con.getId(), con.getName(), con))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        contactComboBox.setConverter(ComboBoxOption.getComboBoxConverter(contactOptions));
        contactComboBox.valueProperty().addListener((obs, oldVale, newValue) -> {
            if (newValue != null) {
                populateContactSchedule();
            }
        });

        contactComboBox.setItems(contactOptions);

        if (contactOptions.size() > 0) {
            contactComboBox.setValue(contactOptions.get(0));
        }
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

    private void populateCustomerAppointments() {
        ComboBoxOption aggregation = aggregationComboBox.getValue();

        if (aggregation != null) {
            Map<String, Long> counts = new HashMap<>();

            appointmentBarChart.setLegendVisible(false);
            appointmentXAxis.setLabel(aggregation.getLabel());
            appointmentYAxis.setLabel("Appointments");
            appointmentYAxis.setMinorTickVisible(false);
            appointmentYAxis.setMinorTickCount(0);

            switch ((String) aggregation.getValue()) {
                case "type":
                    counts = appointments.stream()
                            .collect(Collectors.groupingBy(a -> a.getType().toUpperCase(Locale.ROOT),
                                    Collectors.counting()));
                    break;
                case "month":
                    counts = appointments.stream().collect(Collectors.groupingBy(a ->
                                    a.getStart().getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            Collectors.counting()));
                    break;
                case "typeByMonth":
                    counts = appointments.stream().collect(Collectors.groupingBy(a ->
                                    a.getStart().getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + "=" +
                                            a.getType(),
                            Collectors.counting()));
                    break;
            }

            // Clear old
            appointmentXAxis.getCategories().clear();
            appointmentBarChart.getData().clear();

            // Set new
            Optional<Map.Entry<String, Long>> maxEntry = counts.entrySet().stream()
                    .max(Map.Entry.comparingByValue());
            long upperBound = 5L;

            if (maxEntry.isPresent()) {
                Long maxCount = maxEntry.get().getValue();
                if (maxCount >= 5L) upperBound = maxCount + 1L;
            }

            appointmentYAxis.setAutoRanging(false);
            appointmentYAxis.setLowerBound(0);
            appointmentYAxis.setUpperBound(upperBound);
            appointmentYAxis.setTickUnit(1);

            List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();


            if (aggregation.getValue().equals("typeByMonth")) {
                appointmentBarChart.setLegendVisible(true);

                Set<String> months = counts.keySet().stream()
                        .map(key -> key.split("=")[0]).collect(Collectors.toSet());
                Set<String> types = counts.keySet().stream()
                        .map(key -> key.split("=")[1]).collect(Collectors.toSet());

                appointmentXAxis.setCategories(FXCollections.observableArrayList(months));
                Map<String, Long> finalCounts = counts;

                types.forEach(type -> {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(type);
                    finalCounts.forEach((key, value) -> {
                        String[] typeAndMonth = key.split("=");
                        if (typeAndMonth[1].equals(type)) {
                            series.getData().add(new XYChart.Data<>(typeAndMonth[0], value));
                        }
                    });
                    seriesList.add(series);
                });
            } else {
                appointmentXAxis.setCategories(FXCollections.observableArrayList(counts.keySet()));

                counts.forEach((key, value) -> {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(key);
                    series.getData().add(new XYChart.Data<>(key, value));
                    seriesList.add(series);
                });
            }

            seriesList.forEach(appointmentBarChart.getData()::add);
        }
    }

    private void populateUserWorkload() {

    }

    private void populateContactSchedule() {
        int contactId = contactComboBox.getValue().getId();
        ObservableList<Appointment> schedule = appointments
                .stream()
                .filter(app -> app.getContact().getId() == contactId)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        scheduleTable.setItems(schedule);
    }

    private void loadAppointmentsInWindow(ZonedDateTime start, ZonedDateTime end) {
        appointments.clear();
        appointments.addAll(appointmentDao.getByStartDateTimeWindow(start, end));
    }
}
