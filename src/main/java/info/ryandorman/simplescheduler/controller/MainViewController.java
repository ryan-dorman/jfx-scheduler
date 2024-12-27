package info.ryandorman.simplescheduler.controller;

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
 * Handles the controller logic associated with the navigation menu in the application and using the selected item to
 * populate the MainView.
 */
public class MainViewController implements Initializable {
    /**
     * System Logger
     */
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /**
     * Current User logged into the application
     */
    public static User currentUser;
    /**
     * Appointment Data Access Object
     */
    private final AppointmentDao appointmentDao = new AppointmentDaoImpl();

    /**
     * Pane that the selected View is loaded into.
     */
    @FXML
    private BorderPane mainView;
    /**
     * Tab that activates the Dashboard View
     */
    @FXML
    private Button dashboardButton;
    /**
     * Tab that activates the Customers View
     */
    @FXML
    private Button customersButton;
    /**
     * Tab that activates the Appointments View
     */
    @FXML
    private Button appointmentsButton;

    /**
     * Initializes the controller. Sets the initial View chosen on the navigation menu.
     *
     * @param url            Location used to resolve relative paths
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onDashboardClick();
    }

    /**
     * Allows current User data to be passed to the controller.
     *
     * @param currentUser Current application User
     */
    public void initData(User currentUser) {
        MainViewController.currentUser = currentUser;
        checkForUpcomingAppointments();
        sysLogger.info("Main view loaded for " + currentUser.toString());
    }

    /**
     * Sets the Main View to the Dashboard View.
     */
    @FXML
    public void onDashboardClick() {
        setActiveView("DashboardView");
        setActiveStyle(dashboardButton);
    }

    /**
     * Sets the Main View to the Customers View.
     */
    @FXML
    public void onCustomersClick() {
        setActiveView("CustomersView");
        setActiveStyle(customersButton);
    }

    /**
     * Sets the Main View to the Appointments View.
     */
    @FXML
    public void onAppointmentsClick() {
        setActiveView("AppointmentsView");
        setActiveStyle(appointmentsButton);
    }

    /**
     * Handles the logic to set the chosen View into the Main View.
     *
     * @param viewName Name of view to load
     */
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

    /**
     * Sets the active navigation menu styles for the View selected.
     *
     * @param selectedTab Tab selected
     */
    private void setActiveStyle(Button selectedTab) {
        // reset
        dashboardButton.getStyleClass().remove("tab--active");
        customersButton.getStyleClass().remove("tab--active");
        appointmentsButton.getStyleClass().remove("tab--active");

        // set
        selectedTab.getStyleClass().add("tab--active");
    }

    /**
     * Checks to see if the current User has any Appointments starting in the next 15 minutes, and reminds the User of
     * their upcoming schedule. A Lambda is used to perform a quick and readable filter on upcoming Appointments to
     * locate any for the current User.
     */
    private void checkForUpcomingAppointments() {
        ZonedDateTime loginTime = Instant.now().atZone(ZoneId.systemDefault());

        List<Appointment> upcomingUserAppointments = appointmentDao
                .getByStartDateTimeWindow(loginTime, loginTime.plusMinutes(15))
                .stream()
                .filter(app -> app.getUser().getId() == currentUser.getId())
                .collect(Collectors.toList());

        if (upcomingUserAppointments.size() > 0) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy h:mm a");
            StringBuilder appointmentSummary = new StringBuilder();

            for (Appointment app : upcomingUserAppointments) {
                appointmentSummary
                        .append("\n")
                        .append(app.getId()).append("\t")
                        .append(app.getType()).append("\t")
                        .append(app.getStart().format(formatter));
            }

            AlertUtil.inform("Your Appointments", "Upcoming Appointments",
                    "Welcome Back! You have appointments starting in the next 15 minutes:\n" + appointmentSummary);
        } else {
            AlertUtil.inform("Your Appointments", "No Appointments",
                    "Welcome Back! You have no appointments starting in the next 15 minutes.");
        }

    }
}
