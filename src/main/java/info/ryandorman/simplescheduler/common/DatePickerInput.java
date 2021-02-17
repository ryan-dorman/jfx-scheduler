package info.ryandorman.simplescheduler.common;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Improved version of the JavaFx DatePicker that ensures user text input causes the proper value updates.
 */
public class DatePickerInput extends DatePicker {

    public DatePickerInput() {
        setPickerValueBasedOnInput();
    }

    /**
     * Set dates available to select from in picker window to only weekdays in the present and future.
     *
     */
    public void setRestrictBusinessDays() {
        this.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean b) {
                super.updateItem(localDate, b);
                boolean past = localDate.compareTo(LocalDate.now()) < 0;
                boolean weekend = CalendarUtil.isWeekend(localDate.getDayOfWeek());

                setDisable(b || past || weekend);
                setStyle("-fx-background-color: #cccccc;");
            }
        });
    }

    /**
     * Makes sure the value set via text input by user results in the DatePicker's value being updated. The JavaFx
     * default DatePicker behavior does not accomplish this reliably.
     */
    private void setPickerValueBasedOnInput() {
        this.getEditor().focusedProperty().addListener((object, wasFocused, isFocused) -> {
            if (!isFocused) {
                try {
                    String newInput = DatePickerInput.this.getEditor().getText();
                    LocalDate newDate = DatePickerInput.this.getConverter().fromString(newInput);
                    DatePickerInput.this.setValue(newDate);
                } catch (DateTimeParseException e) {
                    LocalDate originalDate = DatePickerInput.this.getValue();
                    String originalInput = DatePickerInput.this.getConverter().toString(originalDate);
                    DatePickerInput.this.getEditor().setText(originalInput);
                }
            }
        });
    }
}
