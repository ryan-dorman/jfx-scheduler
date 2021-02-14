package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.ComboBoxOption;
import info.ryandorman.simplescheduler.common.JavaFXUtil;
import info.ryandorman.simplescheduler.dao.*;
import info.ryandorman.simplescheduler.model.Contact;
import info.ryandorman.simplescheduler.model.Country;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppointmentViewController implements Initializable {

    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final ContactDao contactDao = new ContactDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

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
        // Load customers, contacts, users to be comboBox options
        ObservableList<ComboBoxOption> customerOptions = customerDao.getAll()
                .stream()
                .map(c -> new ComboBoxOption(c.getId(), c.getId() + " - " + c.getName(), c))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        ObservableList<ComboBoxOption> contactOptions = contactDao.getAll()
                .stream()
                .map(con -> new ComboBoxOption(con.getId(), con.getName(), con))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        ObservableList<ComboBoxOption> userOptions = userDao.getAll()
                .stream()
                .map(u -> new ComboBoxOption(u.getId(), u.getId() + " - " + u.getName(), u))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Configure how ComboBox formats the options
        customerComboBox.setConverter(JavaFXUtil.getComboBoxConverter(customerOptions));
        contactComboBox.setConverter(JavaFXUtil.getComboBoxConverter(contactOptions));
        userComboBox.setConverter(JavaFXUtil.getComboBoxConverter(userOptions));

        customerComboBox.setItems(customerOptions);
        contactComboBox.setItems(contactOptions);
        userComboBox.setItems(userOptions);

        // Setup Spinners to handle time input
//        https://www.reddit.com/r/javahelp/comments/79cocp/javafx_time_spinner_hhmm_24_hour_format/

    }
}
