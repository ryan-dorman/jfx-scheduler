package info.ryandorman.simplescheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.*;

/**
 * The entry point to the application. It makes the initial calls to start the JavaFx application and load the first
 * scene onto the primary stage.
 */
public class Main extends Application {

    private static Logger logger = null;

    /**
     * Sets the global logging configuration based on properties in <code>logging.properties</code>.
     */
    static {
        try {
            InputStream stream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(stream);
            logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the JavaFx application and show the initial view <code>LoginView.fxml</code>.
     * @param primaryStage The primary JavaFx stage the applications scenes are loaded onto
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

        primaryStage.setTitle("Simple Scheduler");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.show();
    }

    /**
     * Take in any CLI arguments and pass them to the JavaFx application.
     * @param args Array of arguments passed to the JVM on application start
     */
    public static void main(String[] args) {
        launch(args);
    }
}
