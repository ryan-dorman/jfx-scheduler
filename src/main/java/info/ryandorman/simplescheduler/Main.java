package info.ryandorman.simplescheduler;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.JavaFxUtilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    private static boolean hasError = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));

        primaryStage.setTitle("Simple Scheduler");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.show();

        if (hasError) {
            // TODO: localize this copy
            JavaFxUtilities.warning("Error", "Something went wrong.", "Please restart the application and try again.");
        }
    }


    public static void main(String[] args) {
        try {
            DBConnection.getConnection();
        } catch (Exception e) {
            hasError = true;
            // Todo: replace prntln with concat to log file
            System.out.println(e);
        }
        launch(args);
        DBConnection.closeConnection();
    }
}
