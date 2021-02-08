package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.JavaFxUtil;
import info.ryandorman.simplescheduler.model.Country;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;
import javafx.collections.ObservableList;
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

    // State
    Customer selectedCustomer;
    ObservableList<Country> countries;
    ObservableList<FirstLevelDivision> divisions;

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
        // Load Countries for ComboBox
    }

    public void initData(Customer selectedCustomers) {
        this.selectedCustomer = selectedCustomer;

        // Determine if the user is Creating or Updating
        if (this.selectedCustomer != null) {
            // Setup modal for editing
            header.setText("Edit Customer");

            // Set up form with selected customer for updating
            // populate text fields

            // set combo values based on country and fld
        }
    }

    public void onCountrySelection() {
        // set values on division combo base on selection
    }

    @FXML
    public void onSave() {}

    @FXML
    public void onCancel(ActionEvent actionEvent) {
        // Confirm cancel before closing the associated Modal
        boolean userConfirmed = JavaFxUtil.confirmation("Cancel", "Cancel Changes",
                "Are you sure you want to return to the Customers?");

        if (userConfirmed) {
            Stage partStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            partStage.close();
        }
    }
}
