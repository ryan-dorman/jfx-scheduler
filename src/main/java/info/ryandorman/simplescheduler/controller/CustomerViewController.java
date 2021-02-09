package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.AlertUtil;
import info.ryandorman.simplescheduler.dao.CustomerDao;
import info.ryandorman.simplescheduler.dao.CustomerDaoImpl;
import info.ryandorman.simplescheduler.model.Country;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {

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
    private ComboBox<Country> countryComboBox;

    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup ComboBoxes to display the correct class properties

        // Setup fld ComboBox to load items based on selection of Country

        // Load Countries for ComboBox

    }

    public void initData(Stage currentStage, int selectedCustomerId) {
        // Setup modal for editing
        isUpdating = true;
        header.setText("Edit Customer");

        // Get current version of customer
        CustomerDao customerDao = new CustomerDaoImpl();
        Customer customer = customerDao.getById(selectedCustomerId);

        if (customer != null) {
            // Set up form with selected customer for updating
            // populate text fields
            idTextField.setText(String.valueOf(customer.getId()));
            nameTextField.setText(customer.getName());
            phoneTextField.setText(customer.getPhone());
            addressTextField.setText(customer.getAddress());
            postalCodeTextField.setText(customer.getPostalCode());

            // set combo values based on country and fld
        } else {
            // Display warning and close
            AlertUtil.warning("Not Found", "Invalid Id", "Customer specified no longer exists.");
            currentStage.close();
        }
    }

    public void onCountrySelection() {
        // set values on division combo base on selection
    }

    @FXML
    public void onSave() {
        if (isUpdating) {
            // Update call to dao
        } else {
            // Save call to dao
        }
    }

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
}
