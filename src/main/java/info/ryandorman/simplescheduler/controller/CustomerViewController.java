package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.ComboBoxOption;
import info.ryandorman.simplescheduler.common.JavaFXUtil;
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

public class CustomerViewController implements Initializable {

    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final CountryDao countryDao = new CountryDaoImpl();
    private final FirstLevelDivisionDao divisionDao = new FirstLevelDivisionDaoImpl();

    private Customer currentCustomer = new Customer();
    private boolean isUpdating = false;

    // Modal Header
    @FXML
    private Label header;

    // Customer Fields
    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private ComboBox<ComboBoxOption> countryComboBox;

    @FXML
    private ComboBox<ComboBoxOption> divisionComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCountryComboBox();
    }

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
            JavaFXUtil.warning("Not Found", "Invalid Id", "Customer specified no longer exists.");
            currentStage.close();
        }
    }

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
            JavaFXUtil.warning("Failed", "Failed to Save Changes",
                    "Something went wrong. Please try to save the Customer again.");
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
                "Are you sure you want to return to the Customers?");

        if (userConfirmed) {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }
    }

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
