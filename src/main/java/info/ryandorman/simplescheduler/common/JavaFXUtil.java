package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * A utility class that allow easy creation and manipulation of various JavaFx components such as ComboBoxes, Spinners,
 * and Alerts.
 */
public class JavaFXUtil {

    /**
     * Get a <code>StringConverter</code> that can handle the allows easy conversion between options used in a JavaFX.
     *
     * ComboBox and the option wrapper class <code>ComboBoxOption</code>.
     * @param options Reference to the list of ComboBox options that will be used with the converter
     * @return StringConverter to use with ComboBoxes made up of <code>ComboBoxOption</code>s
     */
    public static StringConverter<ComboBoxOption> getComboBoxConverter(List<ComboBoxOption> options) {
        return new StringConverter<>() {
            @Override
            public String toString(ComboBoxOption option) {
                return option.getLabel();
            }

            @Override
            public ComboBoxOption fromString(String string) {
                return options.stream().filter(option ->
                        option.getLabel().equals(string)).findFirst().orElse(null);
            }
        };
    }

    /**
     * Get a <code>SpinnerValueFactory</code> that allow LocalTime input into the JavaFX Spinner.
     *
     * @param spinner Reference to the Spinner that will be used to accept LocalTime
     * @param format String representing the DateTimeFormat to display LocalTime in
     * @param minTime The lower bounds of LocalTime the Spinner can reach
     * @param maxTime The upper bounds of LocalTime the Spinner can reach
     * @return SpinnerValueFactory to be used as a Spinner's value factory
     */
    public static SpinnerValueFactory<LocalTime> getSpinnerLocalTimeFactory(Spinner<LocalTime> spinner, String format,
                                                                            LocalTime minTime, LocalTime maxTime) {
        return new SpinnerValueFactory<>() {
            {
                setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern(format),
                        DateTimeFormatter.ofPattern(format)));
            }

            @Override
            public void decrement(int i) {
                if (getValue() == null) {
                    setValue(validateTime(LocalTime.now()));
                } else {
                    LocalTime time  = getValue();
                    int caretPos = spinner.getEditor().getCaretPosition();
                    int delimiterPos = spinner.getEditor().getText().indexOf(':');

                    if (caretPos <= delimiterPos) {
                        setValue(validateTime(time.minusHours(i)));
                    } else {
                        setValue(validateTime(time.minusMinutes(i)));
                    }
                    spinner.getEditor().positionCaret(caretPos);
                }
            }

            @Override
            public void increment(int i) {
                if (getValue() == null) {
                    setValue(validateTime(LocalTime.now()));
                } else {
                    LocalTime time  = getValue();
                    int caretPos = spinner.getEditor().getCaretPosition();
                    int delimiterPos = spinner.getEditor().getText().indexOf(':');

                    if (caretPos <= delimiterPos) {
                        setValue(validateTime(time.plusHours(i)));
                    } else {
                        setValue(validateTime(time.plusMinutes(i)));
                    }
                    spinner.getEditor().positionCaret(caretPos);
                }
            }

            private LocalTime validateTime(LocalTime time) {
                if (time.compareTo(minTime) >= 0 && time.compareTo(maxTime) <= 0) {
                    return time;
                } else if (time.compareTo(minTime) < 0) {
                    return minTime;
                } else {
                    return maxTime;
                }
            }
        };
    }

    /**
     * Get a <code>TextFormatter</code> that can parse LocalTime to and from string input.
     *
     * @param format String representing the DateTimeFormat to display LocalTime in
     * @param defaultValue The LocalTime value to return if an invalid string format is encountered
     * @return TextFormatter to manage the string formatting of LocalTime input
     */
    public static TextFormatter<LocalTime> getLocalTimeFormatter(String format, LocalTime defaultValue) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringConverter<LocalTime> converter = new StringConverter<>() {
            @Override
            public String toString(LocalTime time) {
                try {
                    return formatter.format(time);
                } catch (DateTimeParseException e) {
                    return formatter.format(defaultValue);
                }
            }

            @Override
            public LocalTime fromString(String s) {
                try {
                    return LocalTime.parse(s, formatter);
                } catch (DateTimeParseException e) {
                    return defaultValue;
                }
            }
        };

        return new TextFormatter<>(converter, defaultValue, text -> {
            try {
                LocalTime.parse(text.getControlNewText(), formatter);
                return text;
            } catch (DateTimeParseException e) {
                return null;
            }
        });
    }

    /**
     * Create a confirmation window that requires user feedback to determine which path the application takes.
     *
     * @param title Title of the window
     * @param header Header in the window
     * @param content Content in main body of window
     * @return A boolean that indicates the user's choice
     */
    public static boolean confirmation(String title, String header, String content) {
        return alert(Alert.AlertType.CONFIRMATION, title, header, content);
    }

    /**
     * Create a warning window that notifies the user and requires they accept to continue.
     *
     * @param title Title of the window
     * @param header Header in the window
     * @param content Content in main body of window
     * @return A boolean that indicates the user has accepted the warning
     */
    public static boolean warning(String title, String header, String content) {
        return alert(Alert.AlertType.WARNING, title, header, content);
    }

    /**
     * Create a warning window that informs the user and requires they accept to continue.
     *
     * @param title Title of the window
     * @param header Header in the window
     * @param content Content in main body of window
     */
    public static void inform(String title, String header, String content) {
        alert(Alert.AlertType.INFORMATION, title, header, content);
    }

    /**
     * A wrapper function that allows easy population of a JavaFx alert window that is displayed until the user selects
     * a confirmation/choice.
     *
     * @param type Type of JavaFx alert to display
     * @param title Title of the window
     * @param header Header in the window
     * @param content Content in main body of window
     * @return A boolean that indicates the user has accepted the alert
     */
    private static boolean alert(Alert.AlertType type, String title, String header, String content) {
        // Create Confirmation Alert and set the stylesheet on the pane
        Alert alert = new Alert(type);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(JavaFXUtil.class.getResource("/view/theme.css").toExternalForm());

        // Set the Alert's type content
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Show the Alert and wait for a response, returning it to the calling method
        Optional<ButtonType> option = alert.showAndWait();

        return option.filter(ButtonType.OK::equals).isPresent();
    }


}
