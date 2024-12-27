package info.ryandorman.simplescheduler.common;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;

/**
 * Allows easy creation and setup of <code>javafx.scene.control.Alert</code> components.
 */
public class AlertUtil {
    /**
     * Prevents direct class instantiation. Methods should be accessed statically.
     */
    private AlertUtil() {
    }

    /**
     * Creates a confirmation window that requires user feedback to determine which path the application takes.
     *
     * @param title   Title of the window
     * @param header  Header in the window
     * @param content Content in main body of window
     * @return A boolean that indicates the user's choice
     */
    public static boolean confirmation(String title, String header, String content) {
        return alert(Alert.AlertType.CONFIRMATION, title, header, content);
    }

    /**
     * Creates a warning window that notifies the user and requires they accept to continue.
     *
     * @param title   Title of the window
     * @param header  Header in the window
     * @param content Content in main body of window
     * @return A boolean that indicates the user has accepted the warning
     */
    public static boolean warning(String title, String header, String content) {
        return alert(Alert.AlertType.WARNING, title, header, content);
    }

    /**
     * Creates a warning window that informs the user and requires they accept to continue.
     *
     * @param title   Title of the window
     * @param header  Header in the window
     * @param content Content in main body of window
     */
    public static void inform(String title, String header, String content) {
        alert(Alert.AlertType.INFORMATION, title, header, content);
    }

    /**
     * Allows easy population of a <code>javafx.scene.control.Alert</code> that is displayed until the user selects a
     * confirmation/choice.
     *
     * @param type    Type of <code>Alert.AlertType</code> to display
     * @param title   Title of the window
     * @param header  Header in the window
     * @param content Content in main body of window
     * @return A boolean that indicates the user has accepted the alert
     */
    private static boolean alert(Alert.AlertType type, String title, String header, String content) {
        // Create Confirmation Alert and set the stylesheet on the pane
        Alert alert = new Alert(type);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(AlertUtil.class.getResource("/view/theme.css").toExternalForm());

        // Set the Alert's type content
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Show the Alert and wait for a response, returning it to the calling method
        Optional<ButtonType> option = alert.showAndWait();

        return option.filter(ButtonType.OK::equals).isPresent();
    }
}
