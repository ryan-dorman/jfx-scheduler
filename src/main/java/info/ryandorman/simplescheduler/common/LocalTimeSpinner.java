package info.ryandorman.simplescheduler.common;

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
 * A customer JavaFx component that handles LocalTime input into the Spinner.
 * Design inspired by https://stackoverflow.com/questions/32613619/how-to-make-a-timespinner-in-javafx
 */
public class LocalTimeSpinner extends Spinner<LocalTime> {

    private static final String format = "h:mm a";

    public LocalTimeSpinner() {
        this(LocalTime.now(ZoneId.systemDefault()));
    }

    public LocalTimeSpinner(LocalTime defaultValue) {
        this(format, defaultValue);
    }

    public LocalTimeSpinner(String format, LocalTime defaultValue) {
        this.setValueFactory(getSpinnerLocalTimeFactory(format));
        this.getEditor().addEventHandler(MouseEvent.MOUSE_CLICKED, getTimeSpinnerSelectionRules());
        setDefaultValue(defaultValue);
    }

    public void setDefaultValue(LocalTime defaultValue) {
        this.getEditor().setTextFormatter(getLocalTimeFormatter(format, defaultValue));
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
                    int caretPos = LocalTimeSpinner.this.getEditor().getCaretPosition();
                    int delimiterPos = LocalTimeSpinner.this.getEditor().getText().indexOf(':');

                    if (caretPos <= delimiterPos) {
                        setValue(time.minusHours(i));
                    } else {
                        setValue(time.minusMinutes(i));
                    }
                    LocalTimeSpinner.this.getEditor().positionCaret(caretPos);
                }
            }

            @Override
            public void increment(int i) {
                if (getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time  = getValue();
                    int caretPos = LocalTimeSpinner.this.getEditor().getCaretPosition();
                    int delimiterPos = LocalTimeSpinner.this.getEditor().getText().indexOf(':');

                    if (caretPos <= delimiterPos) {
                        setValue(time.plusHours(i));
                    } else {
                        setValue(time.plusMinutes(i));
                    }
                    LocalTimeSpinner.this.getEditor().positionCaret(caretPos);
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
    private TextFormatter<LocalTime> getLocalTimeFormatter(String format, LocalTime defaultValue) {
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

    private EventHandler<MouseEvent> getTimeSpinnerSelectionRules() {
        return mouseEvent -> {
            int caretPos = LocalTimeSpinner.this.getEditor().getCaretPosition();
            int delimiterPos = LocalTimeSpinner.this.getEditor().getText().indexOf(':');
            int emptyPos = LocalTimeSpinner.this.getEditor().getText().indexOf(' ');
            int startSelect = caretPos;

            if (caretPos == delimiterPos) {
                // if we are before the : the then select hours
                startSelect = caretPos - 1;
            } else if (caretPos >= emptyPos) {
                // if we are near the AM/PM then start at the beginning of this text
                startSelect = emptyPos + 1;
            }
            LocalTimeSpinner.this.getEditor().selectRange(startSelect, startSelect + 1);
        };
    }
}
