package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.AlertUtil;
import info.ryandorman.simplescheduler.common.CalendarUtil;
import info.ryandorman.simplescheduler.dao.AppointmentDao;
import info.ryandorman.simplescheduler.dao.AppointmentDaoImpl;
import info.ryandorman.simplescheduler.model.Appointment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Handles the logic associated with display a table of all Appointments and provides options to filter, create, update
 * or delete Appointments.
 */
public class AppointmentsViewController implements Initializable {
    /**
     * Appointment Data Access Object
     */
    private final AppointmentDao appointmentDao = new AppointmentDaoImpl();

    /**
     * Table for Appointments data
     */
    @FXML
    private TableView<Appointment> appointmentsTable;
    /**
     * Unique Identifier column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    /**
     * Title column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    /**
     * Description column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    /**
     * Location column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    /**
     * Contact column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    /**
     * Type column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    /**
     * Start date and time column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, String> startColumn;
    /**
     * End date and time column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, String> endColumn;
    /**
     * Customer identifier column for Appointments table
     */
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;
    /**
     * Button to clear and filters
     */
    @FXML
    private RadioButton allRadioButton;
    /**
     * Button to filter by Appointments this week
     */
    @FXML
    private RadioButton thisWeekRadioButton;
    /**
     * Button to filter by Appointments this month
     */
    @FXML
    private RadioButton thisMonthRadioButton;

    /**
     * Initializes the controller. Sets up the Appointments table, populates the data, and sets up the radio buttons for
     * filtering.
     *
     * @param url            Location used to resolve relative paths
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAppointmentsTableView();
        setupFilterRadioButtons();
    }

    /**
     * Handles the creation of the Appointment modal window to create a new Appointment.
     *
     * @param actionEvent Event trigger by Create button
     * @throws IOException The Appointment modal fails to open
     */
    @FXML
    public void onCreate(ActionEvent actionEvent) throws IOException {
        loadAppointmentView(actionEvent, "Create Appointment", -1);
    }

    /**
     * Handles the creation of the Appointment modal window to update a existing Appointment.
     *
     * @param actionEvent Event trigger by Update button
     * @throws IOException The Appointment modal fails to open
     */
    @FXML
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            loadAppointmentView(actionEvent, "Update Appointment", selectedAppointment.getId());
        }
    }

    /**
     * Handles the deletion of existing Appointments.
     */
    @FXML
    public void onDelete() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            boolean userConfirmed = AlertUtil.confirmation("Delete", selectedAppointment.getId()
                    + " - " + selectedAppointment.getType(), "Are you sure you want to delete this Appointment?");

            if (userConfirmed) {
                int deleted;
                deleted = appointmentDao.delete(selectedAppointment.getId());

                if (deleted == 0) {
                    AlertUtil.warning("Failed", "Failed to Delete",
                            "Something went wrong. Please try to delete the Appointment again.");
                } else {
                    AlertUtil.inform("Success", "Delete Successful",
                            "Appointment " + selectedAppointment.getId() + " - " + selectedAppointment.getType() +
                                    " has been deleted.");
                    loadAppointments();
                }
            }
        }
    }

    /**
     * Loads Appointment data and replaces all existing data in the table with it.
     */
    private void loadAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentDao.getAll());
        appointmentsTable.setItems(appointments);
    }

    /**
     * Loads Appointment data with the specified date filter and replaces all existing data in the table with it.
     * @param start Date and time to start filter window
     * @param end Date and time to end filter window
     */
    private void filterAppointments(ZonedDateTime start, ZonedDateTime end) {
        List<Appointment> filteredAppointments = appointmentDao.getByStartDateTimeWindow(start, end);
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(filteredAppointments);
        appointmentsTable.setItems(appointments);
    }

    /**
     * Sets up the Appointment table so it can display the appropriate Appointment data.
     */
    private void setupAppointmentsTableView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy h:mm a");

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
    }

    /**
     * Sets up the radio buttons for filtering the Appointment data by date.
     */
    private void setupFilterRadioButtons() {
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

    /**
     * Load the Appointment modal and set it up for either creating or updating a Appointment.
     *
     * @param actionEvent         Event triggered by Create or Update buttons
     * @param title               Title of the modal
     * @param selectAppointmentId Valid identifier for the Appointment to be updated; If not valid modal will be for
     *                            creation.
     * @throws IOException The <Code>javafx.fxml.FXMLLoader</Code> cannot load <code>AppointmentView.fxml</code>.
     */
    private void loadAppointmentView(ActionEvent actionEvent, String title, int selectAppointmentId) throws IOException {
        Stage customerStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AppointmentView.fxml"));
        Parent parent = loader.load();
        Stage stage = new Stage();
        AppointmentViewController controller = loader.getController();

        // A valid customer id indicates a record is being updated
        if (selectAppointmentId > 0) {
            controller.initData(stage, selectAppointmentId);
        }

        // Init View
        stage.setTitle(title);
        stage.setScene(new Scene(parent, 800, 500));
        stage.initOwner(customerStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(we -> loadAppointments());
        stage.showAndWait();
    }
}
