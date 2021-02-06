package info.ryandorman.simplescheduler.controller;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainViewController implements Initializable {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // Container
    @FXML
    private BorderPane mainView;

    // Nav Buttons
    @FXML
    private Button dashboardButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button appointmentsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // https://stackoverflow.com/questions/50286130/javafx-keep-toolbar-when-loading-next-scene
        onDashboardClick();
    }

    public void initData(User currentUser) {
        sysLogger.info("Main view loaded for " + currentUser.toString());
    }

    @FXML
    public void onDashboardClick() {
        setActiveView("DashboardView");
    }

    @FXML
    public void onCustomersClick() {
        setActiveView("CustomersView");
    }

    @FXML
    public void onAppointmentsClick() {
        setActiveView("AppointmentsView");
    }

    private void setActiveView(String viewName)  {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/" + viewName + ".fxml"));

        try {
            Parent parent = loader.load();
            mainView.setCenter(parent);
        } catch (IOException e) {
            sysLogger.severe("Could not load " + viewName);
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        }
    }

}
