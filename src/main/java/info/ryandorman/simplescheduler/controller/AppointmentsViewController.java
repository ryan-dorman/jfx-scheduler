package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.CalendarUtil;
import info.ryandorman.simplescheduler.common.JavaFXUtil;
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
    private RadioButton thisWeekRadioButton;

    @FXML
    private RadioButton thisMonthRadioButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAppointmentsTableView();
        setupFilterRadioButtons();
    }

    @FXML
    public void onCreate(ActionEvent actionEvent) throws IOException {
        loadAppointmentView(actionEvent, "Create Appointment", -1);
    }

    @FXML
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            loadAppointmentView(actionEvent, "Update Appointment", selectedAppointment.getId());
        }
    }

    @FXML
    public void onDelete() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            boolean userConfirmed = JavaFXUtil.confirmation("Delete", selectedAppointment.getId()
                    + " - " + selectedAppointment.getType(), "Are you sure you want to delete this Appointment?");

            if (userConfirmed) {
                int deleted;
                deleted = appointmentDao.delete(selectedAppointment.getId());

                if (deleted == 0) {
                    JavaFXUtil.warning("Failed", "Failed to Delete",
                            "Something went wrong. Please try to delete the Appointment again.");
                } else {
                    JavaFXUtil.inform("Success", "Delete Successful",
                            "Appointment " + selectedAppointment.getId() + " - " + selectedAppointment.getType() +
                                    " has been deleted.");
                    loadAppointments();
                }
            }
        }
    }

    private void loadAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentDao.getAll());
        appointmentsTable.setItems(appointments);
    }

    private void filterAppointments(ZonedDateTime start, ZonedDateTime end) {
        List<Appointment> filteredAppointments = appointmentDao.getByDateTimeWindow(start, end);
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(filteredAppointments);
        appointmentsTable.setItems(appointments);
    }

    private void setupAppointmentsTableView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy h:mm:ss a");

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
    }

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
