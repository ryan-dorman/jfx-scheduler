package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalDate;
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
     * @return SpinnerValueFactory to be used as a Spinner's value factory
     */
    public static SpinnerValueFactory<LocalTime> getSpinnerLocalTimeFactory(Spinner<LocalTime> spinner, String format) {
        return new SpinnerValueFactory<>() {
            {
                setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern(format),
                        DateTimeFormatter.ofPattern(format)));
            }

            @Override
            public void decrement(int i) {
                if (getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time  = getValue();
                    int caretPos = spinner.getEditor().getCaretPosition();
                    int delimiterPos = spinner.getEditor().getText().indexOf(':');

                    if (caretPos <= delimiterPos) {
                        setValue(time.minusHours(i));
                    } else {
                        setValue(time.minusMinutes(i));
                    }
                    spinner.getEditor().positionCaret(caretPos);
                }
            }

            @Override
            public void increment(int i) {
                if (getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time  = getValue();
                    int caretPos = spinner.getEditor().getCaretPosition();
                    int delimiterPos = spinner.getEditor().getText().indexOf(':');

                    if (caretPos <= delimiterPos) {
                        setValue(time.plusHours(i));
                    } else {
                        setValue(time.plusMinutes(i));
                    }
                    spinner.getEditor().positionCaret(caretPos);
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

    public static EventHandler<MouseEvent> getTimeSpinnerSelectionRules(Spinner<LocalTime> spinner) {
        return mouseEvent -> {
            int caretPos = spinner.getEditor().getCaretPosition();
            int delimiterPos = spinner.getEditor().getText().indexOf(':');
            int emptyPos = spinner.getEditor().getText().indexOf(' ');
            int startSelect = caretPos;

            if (caretPos == delimiterPos) {
                // if we are before the : the then select hours
                startSelect = caretPos - 1;
            } else if (caretPos >= emptyPos) {
                // if we are near the AM/PM then start at the beginning of this text
                startSelect = emptyPos + 1;
            }
            spinner.getEditor().selectRange(startSelect, startSelect + 1);
        };
    }

    /**
     * Get a <code>DateCell</code> to supply to a <code>DatePicker.setDayCellFactory</code> callback that disables
     * cells that occur in the past and on weekends.
     *
     * @return DateCell that is set to disabled for prior dates and weekends.
     */
    public static DateCell getDisabledPastAndWeekendDateCell() {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean b) {
                super.updateItem(localDate, b);
                boolean past = localDate.compareTo(LocalDate.now()) < 0;
                boolean weekend = CalendarUtil.isWeekend(localDate.getDayOfWeek());

                setDisable(b || past || weekend);
                setStyle("-fx-background-color: #cccccc;");
            }
        };
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
