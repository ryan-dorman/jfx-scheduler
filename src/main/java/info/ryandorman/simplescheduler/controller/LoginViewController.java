package info.ryandorman.simplescheduler.controller;

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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LoginViewController implements Initializable {

    private static final Logger userLogger = Logger.getLogger("userActivity");
    private User user;

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
        // Validate Form fields and show errors for missing values
        // Call userDao.validateLogin(username: String, password: String): boolean
        // If true then log to file and continue to MainView
        // Else log login failure to file and alert user
        userLogger.info("Login Attempt");
//        UserDao userDao = new UserDaoImpl();
//        List<User> users = userDao.getAll();
//
//        for (User user : users) {
//            System.out.println(user.getId());
//            System.out.println(user.getName());
//            System.out.println(user.getPassword());
//            System.out.println(user.getCreated());
//            System.out.println(user.getCreatedBy());
//            System.out.println(user.getUpdated());
//            System.out.println(user.getUpdatedBy());
//        }
    }

    @FXML
    public void onCancel() {
        Platform.exit();
    }
}
