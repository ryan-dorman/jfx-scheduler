package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.CalendarUtil;
import info.ryandorman.simplescheduler.common.ComboBoxOption;
import info.ryandorman.simplescheduler.common.JavaFXUtil;
import info.ryandorman.simplescheduler.dao.*;
import info.ryandorman.simplescheduler.model.Appointment;
import info.ryandorman.simplescheduler.model.Contact;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
        setupDatePickers();
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
    public void onSave(ActionEvent actionEvent) {
        int saved;

        // Get Appointment fields updated in form
        String title = titleTextField.getText().trim();
        String description = descriptionTextArea.getText().trim();
        String location = locationTextField.getText().trim();
        String type = typeTextField.getText().trim();
        ZonedDateTime start = startDatePicker.getValue().atTime(startTimeSpinner.getValue().withSecond(0))
                .atZone(ZoneId.systemDefault());
        ZonedDateTime end = endDatePicker.getValue().atTime(endTimeSpinner.getValue().withSecond(0))
                .atZone(ZoneId.systemDefault());
        Customer customer = (Customer) customerComboBox.valueProperty().getValue().getValue();
        User user = (User) userComboBox.valueProperty().getValue().getValue();
        Contact contact = (Contact) contactComboBox.valueProperty().getValue().getValue();

        try {
            validateAppointment(customer, start, end);
        } catch (DateTimeException e) {
            JavaFXUtil.warning("Invalid", "Invalid Appointment Window", e.getMessage());
            return;
        }

        // Update Appointment object
        currentAppointment.setTitle(title);
        currentAppointment.setDescription(description);
        currentAppointment.setLocation(location);
        currentAppointment.setType(type);
        currentAppointment.setStart(start);
        currentAppointment.setEnd(end);
        currentAppointment.setCustomer(customer);
        currentAppointment.setUser(user);
        currentAppointment.setContact(contact);
        currentAppointment.setUpdatedBy(MainViewController.currentUser.getName());

        if (isUpdating) {
            saved = appointmentDao.update(currentAppointment);
        } else {
            currentAppointment.setCreatedBy(MainViewController.currentUser.getName());
            saved = appointmentDao.create(currentAppointment);
        }

        if (saved == 0) {
            JavaFXUtil.warning("Failed", "Failed to Save Changes",
                    "Something went wrong. Please try to save the Appointment again.");
            return;
        }

        // Close the Modal and reload customers to view create/update
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    public void onCancel(ActionEvent actionEvent) {
        // Confirm cancel before closing the associated Modal
        boolean userConfirmed = JavaFXUtil.confirmation("Cancel", "Cancel Changes",
                "Are you sure you want to return to the Appointments?");

        if (userConfirmed) {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }
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

    private void setupDatePickers() {
        LocalDate defaultDate = LocalDate.now();
        while (CalendarUtil.isWeekend(defaultDate.getDayOfWeek())) {
            defaultDate.plusDays(1);
        }

        startDatePicker.setDayCellFactory(picker -> JavaFXUtil.getDisabledPastAndWeekendDateCell());
        startDatePicker.setValue(defaultDate);
        endDatePicker.setDayCellFactory(picker -> JavaFXUtil.getDisabledPastAndWeekendDateCell());
        endDatePicker.setValue(defaultDate);
    }

    private void setupLocalTimeSpinners() {
        String format = "h:mm a";
        Instant now = Instant.now();
        ZonedDateTime eastern = now.atZone(ZoneId.of("America/New_York"));

        ZonedDateTime openingEastern = eastern.withHour(8).withMinute(0).withSecond(0);
        LocalTime openingTime = openingEastern.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();

        startTimeSpinner.setValueFactory(JavaFXUtil.getSpinnerLocalTimeFactory(startTimeSpinner, format));
        startTimeSpinner.getEditor().addEventHandler(MouseEvent.MOUSE_CLICKED,
                JavaFXUtil.getTimeSpinnerSelectionRules(startTimeSpinner));
        startTimeSpinner.getEditor().setTextFormatter(JavaFXUtil.getLocalTimeFormatter(format, openingTime));
        startTimeSpinner.getValueFactory().setValue(openingTime);

        endTimeSpinner.setValueFactory(JavaFXUtil.getSpinnerLocalTimeFactory(endTimeSpinner, format));
        endTimeSpinner.getEditor().addEventHandler(MouseEvent.MOUSE_CLICKED,
                JavaFXUtil.getTimeSpinnerSelectionRules(endTimeSpinner));
        endTimeSpinner.getEditor().setTextFormatter(JavaFXUtil.getLocalTimeFormatter(format, openingTime.plusMinutes(30)));
        endTimeSpinner.getValueFactory().setValue(openingTime.plusMinutes(30));
    }

    private void validateAppointment(Customer customer, ZonedDateTime start, ZonedDateTime end) throws DateTimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd h:mm a");

        ZonedDateTime easternStart = start.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime easternOpening = easternStart.withHour(8).withMinute(0).withSecond(0);
        ZonedDateTime easternEnd = end.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime easternClosing = easternEnd.withHour(22).withMinute(0).withSecond(0);

        boolean weekendAppointment = CalendarUtil.isWeekend(easternStart.getDayOfWeek()) ||
                CalendarUtil.isWeekend(easternEnd.getDayOfWeek());
        boolean notBusinessHours = easternStart.compareTo(easternOpening) < 0 ||
                easternEnd.compareTo(easternClosing) > 0;

        String message = "";
        if (start.compareTo(end) > 0) {
            message = "Make sure your appointment is set to Start before the End.";
        } else if (weekendAppointment || notBusinessHours) {
            message = "Appointments must fall between business hours:\nMonday - Friday " + easternOpening.format(formatter) + " to " +
                    easternClosing.format(formatter) + " EST";
        } else if (appointmentDao.getByCustomerIdAndDateTimeWindow(customer.getId(), start, end).size() > 0) {
            message = "Customer " + customer.getId() + " - " + customer.getName() +
                    " already has at least one appointment during this time.";
        }

        if (!message.isEmpty()) throw new DateTimeException(message);
    }
}
