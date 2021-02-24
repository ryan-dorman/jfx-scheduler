package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.*;
import info.ryandorman.simplescheduler.dao.*;
import info.ryandorman.simplescheduler.model.Appointment;
import info.ryandorman.simplescheduler.model.Contact;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Handles the logic associated with a detailed display of Appointment data for creating or updating a Appointment.
 */
public class AppointmentViewController implements Initializable {
    /**
     * Appointment Data Access Object
     */
    private final AppointmentDao appointmentDao = new AppointmentDaoImpl();
    /**
     * Customer Data Access Object
     */
    private final CustomerDao customerDao = new CustomerDaoImpl();
    /**
     * Contact Data Access Object
     */
    private final ContactDao contactDao = new ContactDaoImpl();
    /**
     * User Data Access Object
     */
    private final UserDao userDao = new UserDaoImpl();
    /**
     * Current Appointment being created or updated
     */
    private Appointment currentAppointment = new Appointment();
    /**
     * Is the current interaction a create or update
     */
    private boolean isUpdating = false;

    /**
     * Label to display header of modal
     */
    @FXML
    private Label header;
    /**
     * Input field for id
     */
    @FXML
    private TextField idTextField;
    /**
     * Input field for title
     */
    @FXML
    private TextField titleTextField;
    /**
     * Input field for description
     */
    @FXML
    private TextArea descriptionTextArea;
    /**
     * Input field for location
     */
    @FXML
    private TextField locationTextField;
    /**
     * Input field for type
     */
    @FXML
    private TextField typeTextField;
    /**
     * Date picker field for start date
     */
    @FXML
    private DatePickerInput startDatePicker;
    /**
     * Spinner for start time
     */
    @FXML
    private TimeSpinner startTimeSpinner;
    /**
     * Date picker field for end date
     */
    @FXML
    private DatePickerInput endDatePicker;
    /**
     * Spinner for end time
     */
    @FXML
    private TimeSpinner endTimeSpinner;
    /**
     * Combo box for Customers
     */
    @FXML
    private ComboBox<ComboBoxOption> customerComboBox;
    /**
     * Combo box for Contacts
     */
    @FXML
    private ComboBox<ComboBoxOption> contactComboBox;
    /**
     * Combo box for Users
     */
    @FXML
    private ComboBox<ComboBoxOption> userComboBox;

    /**
     * Initializes the controller. Sets up the options necessary for the combo boxes and configure inputs for dates and
     * times.
     *
     * @param url            Location used to resolve relative paths
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCustomerComboBox();
        setupContactComboBox();
        setupUserComboBox();
        setupDatePickers();
        setupLocalTimeSpinners();
    }

    /**
     * Allows a an existing Appointment to be populated into the form for updating. If the Appointment does not exist
     * the modal will close.
     *
     * @param currentStage          Reference to the Current modal stage
     * @param selectedAppointmentId Unique identifier of the Appointment to be updated
     */
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

            customerComboBox.valueProperty().setValue(new ComboBoxOption(customer.getId(), customer.getId() +
                    " - " + customer.getName(), customer));
            contactComboBox.valueProperty().setValue(new ComboBoxOption(contact.getId(), contact.getName(), contact));
            userComboBox.valueProperty().setValue(new ComboBoxOption(user.getId(), user.getId() + " - " +
                    user.getName(), user));
        } else {
            // Display warning and close
            AlertUtil.warning("Not Found", "Invalid Id", "Appointment specified no longer exists.");
            currentStage.close();
        }
    }

    /**
     * Handles Appointment update or creation based on form input field values.
     *
     * @param actionEvent Event triggered by Save button
     */
    @FXML
    public void onSave(ActionEvent actionEvent) {
        int saved;

        // Get Appointment fields updated in form
        String title = titleTextField.getText().trim();
        String description = descriptionTextArea.getText().trim();
        String location = locationTextField.getText().trim();
        String type = typeTextField.getText().trim();
        ZonedDateTime start = startDatePicker.valueProperty().getValue()
                .atTime(startTimeSpinner.getValue().withSecond(0).withNano(0)).atZone(ZoneId.systemDefault());
        ZonedDateTime end = endDatePicker.valueProperty().getValue()
                .atTime(endTimeSpinner.getValue().withSecond(0).withNano(0)).atZone(ZoneId.systemDefault());
        Customer customer = (Customer) customerComboBox.valueProperty().getValue().getValue();
        User user = (User) userComboBox.valueProperty().getValue().getValue();
        Contact contact = (Contact) contactComboBox.valueProperty().getValue().getValue();

        try {
            int appointmentId = isUpdating ? currentAppointment.getId() : -1;
            validateAppointment(appointmentId, customer, start, end);
        } catch (DateTimeException e) {
            AlertUtil.warning("Invalid", "Invalid Appointment Window", e.getMessage());
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
            AlertUtil.warning("Failed", "Failed to Save Changes",
                    "Something went wrong. Please try to save the Appointment again.");
            return;
        }

        // Close the Modal and reload customers to view create/update
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Handles cancel of current Appointment update or create and closes the modal.
     *
     * @param actionEvent Event triggered by cancel button
     */
    @FXML
    public void onCancel(ActionEvent actionEvent) {
        // Confirm cancel before closing the associated Modal
        boolean userConfirmed = AlertUtil.confirmation("Cancel", "Cancel Changes",
                "Are you sure you want to return to the Appointments?");

        if (userConfirmed) {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }
    }

    /**
     * Sets up the Customer combo box with the current Customer options. Lambdas increase readability and ease of
     * mapping Customer records to options for the combo box.
     */
    private void setupCustomerComboBox() {
        ObservableList<ComboBoxOption> customerOptions = customerDao.getAll()
                .stream()
                .map(c -> new ComboBoxOption(c.getId(), c.getId() + " - " + c.getName(), c))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        customerComboBox.setConverter(ComboBoxOption.getComboBoxConverter(customerOptions));
        customerComboBox.setItems(customerOptions);
    }

    /**
     * Sets up the Contact combo box with the current Contact options. Lambdas increase readability and ease of
     * mapping Contact records to options for the combo box.
     */
    private void setupContactComboBox() {
        ObservableList<ComboBoxOption> contactOptions = contactDao.getAll()
                .stream()
                .map(con -> new ComboBoxOption(con.getId(), con.getName(), con))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        contactComboBox.setConverter(ComboBoxOption.getComboBoxConverter(contactOptions));
        contactComboBox.setItems(contactOptions);
    }

    /**
     * Sets up the User combo box with the current User options. Lambdas increase readability and ease of
     * mapping User records to options for the combo box.
     */
    private void setupUserComboBox() {
        ObservableList<ComboBoxOption> userOptions = userDao.getAll()
                .stream()
                .map(u -> new ComboBoxOption(u.getId(), u.getId() + " - " + u.getName(), u))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        userComboBox.setConverter(ComboBoxOption.getComboBoxConverter(userOptions));
        userComboBox.setItems(userOptions);
    }

    /**
     * Sets up the default vales for the date pickers.
     */
    private void setupDatePickers() {
        LocalDate defaultDate = LocalDate.now();
        while (CalendarUtil.isWeekend(defaultDate.getDayOfWeek())) {
            defaultDate = defaultDate.plusDays(1);
        }

        startDatePicker.setRestrictBusinessDays();
        startDatePicker.setValue(defaultDate);
        endDatePicker.setRestrictBusinessDays();
        endDatePicker.setValue(defaultDate);

        startDatePicker.valueProperty().addListener((ovVal, oldVal, newVal) -> endDatePicker.setValue(newVal));
    }


    /**
     * Sets up the default vales for the time spinners.
     */
    private void setupLocalTimeSpinners() {
        Instant now = Instant.now();
        ZonedDateTime eastern = now.atZone(ZoneId.of("America/New_York"));

        ZonedDateTime openingEastern = eastern.withHour(8).withMinute(0).withSecond(0);
        LocalTime openingTime = openingEastern.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();

        startTimeSpinner.setDefaultValue(openingTime);
        startTimeSpinner.getValueFactory().setValue(openingTime);

        endTimeSpinner.setDefaultValue(openingTime.plusMinutes(30));
        endTimeSpinner.getValueFactory().setValue(openingTime.plusMinutes(30));
    }

    /**
     * Checks that an Appointment has a valid start and end and does not double book a Customer.
     *
     * @param appointmentId Unique identifier of Appointment being updated; -1 for new Appointments
     * @param customer      Customer to participate in Appointment
     * @param start         Start date and time of Appointment
     * @param end           End date and time of Appointment
     * @throws DateTimeException The Appointment is not for a valid date and time window
     */
    private void validateAppointment(int appointmentId, Customer customer, ZonedDateTime start,
                                     ZonedDateTime end) throws DateTimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        ZonedDateTime easternStart = start.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime easternOpening = easternStart.withHour(8).withMinute(0).withSecond(0);
        ZonedDateTime easternEnd = end.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime easternClosing = easternEnd.withHour(22).withMinute(0).withSecond(0);

        boolean weekendAppointment = CalendarUtil.isWeekend(easternStart.getDayOfWeek()) ||
                CalendarUtil.isWeekend(easternEnd.getDayOfWeek());
        boolean notBusinessHours = easternStart.compareTo(easternOpening) < 0 ||
                easternEnd.compareTo(easternClosing) > 0;
        boolean conflictingAppointment = false;

        List<Appointment> conflictingCustomerAppointments = appointmentDao
                .getByCustomerIdAndDateTimeWindow(customer.getId(), start, end);

        for (Appointment app : conflictingCustomerAppointments) {
            conflictingAppointment = app.getId() != appointmentId;
            break;
        }

        String message = "";
        if (start.compareTo(end) > 0) {
            message = "Make sure your appointment is set to Start before the End.";
        } else if (weekendAppointment || notBusinessHours) {
            message = "Appointments must fall between business hours:\nMonday - Friday " +
                    easternOpening.format(formatter) + " to " + easternClosing.format(formatter) + " EST";
        } else if (conflictingAppointment) {
            message = "Customer " + customer.getId() + " - " + customer.getName() +
                    " already has at least one appointment during this time.";
        }

        if (!message.isEmpty()) throw new DateTimeException(message);
    }
}
