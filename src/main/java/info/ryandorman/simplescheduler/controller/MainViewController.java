package info.ryandorman.simplescheduler.controller;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.common.AlertUtil;
import info.ryandorman.simplescheduler.dao.AppointmentDao;
import info.ryandorman.simplescheduler.dao.AppointmentDaoImpl;
import info.ryandorman.simplescheduler.model.Appointment;
import info.ryandorman.simplescheduler.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller for the the MainView of the application. Handles the logic associated with the navigation items
 * in the application and loading the appropriate child views into the MainView based on the navigation item selected.
 */
public class MainViewController implements Initializable {

    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final AppointmentDao appointmentDao = new AppointmentDaoImpl();

    public static User currentUser;

    // Container
    @FXML
    private BorderPane mainView;

    // Menu Buttons
    @FXML
    private Button dashboardButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button appointmentsButton;

    /**
     * Initializes the controller. Sets the initial View chosen on the navigation menu.
     * @param url Location used to resolve relative paths
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onDashboardClick();
    }

    public void initData(User currentUser) {
        MainViewController.currentUser = currentUser;
        checkForUpcomingAppointments();
        sysLogger.info("Main view loaded for " + currentUser.toString());
    }

    @FXML
    public void onDashboardClick() {
        setActiveView("DashboardView");
        setActiveStyle(dashboardButton);
    }

    @FXML
    public void onCustomersClick() {
        setActiveView("CustomersView");
        setActiveStyle(customersButton);
    }

    @FXML
    public void onAppointmentsClick() {
        setActiveView("AppointmentsView");
        setActiveStyle(appointmentsButton);
    }

    private void setActiveView(String viewName) {
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

    private void setActiveStyle(Button selectedTab) {
        // reset
        dashboardButton.getStyleClass().remove("tab--active");
        customersButton.getStyleClass().remove("tab--active");
        appointmentsButton.getStyleClass().remove("tab--active");

        // set
        selectedTab.getStyleClass().add("tab--active");
    }

    private void checkForUpcomingAppointments() {
        ZonedDateTime loginTime = Instant.now().atZone(ZoneId.systemDefault());

        List<Appointment> upcomingUserAppointments = appointmentDao
                .getByStartDateTimeWindow(loginTime, loginTime.plusMinutes(15))
                .stream()
                .filter(app -> app.getUser().getId() == currentUser.getId())
                .collect(Collectors.toList());

        if (upcomingUserAppointments.size() > 0) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy h:mm a");
            String appointmentSummary = "";

            for (Appointment app : upcomingUserAppointments) {
                appointmentSummary += "\n" + app.getId() + "\t" + app.getType() + "\t" +
                        app.getStart().format(formatter);
            }

            AlertUtil.inform("Your Appointments", "Upcoming Appointments",
                    "Welcome Back! You have appointments starting in the next 15 minutes:\n" + appointmentSummary);
        } else {
            AlertUtil.inform("Your Appointments", "No Appointments",
                    "Welcome Back! You have no appointments starting in the next 15 minutes.");
        }

    }
}
