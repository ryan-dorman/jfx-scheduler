package info.ryandorman.simplescheduler.controller;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.common.AlertUtil;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.dao.UserDao;
import info.ryandorman.simplescheduler.dao.UserDaoImpl;
import info.ryandorman.simplescheduler.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Handles the logic associated with user authorization and logging into the application.
 */
public class LoginViewController implements Initializable {
    /**
     * User Logger
     */
    private static final Logger userLogger = Logger.getLogger("userActivity");
    /**
     * User Data Access Object
     */
    private final UserDao userDao = new UserDaoImpl();
    /**
     * Counter to track login attempts
     */
    private int loginAttempts = 0;

    /**
     * Label for the username input field
     */
    @FXML
    private Label usernameLabel;
    /**
     * Label for the password input field
     */
    @FXML
    private Label passwordLabel;
    /**
     * Label to describe User zone
     */
    @FXML
    private Label userZoneLabel;
    /**
     * Label to display current zone application is being accessed from
     */
    @FXML
    private Label userZone;
    /**
     * Input field for username
     */
    @FXML
    private TextField usernameField;
    /**
     * Input field for password
     */
    @FXML
    private TextField passwordField;
    /**
     * Button that triggers login
     */
    @FXML
    private Button loginButton;
    /**
     * Button that closes application
     */
    @FXML
    private Button closeButton;

    /**
     * Initializes the controller. Determines the User's zone and localizes the LoginView accordingly.
     *
     * @param url            Location used to resolve relative paths
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLoginForm();
    }

    /**
     * Handles login and informs User of any issues.
     * @param actionEvent Event created by User interaction with <code>loginButton</code>
     * @throws IOException The MainView did not load successfully.
     */
    @FXML
    public void onLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate fields and show errors for missing values
        if (username.isBlank() || username.length() > 50) {
            AlertUtil.warning(L10nUtil.getLanguage("alert.invalidUsername.title"),
                    L10nUtil.getLanguage("alert.invalidUsername.header"),
                    L10nUtil.getLanguage("alert.invalidUsername.content"));
            return;
        } else if (password.isBlank()) {
            AlertUtil.warning(L10nUtil.getLanguage("alert.invalidPassword.title"),
                    L10nUtil.getLanguage("alert.invalidPassword.header"),
                    L10nUtil.getLanguage("alert.invalidPassword.content"));
            return;
        }

        // Check DB for User of provided name
        User user = userDao.getByName(username.toLowerCase());
        loginAttempts++;

        // If the passwords match login, otherwise alert
        if (user != null && user.getPassword().equals(password)) {
            userLogger.info("User: " + user.getName() + " - Successful Login (try=" + loginAttempts + ")");
            loadMainView(actionEvent, user);
        } else {
            userLogger.info("User: " + username + " - Invalid Login (try=" + loginAttempts + ")");
            AlertUtil.warning(L10nUtil.getLanguage("alert.invalidLogin.title"),
                    L10nUtil.getLanguage("alert.invalidLogin.header"),
                    L10nUtil.getLanguage("alert.invalidLogin.content"));
        }
    }

    /**
     * Handles application close.
     */
    @FXML
    public void onCancel() {
        Platform.exit();
    }

    /**
     * Loads <code>MainView.fxml</code> and pass the current User's data to the <code>MainViewController</code>.
     * @param actionEvent Reference to event fired by the User choice
     * @param currentUser User currently authorized and accessing the application
     * @throws IOException The <Code>javafx.fxml.FXMLLoader</Code> cannot load <code>MainView.fxml</code>.
     */
    private void loadMainView(ActionEvent actionEvent, User currentUser) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/MainView.fxml"));
        Parent parent = loader.load();

        // Pass currentUser ref to Controller
        MainViewController controller = loader.getController();
        controller.initData(currentUser);

        // Set stage with MainView
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(stage.getTitle());
        stage.setScene(new Scene(parent, 1200, 800));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Determines the User's zone and localize the LoginView labels accordingly.
     */
    private void setupLoginForm() {
        // Set label and button text with correct language
        usernameLabel.setText(L10nUtil.getLanguage("label.username"));
        passwordLabel.setText(L10nUtil.getLanguage("label.password"));
        userZoneLabel.setText(L10nUtil.getLanguage("label.userZone"));
        loginButton.setText(L10nUtil.getLanguage("btn.login"));
        closeButton.setText(L10nUtil.getLanguage("btn.close"));

        // Fire 'onLogin' on 'Enter' key
        loginButton.setDefaultButton(true);

        // Set Timezone Value
        userZone.setText(ZoneId.systemDefault().toString());
    }
}
