package info.ryandorman.simplescheduler;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.JavaFxUtilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.logging.*;

public class Main extends Application {

    private static Logger logger = null;
    private static boolean hasError = false;

    /**
     * Set global logging configuration based on properties in <code>logging.properties</code>.
     */
    static {
        try {
            InputStream stream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(stream);
            logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            hasError = true;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

        primaryStage.setTitle("Simple Scheduler");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.show();

        if (hasError) {
            JavaFxUtilities.warning("Error", "Something went wrong.", "Please restart the application and try again.");
        }
    }


    public static void main(String[] args) {
        try {
            // TODO: move all refs to DBConnection to DAOs
            Connection connection = DBConnection.getConnection();
            logger.info("Successfully connected to database: " + connection.toString());
        } catch (Exception e) {
            hasError = true;
            logger.severe(e.getMessage());
        }
        launch(args);
        DBConnection.closeConnection();
    }
}
