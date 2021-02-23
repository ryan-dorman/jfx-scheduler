package info.ryandorman.simplescheduler;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.common.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Starts the application and initializes the loggers. Logger configuration based on global properties in
 * <code>logging.properties</code>. By default <code>java.util.logging.FileHandler</code>s are specified in a static
 * block to allow for the creation of two unique log files. One log captures system activity and the other captures user
 * login activity.
 */
public class Main extends Application {
    /**
     * System Logger
     */
    private static Logger sysLogger = null;

    static {
        try {
            String rootPath = System.getProperty("user.dir");
            InputStream stream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(stream);

            FileHandler sysFh = new FileHandler(rootPath + "/sys_activity.txt", false);
            sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            sysLogger.addHandler(sysFh);

            FileHandler userFh = new FileHandler(rootPath + "/login_activity.txt", true);
            Logger userLogger = Logger.getLogger("userActivity");
            userLogger.addHandler(userFh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes in any CLI arguments and passes them to the JavaFx application.
     * @param args Array of arguments passed to the JVM on application start
     */
    public static void main(String[] args) {
//        Locale.setDefault(new Locale("fr")); // For testing

        launch(args);
    }

    /**
     * Starts the JavaFx application and shows the initial view <code>LoginView.fxml</code>.
     * @param primaryStage The primary JavaFx stage the applications scenes are loaded onto
     * @throws IOException The <Code>javafx.fxml.FXMLLoader</Code> cannot locate the <code>LoginView.fxml</code>
     * file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

        primaryStage.setTitle("Simple Scheduler");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Stops the JavaFx application and closes any open Database connection.
     * @throws Exception There is trouble stopping the application
     */
    @Override
    public void stop() throws Exception {
        sysLogger.info("Application Exiting...");
        sysLogger.info("Closing any open database connections");
        DBConnection.close();
        super.stop();
    }
}
