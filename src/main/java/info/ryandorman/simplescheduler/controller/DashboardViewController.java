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
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardViewController implements Initializable {
    /**
     * List of all Appointments to display data for
     */
    private final List<Appointment> appointments = new ArrayList<>();
    /**
     * Appointment Data Access Object
     */
    private final AppointmentDao appointmentDao = new AppointmentDaoImpl();
    /**
     * Contact Data Access Object
     */
    private final ContactDao contactDao = new ContactDaoImpl();

    /**
     * Date input for start of filter window
     */
    @FXML
    private DatePickerInput startDatePicker;
    /**
     * Date input for end of filter window
     */
    @FXML
    private DatePickerInput endDatePicker;
    /**
     * ComboBox of categories for Customer Appointment aggregation
     */
    @FXML
    private ComboBox<ComboBoxOption> aggregationComboBox;
    /**
     * BarChart to display Customer Appointments by selected aggregation category
     */
    @FXML
    private BarChart<String, Number> appointmentBarChart;
    /**
     * X-axis for Customer Appointments BarChart
     */
    @FXML
    private CategoryAxis appointmentXAxis;
    /**
     * Y-axis for Customer Appointments BarChart
     */
    @FXML
    private NumberAxis appointmentYAxis;
    /**
     * LineChart to display Users Workloads over time
     */
    @FXML
    private LineChart<String, Number> userLineChart;
    /**
     * X-axis for User Workload LineChart
     */
    @FXML
    private CategoryAxis userXAxis;
    /**
     * Y-axis for User Workload LineChart
     */
    @FXML
    private NumberAxis userYAxis;
    /**
     * ComboBox of Contacts for Contact schedule selection
     */
    @FXML
    private ComboBox<ComboBoxOption> contactComboBox;
    /**
     * Table to display selected Contacts Schedule
     */
    @FXML
    private TableView<Appointment> scheduleTable;
    /**
     * Unique Identifier column for scheduled Appointment
     */
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    /**
     * Start date and time column for scheduled Appointment
     */
    @FXML
    private TableColumn<Appointment, String> startColumn;
    /**
     * End date and time column for scheduled Appointment
     */
    @FXML
    private TableColumn<Appointment, String> endColumn;
    /**
     * Title column for scheduled Appointment
     */
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    /**
     * Type column for scheduled Appointment
     */
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    /**
     * Description column for scheduled Appointment
     */
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    /**
     * Customer unique identifier column for scheduled Appointment
     */
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    /**
     * Initializes the controller. Configures the charts and table displaying the reports and load the initial data to
     * report on.
     *
     * @param url            Location used to resolve relative paths
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupDatePickers();
        setupAggregationComboBox();
        setupContactComboBox();
        setupScheduleTableView();
        onDateFilter();
    }

    /**
     * Handles new date filter input and updates view.
     */
    @FXML
    public void onDateFilter() {
        ZonedDateTime startDate = startDatePicker.valueProperty().getValue().atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endDate = endDatePicker.valueProperty().getValue().atStartOfDay(ZoneId.systemDefault());

        if (startDate.compareTo(endDate) < 0) {
            loadAppointmentsInWindow(startDate, endDate);
            populateCustomerAppointments();
            populateUserWorkload(startDate, endDate);
            populateContactSchedule();
        } else {
            AlertUtil.warning("Date Input", "Invalid Filter Range",
                    "Make sure your start date occurs at least one day before your end date.");
        }
    }

    /**
     * Sets date filter inputs to default to start and end of current year.
     */
    private void setupDatePickers() {
        startDatePicker.setValue(CalendarUtil.getFirstDayOfYear().toLocalDate());
        endDatePicker.setValue(CalendarUtil.getLastDayOfYear().toLocalDate());
    }

    /**
     * Sets Customer Appointments aggregation combo box to display options: Type. Month, Type by Month. Updates the
     * Customer Appointments BarChart on selection. A Lambda is used to increase readability and simplify access to the
     * <code>javafx.beans.value.ChangeListener</code> interface.
     */
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

    /**
     * Sets Contact Schedule combo box to display Contacts as options. Updates the Contact schedule currently displayed
     * in the view on selection. A Lambda is used to increase readability and simplify access to the
     * <code>javafx.beans.value.ChangeListener</code> interface.
     */
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

    /**
     * Sets up the Contact Schedule table so it can display the appropriate Appointment data.
     */
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

    /**
     * Reads the Customer Appointment aggregation category and populates the BarChart with the correct data.
     */
    private void populateCustomerAppointments() {
        ComboBoxOption aggregation = aggregationComboBox.getValue();

        if (aggregation != null) {
            List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

            // Clear old data
            appointmentXAxis.getCategories().clear();
            appointmentBarChart.getData().clear();

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

            appointmentYAxis.setAutoRanging(false);
            appointmentYAxis.setLowerBound(0);
            appointmentYAxis.setUpperBound(getChartUpperBounds(seriesList));
            appointmentYAxis.setTickUnit(1);

            // Add each new series created to the BarChart
            seriesList.forEach(appointmentBarChart.getData()::add);
        }
    }

    /**
     * Transforms the Appointment data into a map of aggregated counts for each Appointment type (e.g., {
     * typeOne=2, typeTwo=5, ... }) and sets the counts into BarChart series. Streams and lambdas are used to increase
     * ease and readability of data transformations.
     *
     * @return List of data series containing the Appointment type counts to set in the BarChart
     */
    private List<XYChart.Series<String, Number>> getCustomerAppointmentsByType() {
        // Tally up counts in a map for appointments by type
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        Map<String, Long> counts = new LinkedHashMap<>(appointments.stream()
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

    /**
     * Transforms the Appointment data into a map of aggregated counts for each Appointment month (e.g., {
     * JAN=2, FEB=5, ... }) and sets the counts into BarChart series. Streams and lambdas are used to increase
     * ease and readability of data transformations.
     *
     * @return List of data series containing the Appointment month counts to set in the BarChart
     */
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

    /**
     * Transforms the Appointment data into a map of aggregated counts for each Appointment month and type combination
     * (e.g., { JAN&&typeOne=3, JAN&&typeTwo=1, FEB&&TypeOne=5, ... }) and sets the counts into BarChart series for each
     * type by month. Streams and lambdas are used to increase ease and readability of data transformations.
     *
     * @return List of data series containing the Appointment month counts to set in the BarChart
     */
    private List<XYChart.Series<String, Number>> getCustomerAppointmentsByTypeAndMonth() {
        // Tally up counts in a map (i.e., bag) for appointments by type & month
        final String CATEGORY_DELIMITER = "&&";
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        Map<String, Long> counts = new LinkedHashMap<>(appointments.stream()
                .collect(Collectors.groupingBy(a ->
                        a.getStart().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                                CATEGORY_DELIMITER + a.getType(), Collectors.counting())));

        // Type will be defined by series in the legend and months to category groups
        appointmentBarChart.setLegendVisible(true);

        // Get a list of ordered months and one of the types
        Set<String> months = counts.keySet().stream()
                .map(key -> key.split(CATEGORY_DELIMITER)[0])
                .sorted((str1, str2) -> {
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

    /**
     * Transforms the Appointment data into a map of aggregated counts for each user Appointment per month year combo
     * (e.g., { userOne={ 01-2020=1, 02-2021=4, ... }, userTwo={ 02-2021=1 }, ... }). The counts are then displayed in
     * the User Workload LineChart. Streams and lambdas are used to increase ease and readability of data transformations.
     * @param startDate
     * @param endDate
     */
    private void populateUserWorkload(ZonedDateTime startDate, ZonedDateTime endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M-yy");
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        // Build a list of all Month-Years to display on LineChart
        LocalDate minDate = startDate.withDayOfMonth(1).toLocalDate();
        LocalDate maxDate = endDate.withDayOfMonth(1).toLocalDate();
        Set<String> monthYears = new LinkedHashSet<>();

        do {
            monthYears.add(minDate.format(formatter));
            minDate = minDate.plusMonths(1);
        } while (minDate.isBefore(maxDate));

        // Reset Chart
        userLineChart.getData().clear();
        userXAxis.getCategories().clear();

        // Setup LineChart
        userXAxis.setLabel("Time");
        userYAxis.setLabel("Appointments");
        userXAxis.setCategories(FXCollections.observableArrayList(monthYears));

        // Transform appointments into a map of user lists
        Map<String, List<Appointment>> userAppointments = appointments.stream()
                .collect(Collectors.groupingBy(app -> app.getUser().getName()));

        // Flatten list into a sub-map (i.e, bag) of user appointment counts by month and year
        userAppointments.forEach((userName, apps) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(userName);
            Map<String,Long> workloadCounts = apps.stream()
                    .collect(Collectors.groupingBy(app -> app.getStart().format(formatter), Collectors.counting()));

            // For each month-year set user counts or 0 if none
            monthYears.forEach(monthYear -> {
                Long userCount = workloadCounts.get(monthYear);
                series.getData().add(new XYChart.Data<>(monthYear, userCount != null ? userCount : 0));
            });

            seriesList.add(series);
        });

        userYAxis.setAutoRanging(false);
        userYAxis.setLowerBound(0);
        userYAxis.setUpperBound(getChartUpperBounds(seriesList));
        userYAxis.setTickUnit(1);

        // Add each new series created to the LineChart
        seriesList.forEach(userLineChart.getData()::add);
    }

    private long getChartUpperBounds(List<XYChart.Series<String, Number>> seriesList) {
        // Determine upper bounds of Y axis range

        long upperBound = 5L;
        // Get max for each series list then find the max of that
        long maxCount = seriesList.stream()
                // Map each series to the entry with the max Y value
                .map(series -> series.getData().stream()
                        .max(Comparator.comparing(XYChart.Data<String, Number>::getYValue,
                                Comparator.comparingLong(Number::longValue))))
                // Get the Max Y value of all series
                .max(Comparator.comparing(data -> data.isPresent() ? data.get().getYValue() : 0L,
                        Comparator.comparingLong(Number::longValue)))
                // The the Y value as long
                .flatMap(max -> max.map(entry -> entry.getYValue().longValue()))
                .orElse(0L);

        if (maxCount >= 5L) upperBound = maxCount + 1L;

        return upperBound;
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
