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
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.Month;
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
    private LineChart<String, Number> userLineChart;

    @FXML
    private CategoryAxis userXAxis;

    @FXML
    private NumberAxis userYAxis;

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
            Map<String, Long> counts = new LinkedHashMap<>();
            List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

            // Setup BarChart
            appointmentBarChart.setLegendVisible(false);
            appointmentXAxis.setLabel(aggregation.getLabel());
            appointmentYAxis.setLabel("Appointments");
            appointmentYAxis.setMinorTickVisible(false);
            appointmentYAxis.setMinorTickCount(0);

            // Tally up counts in a map (i.e., bag) for the aggregation category chosen
            switch ((String) aggregation.getValue()) {
                case "type":
                    seriesList = getCustomerAppointmentsByType();
                    break;
                case "month":
                    seriesList = getCustomerAppointmentsByMonth();
                    break;
                case "typeByMonth":
                    seriesList = getCustomerAppointmentsByTypeAndMonth();
                    break;
            }

            // Clear old data
            appointmentXAxis.getCategories().clear();
            appointmentBarChart.getData().clear();

            // TODO; how to get counts to set range with eveything in own method??
            // Determine upper bounds of Y axis range
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

            // Add each new series created to the BarChart
            seriesList.forEach(appointmentBarChart.getData()::add);
        }
    }

    private List<XYChart.Series<String, Number>> getCustomerAppointmentsByType() {
        // Tally up counts in a map (i.e., bag) for appointments by type
        Map<String, Long> counts = new LinkedHashMap<>();
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        counts.putAll(appointments.stream()
                .collect(Collectors.groupingBy(a -> a.getType().toUpperCase(Locale.ROOT),
                        Collectors.counting())));

        // Set categories and counts in BarChart
        appointmentXAxis.setCategories(FXCollections.observableArrayList(counts.keySet()));

        counts.forEach((key, value) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(key);
            series.getData().add(new XYChart.Data<>(key, value));
            seriesList.add(series);
        });

        return seriesList;
    }

    private List<XYChart.Series<String, Number>> getCustomerAppointmentsByMonth() {
        // Tally up counts in a map (i.e., bag) for appointments by month
        Map<String, Long> counts = new LinkedHashMap<>();
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        Map<Month, Long> unsortedCounts = appointments.stream()
                .collect(Collectors.groupingBy(a ->
                        a.getStart().getMonth(), Collectors.counting()));
        // Sort counts by month and convert month to String for display
        counts.putAll((Map<String, Long>) unsortedCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new)
                )
        );

        // Set categories and counts in BarChart
        appointmentXAxis.setCategories(FXCollections.observableArrayList(counts.keySet()));

        counts.forEach((key, value) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(key);
            series.getData().add(new XYChart.Data<>(key, value));
            seriesList.add(series);
        });

        return seriesList;
    }

    private List<XYChart.Series<String, Number>> getCustomerAppointmentsByTypeAndMonth() {
        // Tally up counts in a map (i.e., bag) for appointments by type & month
        final String CATEGORY_DELIMITER = "&&";
        Map<String, Long> counts = new LinkedHashMap<>();
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        counts.putAll(appointments.stream()
                .collect(Collectors.groupingBy(a ->
                        a.getStart().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                                CATEGORY_DELIMITER + a.getType(), Collectors.counting())));

        // Type will be defined by series in the legend and months to category groups
        appointmentBarChart.setLegendVisible(true);

        // Get a list of ordered months and one of the types
        Set<String> months = counts.keySet().stream()
                .map(key -> key.split(CATEGORY_DELIMITER)[0]).collect(Collectors.toSet())
                .stream().sorted((str1, str2) -> {
                    Month month1 = Month.valueOf(str1.toUpperCase(Locale.ROOT));
                    Month month2 = Month.valueOf(str2.toUpperCase(Locale.ROOT));
                    return month1.compareTo(month2);
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Set<String> types = counts.keySet().stream()
                .map(key -> key.split(CATEGORY_DELIMITER)[1]).collect(Collectors.toSet());

        appointmentXAxis.setCategories(FXCollections.observableArrayList(months));

        // For each type create a series
        types.forEach(type -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(type);
            //  Add the counts of that type to the series by month
            counts.forEach((key, value) -> {
                String[] monthAndType = key.split(CATEGORY_DELIMITER);
                if (monthAndType[1].equals(type)) {
                    series.getData().add(new XYChart.Data<>(monthAndType[0], value));
                }
            });
            seriesList.add(series);
        });

        return seriesList;
    }

    private void populateUserWorkload() {
        // Get a list of all Month-Years to display on LineChart

        // Setup LineChart
        userXAxis.setLabel("Time");
        userYAxis.setLabel("Appointments");

        // Transform appointments into a map of user lists
        Map<String, List<Appointment>> userAppointments = appointments.stream()
                .collect(Collectors.groupingBy(app -> app.getUser().getName()));

        // Flatten list into a sub-map (i.e, bag) of user appointment counts by month and year
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        userAppointments.forEach((userName, apps) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(userName);
            apps.stream()
                    .collect(Collectors.groupingBy(app -> app.getStart().withDayOfMonth(1).toLocalDate(),
                            Collectors.counting()))
                    .entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEach(entry ->
                            series.getData().add(new XYChart.Data<>(entry.getKey().format(formatter), entry.getValue()))
                    );

            seriesList.add(series);
        });

        // Add each new series created to the LineChart
        userLineChart.getData().clear();
        seriesList.forEach(userLineChart.getData()::add);
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
