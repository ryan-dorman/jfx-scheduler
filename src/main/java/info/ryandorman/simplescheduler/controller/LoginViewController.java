package info.ryandorman.simplescheduler.controller;

import info.ryandorman.simplescheduler.common.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LoginViewController implements Initializable {

    private static final Logger userLogger = Logger.getLogger("userActivity");
    private UserSession session;

    // Login Labels
    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label userZoneLabel;

    @FXML
    private Label userZone;

    // Login Fields
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    // Login Buttons
    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        session = session.getInstance();
        session.setUserZone(ZoneId.systemDefault());
        session.setUserLanguage(Locale.getDefault().getLanguage());
        userZone.setText(session.getUserZone().toString());
    }

    @FXML
    public void onLogin() {
        // Validate Form fields and show errors for missing values
        // Call userDao.validateLogin(username: String, password: String): boolean
        // If true then log to file and continue to MainView
        // Else log login failure to file and alert user
        userLogger.info("Login Attempt");
    }

    @FXML
    public void onCancel() {
        Platform.exit();
    }
}
