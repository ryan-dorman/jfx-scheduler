package info.ryandorman.simplescheduler.controller;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.common.JavaFxUtil;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.dao.UserDao;
import info.ryandorman.simplescheduler.dao.UserDaoImpl;
import info.ryandorman.simplescheduler.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LoginViewController implements Initializable {

    private static final Logger userLogger = Logger.getLogger("userActivity");
    private User currentUser;

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
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set label and button text with correct language
        usernameLabel.setText(L10nUtil.getLanguage("label.username"));
        passwordLabel.setText(L10nUtil.getLanguage("label.password"));
        userZoneLabel.setText(L10nUtil.getLanguage("label.userZone"));
        loginButton.setText(L10nUtil.getLanguage("btn.login"));
        closeButton.setText(L10nUtil.getLanguage("btn.close"));

        // Set Timezone Value
        userZone.setText(L10nUtil.zoneId.toString());
    }

    @FXML
    public void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Validate fields and show errors for missing values
        if (username.isBlank() || username.length() > 50) {
            JavaFxUtil.warning(L10nUtil.getLanguage("alert.invalidUsername.title"),
                    L10nUtil.getLanguage("alert.invalidUsername.header"),
                    L10nUtil.getLanguage("alert.invalidUsername.content"));
            return;
        } else if (password.isBlank()) {
            JavaFxUtil.warning(L10nUtil.getLanguage("alert.invalidPassword.title"),
                    L10nUtil.getLanguage("alert.invalidPassword.header"),
                    L10nUtil.getLanguage("alert.invalidPassword.content"));
            return;
        }

        UserDao userDao = new UserDaoImpl();
        User user = userDao.getByName(username);
        String loginOutcome = "";

        if (user != null && user.getPassword().equals(password)) {
            loginOutcome = "Successful";
            currentUser = user;
            // TODO: to main view
        } else {
            loginOutcome = "Invalid";
            JavaFxUtil.warning(L10nUtil.getLanguage("alert.invalidLogin.title"),
                    L10nUtil.getLanguage("alert.invalidLogin.header"),
                    L10nUtil.getLanguage("alert.invalidLogin.content"));
        }

        userLogger.info(user.getName() + " - " + loginOutcome + " Login Attempt");
    }

    @FXML
    public void onCancel() {
        Platform.exit();
    }
}
