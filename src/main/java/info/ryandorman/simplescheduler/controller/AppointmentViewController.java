package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.ComboBoxOption;
import info.ryandorman.simplescheduler.common.JavaFXUtil;
import info.ryandorman.simplescheduler.dao.*;
import info.ryandorman.simplescheduler.model.Appointment;
import info.ryandorman.simplescheduler.model.Contact;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppointmentViewController implements Initializable {

    private final AppointmentDao appointmentDao = new AppointmentDaoImpl();
    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final ContactDao contactDao = new ContactDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    private Appointment currentAppointment = new Appointment();
    private boolean isUpdating = false;

    // Modal Header
    @FXML
    private Label header;

    // Appointment Fields
    @FXML
    private TextField idTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Spinner<LocalTime> startTimeSpinner;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Spinner<LocalTime> endTimeSpinner;

    @FXML
    private ComboBox<ComboBoxOption> customerComboBox;

    @FXML
    private ComboBox<ComboBoxOption> contactComboBox;

    @FXML
    private ComboBox<ComboBoxOption> userComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCustomerComboBox();
        setupContactComboBox();
        setupUserComboBox();
        setupLocalTimeSpinners();
    }

    public void initData(Stage currentStage, int selectedAppointmentId) {
        // Setup modal for editing
        isUpdating = true;
        header.setText("Update Appointment");

        // Get latest version of customer
        currentAppointment = appointmentDao.getById(selectedAppointmentId);

        if (currentAppointment != null) {
            // Set up form with selected appointment for updating
            // populate text fields
            idTextField.setText(String.valueOf(currentAppointment.getId()));
            titleTextField.setText(currentAppointment.getTitle());
            descriptionTextArea.setText(currentAppointment.getDescription());
            locationTextField.setText(currentAppointment.getLocation());
            typeTextField.setText(currentAppointment.getType());

            // Set up date and time fields
            startDatePicker.setValue(currentAppointment.getStart().toLocalDate());
            startTimeSpinner.getValueFactory().setValue(currentAppointment.getStart().toLocalTime());
            endDatePicker.setValue(currentAppointment.getEnd().toLocalDate());
            endTimeSpinner.getValueFactory().setValue(currentAppointment.getEnd().toLocalTime());

            // Set ComboBoxes
            Customer customer = currentAppointment.getCustomer();
            Contact contact = currentAppointment.getContact();
            User user = currentAppointment.getUser();

            customerComboBox.valueProperty().setValue(new ComboBoxOption(customer.getId(), customer.getName(), customer));
            contactComboBox.valueProperty().setValue(new ComboBoxOption(contact.getId(), contact.getName(), contact));
            userComboBox.valueProperty().setValue(new ComboBoxOption(user.getId(), user.getName(), user));
        } else {
            // Display warning and close
            JavaFXUtil.warning("Not Found", "Invalid Id", "Appointment specified no longer exists.");
            currentStage.close();
        }
    }

    @FXML
    public void onSave() {
    }

    @FXML
    public void onCancel() {
    }

    private void setupCustomerComboBox() {
        ObservableList<ComboBoxOption> customerOptions = customerDao.getAll()
                .stream()
                .map(c -> new ComboBoxOption(c.getId(), c.getId() + " - " + c.getName(), c))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        customerComboBox.setConverter(JavaFXUtil.getComboBoxConverter(customerOptions));
        customerComboBox.setItems(customerOptions);
    }

    private void setupContactComboBox() {
        ObservableList<ComboBoxOption> contactOptions = contactDao.getAll()
                .stream()
                .map(con -> new ComboBoxOption(con.getId(), con.getName(), con))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        contactComboBox.setConverter(JavaFXUtil.getComboBoxConverter(contactOptions));
        contactComboBox.setItems(contactOptions);
    }

    private void setupUserComboBox() {
        ObservableList<ComboBoxOption> userOptions = userDao.getAll()
                .stream()
                .map(u -> new ComboBoxOption(u.getId(), u.getId() + " - " + u.getName(), u))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        userComboBox.setConverter(JavaFXUtil.getComboBoxConverter(userOptions));
        userComboBox.setItems(userOptions);
    }

    private void setupLocalTimeSpinners() {
        //        https://www.reddit.com/r/javahelp/comments/79cocp/javafx_time_spinner_hhmm_24_hour_format/
        //        https://howtodoinjava.com/java/date-time/localdatetime-to-zoneddatetime/
        String format = "h:mm a";
        Instant now = Instant.now();
        ZonedDateTime eastern = now.atZone(ZoneId.of("America/New_York"));

        ZonedDateTime openingEastern = eastern.withHour(8).withMinute(0).withSecond(0); // 7:00 am
        LocalTime opening = openingEastern.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();


        ZonedDateTime closingEaster = eastern.withHour(22).withMinute(0).withSecond(0); // 9:00 pm
        LocalTime closing = closingEaster.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();

        startTimeSpinner.setValueFactory(JavaFXUtil.getSpinnerLocalTimeFactory(startTimeSpinner, format, opening, closing));
        startTimeSpinner.getEditor().setTextFormatter(JavaFXUtil.getLocalTimeFormatter(format, opening));
        startTimeSpinner.getValueFactory().setValue(opening);

        endTimeSpinner.setValueFactory(JavaFXUtil.getSpinnerLocalTimeFactory(endTimeSpinner, format, opening, closing));
        endTimeSpinner.getEditor().setTextFormatter(JavaFXUtil.getLocalTimeFormatter(format, opening.plusMinutes(45)));
        endTimeSpinner.getValueFactory().setValue(opening.plusMinutes(30));
    }
}
