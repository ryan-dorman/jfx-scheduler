package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.dao.CustomerDao;
import info.ryandorman.simplescheduler.dao.CustomerDaoImpl;
import info.ryandorman.simplescheduler.model.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomersViewController implements Initializable {

    // Customers State
    ObservableList<Customer> customers = FXCollections.observableArrayList();

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
        divisionColumn.setCellValueFactory(customerData ->
                new SimpleStringProperty(customerData.getValue().getDivision().getCountry().getName()));

        // Populate customers with DAOs
        initData();

        customersTable.setItems(customers);
    }

    private void initData() {
        CustomerDao customerDao = new CustomerDaoImpl();
        customers = (ObservableList<Customer>) customerDao.getAll();
    }
}
