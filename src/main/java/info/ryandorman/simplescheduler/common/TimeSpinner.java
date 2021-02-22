package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Extends <code>javafx.scene.control.Spinner</code> to handle time input. Design inspired by
 * <a href="https://stackoverflow.com/questions/32613619/how-to-make-a-timespinner-in-javafx.">How to make a TimeSpinner
 * in JavaFx</a>.
 */
public class TimeSpinner extends Spinner<LocalTime> {
    private static final String format = "h:mm a";

    /**
     * Creates a new class instance that sets the default input value to the current time. Allows auto-instantiation
     * through <code>*.fxml</code> files.
     */
    public TimeSpinner() {
        this(LocalTime.now(ZoneId.systemDefault()));
    }

    /**
     * Creates a new class instance that sets the default input value to the user defined time parameter.
     *
     * @param defaultValue Default input value (e.g., time) to display.
     */
    public TimeSpinner(LocalTime defaultValue) {
        this(format, defaultValue);
    }

    public TimeSpinner(String format, LocalTime defaultValue) {
        this.setValueFactory(getSpinnerLocalTimeFactory(format));
        this.getEditor().addEventHandler(MouseEvent.MOUSE_CLICKED, getTimeSpinnerSelectionRules());
        setDefaultValue(defaultValue);
    }

    public void setDefaultValue(LocalTime defaultValue) {
        this.getEditor().setTextFormatter(getLocalTimeFormatter(defaultValue));
    }

    /**
     * Gets a <code>javafx.scene.control.SpinnerValueFactory</code> that allows <code>java.time.LocalTime</code> input.
     *
     * @param format String representing the format to display the time input
     * @return factory to be used as TimeSpinner's value factory
     */
    private SpinnerValueFactory<LocalTime> getSpinnerLocalTimeFactory(String format) {
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
                    LocalTime time = getValue();
                    int caretPos = TimeSpinner.this.getEditor().getCaretPosition();
                    int delimiterPos = TimeSpinner.this.getEditor().getText().indexOf(':');

                    if (caretPos <= delimiterPos) {
                        setValue(time.minusHours(i));
                    } else {
                        setValue(time.minusMinutes(i));
                    }
                    TimeSpinner.this.getEditor().positionCaret(caretPos);
                }
            }

            @Override
            public void increment(int i) {
                if (getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time = getValue();
                    int caretPos = TimeSpinner.this.getEditor().getCaretPosition();
                    int delimiterPos = TimeSpinner.this.getEditor().getText().indexOf(':');

                    if (caretPos <= delimiterPos) {
                        setValue(time.plusHours(i));
                    } else {
                        setValue(time.plusMinutes(i));
                    }
                    TimeSpinner.this.getEditor().positionCaret(caretPos);
                }
            }
        };
    }

    /**
     * Gets a <code>javafx.scene.control.TextFormatter</code> that can parse LocalTime to and from user's text input.
     *
     * @param defaultValue The <code>java.time.LocalTime</code> value to return if an invalid string format is encountered
     * @return Formatter to manage the string formatting of time input into the TimeSpinner
     */
    private TextFormatter<LocalTime> getLocalTimeFormatter(LocalTime defaultValue) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeSpinner.format);
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
     * Get <code>javafx.event.EventHandler</code> that allows for easy selection of hour and minute input
     * displayed in the TimeSpinner. A Lambda is used to simplify formatting and quickly return the
     * <code>javafx.event.EventHandler</code> needed to create the correct selection behavior.
     *
     * @return EventHandler that configures the TimeSpinner's input selection
     */
    private EventHandler<MouseEvent> getTimeSpinnerSelectionRules() {
        return mouseEvent -> {
            int caretPos = TimeSpinner.this.getEditor().getCaretPosition();
            int delimiterPos = TimeSpinner.this.getEditor().getText().indexOf(':');
            int emptyPos = TimeSpinner.this.getEditor().getText().indexOf(' ');
            int startSelect = caretPos;

            if (caretPos == delimiterPos) {
                // if we are before the : the then select hours
                startSelect = caretPos - 1;
            } else if (caretPos >= emptyPos) {
                // if we are near the AM/PM then start at the beginning of this text
                startSelect = emptyPos + 1;
            }
            TimeSpinner.this.getEditor().selectRange(startSelect, startSelect + 1);
        };
    }
}
