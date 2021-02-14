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

public class LoginViewController implements Initializable {
    private static final Logger userLogger = Logger.getLogger("userActivity");
    private final UserDao userDao = new UserDaoImpl();
    private int loginAttempts = 0;

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

        // Fire 'onLogin' on 'Enter' key
        loginButton.setDefaultButton(true);

        // Set Timezone Value
        userZone.setText(ZoneId.systemDefault().toString());
    }

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
            userLogger.info("User: " + user.getName() + " - Invalid Login (try=" + loginAttempts + ")");
            AlertUtil.warning(L10nUtil.getLanguage("alert.invalidLogin.title"),
                    L10nUtil.getLanguage("alert.invalidLogin.header"),
                    L10nUtil.getLanguage("alert.invalidLogin.content"));
        }
    }

    @FXML
    public void onCancel() {
        Platform.exit();
    }

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
}
