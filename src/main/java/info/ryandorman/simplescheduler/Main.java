package info.ryandorman.simplescheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The entry point to the application. It starts the application and initializes the loggers. It then loads the first
 * scene onto the JavaFx stage.
 */
public class Main extends Application {

    private static Logger sysLogger = null;
    private static Logger userLogger = null;

    /**
     * Sets the global logging configurations based on global properties in <code>logging.properties</code>. File
     * handlers are specified in the static block to allow for two unique logs going to separate files. One log captures
     * system activity and the other captures login activity.
     */
    static {
        try {
            String rootPath = System.getProperty("user.dir");
            InputStream stream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(stream);

            FileHandler sysFileHandler = new FileHandler(rootPath + "/sys_activity.txt", false);
            sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            sysLogger.addHandler(sysFileHandler);

            FileHandler userFileHandler = new FileHandler(rootPath + "/login_activity.txt", true);
            userLogger = Logger.getLogger("userActivity");
            userLogger.addHandler(userFileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Take in any CLI arguments and pass them to the JavaFx application.
     *
     * @param args Array of arguments passed to the JVM on application start
     */
    public static void main(String[] args) {
        Locale.setDefault(new Locale("fr")); // For testing
        launch(args);
    }

    /**
     * Start the JavaFx application and show the initial view <code>LoginView.fxml</code>.
     *
     * @param primaryStage The primary JavaFx stage the applications scenes are loaded onto
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

        primaryStage.setTitle("Simple Scheduler");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.show();
    }
}
