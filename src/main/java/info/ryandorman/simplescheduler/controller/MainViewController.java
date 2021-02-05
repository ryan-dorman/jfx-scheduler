package info.ryandorman.simplescheduler.controller;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.model.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainViewController implements Initializable {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // https://stackoverflow.com/questions/50286130/javafx-keep-toolbar-when-loading-next-scene
    }

    public void initData(User currentUser) {
        sysLogger.info("Main view loaded for " + currentUser.toString());
    }

}
