package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.AlertUtil;
import info.ryandorman.simplescheduler.common.ComboBoxOption;
import info.ryandorman.simplescheduler.dao.*;
import info.ryandorman.simplescheduler.model.Country;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Handles the logic associated with a detailed display of Customer data for creating or updating a Customer.
 */
public class CustomerViewController implements Initializable {
    /**
     * Customer Data Access Object
     */
    private final CustomerDao customerDao = new CustomerDaoImpl();
    /**
     * Country Data Access Object
     */
    private final CountryDao countryDao = new CountryDaoImpl();
    /**
     * First Level Division Data Access Object
     */
    private final FirstLevelDivisionDao divisionDao = new FirstLevelDivisionDaoImpl();
    /**
     * Current customer being created or updated
     */
    private Customer currentCustomer = new Customer();
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
     * Input field for name
     */
    @FXML
    private TextField nameTextField;
    /**
     * Input field for phone number
     */
    @FXML
    private TextField phoneTextField;
    /**
     * Input field for address
     */
    @FXML
    private TextField addressTextField;
    /**
     * Input field for postal code
     */
    @FXML
    private TextField postalCodeTextField;
    /**
     * Combo box for Country
     */
    @FXML
    private ComboBox<ComboBoxOption> countryComboBox;
    /**
     * Combo box for First Level Division
     */
    @FXML
    private ComboBox<ComboBoxOption> divisionComboBox;

    /**
     * Initializes the controller. Sets up the options necessary for the combo boxes.
     *
     * @param url            Location used to resolve relative paths
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCountryComboBox();
    }

    /**
     * Allows a an existing Customer to be populated into the form for updating. If the Customer does not exist
     * the modal will close.
     *
     * @param currentStage       Reference to the Current modal stage
     * @param selectedCustomerId Unique identifier of the Customer to be updated
     */
    public void initData(Stage currentStage, int selectedCustomerId) {
        // Setup modal for editing
        isUpdating = true;
        header.setText("Update Customer");

        // Get latest version of customer
        currentCustomer = customerDao.getById(selectedCustomerId);

        if (currentCustomer != null) {
            // Set up form with selected customer for updating
            // populate text fields
            idTextField.setText(String.valueOf(currentCustomer.getId()));
            nameTextField.setText(currentCustomer.getName());
            phoneTextField.setText(currentCustomer.getPhone());
            addressTextField.setText(currentCustomer.getAddress());
            postalCodeTextField.setText(currentCustomer.getPostalCode());

            // set combo values based on country and fld
            Country country = currentCustomer.getDivision().getCountry();
            FirstLevelDivision fld = currentCustomer.getDivision();
            countryComboBox.valueProperty().setValue(new ComboBoxOption(country.getId(), country.getName(), country));
            divisionComboBox.valueProperty().setValue(new ComboBoxOption(fld.getId(), fld.getName(), fld));
        } else {
            // Display warning and close
            AlertUtil.warning("Not Found", "Invalid Id", "Customer specified no longer exists.");
            currentStage.close();
        }
    }

    /**
     * Handles Customer update or creation based on form input field values.
     *
     * @param actionEvent Event triggered by Save button
     */
    @FXML
    public void onSave(ActionEvent actionEvent) {
        int saved;

        // Get customer fields updated in form
        String name = nameTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        String address = addressTextField.getText().trim();
        String postalCode = postalCodeTextField.getText().trim();
        FirstLevelDivision division = (FirstLevelDivision) divisionComboBox.valueProperty().getValue().getValue();

        // Update customer object
        currentCustomer.setName(name);
        currentCustomer.setPhone(phone);
        currentCustomer.setAddress(address);
        currentCustomer.setPostalCode(postalCode);
        currentCustomer.setDivision(division);
        currentCustomer.setUpdatedBy(MainViewController.currentUser.getName());

        if (isUpdating) {
            saved = customerDao.update(currentCustomer);
        } else {
            currentCustomer.setCreatedBy(MainViewController.currentUser.getName());
            saved = customerDao.create(currentCustomer);
        }

        if (saved == 0) {
            AlertUtil.warning("Failed", "Failed to Save Changes",
                    "Something went wrong. Please try to save the Customer again.");
            return;
        }

        // Close the Modal and reload customers to view create/update
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Handles cancel of current Customer update or create and closes the modal.
     *
     * @param actionEvent Event triggered by cancel button
     */
    @FXML
    public void onCancel(ActionEvent actionEvent) {
        // Confirm cancel before closing the associated Modal
        boolean userConfirmed = AlertUtil.confirmation("Cancel", "Cancel Changes",
                "Are you sure you want to return to the Customers?");

        if (userConfirmed) {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }
    }

    /**
     * Sets up the Country combo box with the current Country options. Lambdas increase readability and ease of
     * mapping Country records to options for the combo box.
     */
    private void setupCountryComboBox() {
        // Load Countries for ComboBox
        ObservableList<ComboBoxOption> countryOptions = countryDao.getAll()
                .stream()
                .map(co -> new ComboBoxOption(co.getId(), co.getName(), co))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Configure how ComboBox formats the options update divisions on selection
        countryComboBox.setConverter(ComboBoxOption.getComboBoxConverter(countryOptions));
        countryComboBox.valueProperty().addListener((obs, oldVale, newValue) -> {
            if (newValue != null) {
                setupDivisionComboBox(newValue.getId());
            }
        });
        countryComboBox.setItems(countryOptions);
    }

    /**
     * Sets up the First Level Division combo box for the specified Country. Lambdas increase readability and ease of
     * mapping First Level Division records to options for the combo box.
     *
     * @param countryId Unique identifier for the Country to filter divisions by
     */
    private void setupDivisionComboBox(int countryId) {
        ObservableList<ComboBoxOption> divisionOptions = divisionDao.getByCountryId(countryId)
                .stream()
                .map(fld -> new ComboBoxOption(fld.getId(), fld.getName(), fld))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        divisionComboBox.setConverter(ComboBoxOption.getComboBoxConverter(divisionOptions));
        divisionComboBox.setItems(divisionOptions);
        divisionComboBox.setValue(null);
    }

}
