package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.AlertUtil;
import info.ryandorman.simplescheduler.dao.AppointmentDao;
import info.ryandorman.simplescheduler.dao.AppointmentDaoImpl;
import info.ryandorman.simplescheduler.dao.CustomerDao;
import info.ryandorman.simplescheduler.dao.CustomerDaoImpl;
import info.ryandorman.simplescheduler.model.Customer;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handles the logic associated with display a table of all Customers and provides options to search for, create, update
 * or delete Customers.
 */
public class CustomersViewController implements Initializable {
    /**
     * Appointment Data Access Object
     */
    private final AppointmentDao appointmentDao = new AppointmentDaoImpl();
    /**
     * Customer Data Access Object
     */
    private final CustomerDao customerDao = new CustomerDaoImpl();

    /**
     * Table for Customers data
     */
    @FXML
    private TableView<Customer> customersTable;
    /**
     * Unique Identifier column for Customers table
     */
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    /**
     * Name column for Customers table
     */
    @FXML
    private TableColumn<Customer, String> nameColumn;
    /**
     * Phone number column for Customers table
     */
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    /**
     * Address column for Customers table
     */
    @FXML
    private TableColumn<Customer, String> addressColumn;
    /**
     * Postal code column for Customers table
     */
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    /**
     * First Level Division column for Customers table
     */
    @FXML
    private TableColumn<Customer, String> divisionColumn;
    /**
     * Country column for Customers table
     */
    @FXML
    private TableColumn<Customer, String> countryColumn;
    /**
     * Input field Customer search
     */
    @FXML
    private TextField searchField;
    /**
     * Button to clear search
     */
    @FXML
    private Button clearSearchButton;

    /**
     * Initializes the controller. Sets up the Customers table and populates the data.
     *
     * @param url            Location used to resolve relative paths
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCustomersTableView();
        loadCustomers();
    }

    /**
     * Handles search for Customer by unique identifier or name and filters the table with the results.
     */
    @FXML
    public void onSearch() {
        String input = searchField.getText().trim();

        if (!input.isEmpty()) {
            ObservableList<Customer> customers = FXCollections.observableArrayList();
            try {
                int id = Integer.parseInt(input);
                Customer customer = customerDao.getById(id);

                if (customer != null) {
                    customers.add(customer);
                }
            } catch (NumberFormatException e) {
                customers.setAll(customerDao.getByNameLike(input));
            }
            customersTable.setItems(customers);
            clearSearchButton.setVisible(true);
        }
    }

    /**
     * Handles clearing of the Customer search input and resets the data in the Customers table.
     */
    @FXML
    public void onClearSearch() {
        searchField.clear();
        loadCustomers();
        clearSearchButton.setVisible(false);
    }

    /**
     * Handles the creation of the Customer modal window to create a new Customer.
     *
     * @param actionEvent Event trigger by Create button
     * @throws IOException The Customer modal fails to open
     */
    @FXML
    public void onCreate(ActionEvent actionEvent) throws IOException {
        loadCustomerView(actionEvent, "Create Customer", -1);
    }

    /**
     * Handles the creation of the Customer modal window to update a existing Customer.
     *
     * @param actionEvent Event trigger by Update button
     * @throws IOException The Customer modal fails to open
     */
    @FXML
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            loadCustomerView(actionEvent, "Update Customer", selectedCustomer.getId());
        }
    }

    /**
     * Handles the deletion of existing Customers.
     */
    @FXML
    public void onDelete() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            boolean userConfirmed = AlertUtil.confirmation("Delete", selectedCustomer.getId()
                    + " - " + selectedCustomer.getName(), "Are you sure you want to delete this Customer?");

            if (userConfirmed) {
                int deleted;
                appointmentDao.deleteByCustomerId(selectedCustomer.getId());
                deleted = customerDao.delete(selectedCustomer.getId());

                if (deleted == 0) {
                    AlertUtil.warning("Failed", "Failed to Delete",
                            "Something went wrong. Please try to delete the Customer again.");
                } else {
                    AlertUtil.inform("Success", "Delete Successful",
                            "Customer " + selectedCustomer.getId() + " - " + selectedCustomer.getName() +
                                    " has been deleted.");
                    loadCustomers();
                }
            }
        }
    }

    /**
     * Loads Customer data and replaces all existing data in the table with it.
     */
    private void loadCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList(customerDao.getAll());
        customersTable.setItems(customers);
    }

    /**
     * Sets up the Customer table so it can display the appropriate Customer data.
     */
    private void setupCustomersTableView() {
        // Hide clear search button
        clearSearchButton.setVisible(false);
        // Setup Customers Table View Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        divisionColumn.setCellValueFactory(customerData ->
                new SimpleStringProperty(customerData.getValue().getDivision().getName()));
        countryColumn.setCellValueFactory(customerData ->
                new SimpleStringProperty(customerData.getValue().getDivision().getCountry().getName()));
    }

    /**
     * Load the Customer modal and set it up for either creating or updating a Customer.
     *
     * @param actionEvent      Event triggered by Create or Update buttons
     * @param title            Title of the modal
     * @param selectCustomerId Valid identifier for the Customer to be updated; If not valid modal will be for creation.
     * @throws IOException The <Code>javafx.fxml.FXMLLoader</Code> cannot load <code>CustomerView.fxml</code>.
     */
    private void loadCustomerView(ActionEvent actionEvent, String title, int selectCustomerId) throws IOException {
        Stage customerStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/CustomerView.fxml"));
        Parent parent = loader.load();
        Stage stage = new Stage();
        CustomerViewController controller = loader.getController();

        // A valid customer id indicates a record is being updated
        if (selectCustomerId > 0) {
            controller.initData(stage, selectCustomerId);
        }

        // Init View
        stage.setTitle(title);
        stage.setScene(new Scene(parent, 450, 500));
        stage.initOwner(customerStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(we -> loadCustomers());
        stage.showAndWait();
    }
}
