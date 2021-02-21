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
 * A customer JavaFx component that extends <code>javafx.scene.control.Spinner</code> to handle time input. Design
 * inspired by <a href="https://stackoverflow.com/questions/32613619/how-to-make-a-timespinner-in-javafx.">How to make a
 * TimeSpinner in JavaFx</a>.
 */
public class TimeSpinner extends Spinner<LocalTime> {

    private static final String format = "h:mm a";

    /**
     * Creates a new class instance that sets the default input value to the current time. Allows auto-instantiation
     * through .fxml files.
     */
    public TimeSpinner() {
        this(LocalTime.now(ZoneId.systemDefault()));
    }

    /**
     *  Creates a new class instance that sets the default input value to the user defined <code>java.time.LocalTime
     *  </code> parameter.
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
     * Get a <code>SpinnerValueFactory</code> that allow LocalTime input into the JavaFX Spinner.
     *
     * @param format String representing the DateTimeFormat to display LocalTime in
     * @return SpinnerValueFactory to be used as a Spinner's value factory
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
                    LocalTime time  = getValue();
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
                    LocalTime time  = getValue();
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
     * Get a <code>TextFormatter</code> that can parse LocalTime to and from string input.
     *
     * @param defaultValue The LocalTime value to return if an invalid string format is encountered
     * @return TextFormatter to manage the string formatting of LocalTime input
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
     * Get <code>EventHandler<MouseEvent></code> that allows for easy selection of time input displayed in spinner.
     * @return EventHandler that configures time spinner input selection
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
