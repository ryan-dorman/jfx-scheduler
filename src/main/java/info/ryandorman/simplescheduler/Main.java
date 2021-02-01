package info.ryandorman.simplescheduler;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.JavaFxUtilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String logFilePath = "/logs.txt";
    private static boolean hasError = false;

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
            FileHandler fileHandler = new FileHandler(System.getProperty("user.dir") + logFilePath, true);
            // TODO: Setup formatter per requirements
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

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
