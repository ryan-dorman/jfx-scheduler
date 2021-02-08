package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.model.Country;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {

    // State
    ObservableList<Country> countries;
    ObservableList<FirstLevelDivision> divisions;

    // Customer Labels

    // Customer Fields

    // Customer Combos

    // Buttons
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load Countries for ComboBox
    }

    public void onCountrySelection() {
        // set values on division combo base on selection
    }
}
