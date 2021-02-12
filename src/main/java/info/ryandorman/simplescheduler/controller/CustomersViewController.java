package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.AlertUtil;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class CustomersViewController implements Initializable {

    private final CustomerDao customerDao = new CustomerDaoImpl();

    private boolean isFiltered = false;

    // Customers Table
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> divisionColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;

    // Field
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        // Populate the TableView
        loadCustomers();
    }

    @FXML
    public void onSearch() {
        String input = searchField.getText().trim().toLowerCase(Locale.ROOT);

        // If no search input was given reset search filter and display all customers
        if (input.isEmpty() && isFiltered) {
            loadCustomers();
            isFiltered = false;
        } else {
            ObservableList<Customer> customers = FXCollections.observableArrayList(customerDao.getByNameLike(input));
            customersTable.setItems(customers);
            isFiltered = true;
        }
    }

    @FXML
    public void onCreate(ActionEvent actionEvent) throws IOException {
        loadCustomerView(actionEvent, "Create Customer", -1);
    }

    @FXML
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            loadCustomerView(actionEvent, "Update Customer", selectedCustomer.getId());
        }
    }

    @FXML
    public void onDelete() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            boolean userConfirmed = AlertUtil.confirmation("Delete", selectedCustomer.getId()
                    + " - " + selectedCustomer.getName(), "Are you sure you want to delete this Customer?");

            if (userConfirmed) {
                int deleted;
                // TODO: Handle foreign key constraint on a customer's appointments (delete all appointments for customer_id)
                deleted = customerDao.delete(selectedCustomer.getId());

                if (deleted == 0) {
                    AlertUtil.warning("Failed", "Failed to Save Changes",
                            "Something went wrong. Please try to save the Customer again.");
                } else {
                    AlertUtil.inform("Success", "Delete Successful",
                            "Customer " + selectedCustomer.getId() + " - " + selectedCustomer.getName() +
                                    " has been deleted.");
                    loadCustomers();
                }
            }
        }
    }

    private void loadCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList(customerDao.getAll());
        customersTable.setItems(customers);
    }

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
