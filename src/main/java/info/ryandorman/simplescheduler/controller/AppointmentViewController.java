package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.model.Contact;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AppointmentViewController implements Initializable {

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
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private ComboBox<User> userComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load customers, contacts, users

        // Setup comboBoxes

        // Setup Spinners to handle time input

    }
}
