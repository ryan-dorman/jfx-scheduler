package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import javafx.util.StringConverter;

import java.util.List;

/**
 * wrapper class that allows for easy creation and predictable interaction with items/options used set in a
 * <code>javafx.scene.control.ComboBox</code>.
 */
public class ComboBoxOption {
    private final int id;
    private final String label;
    private final Object value;

    /**
     * Gets a <code>javafx.util.StringConverter</code> that is can be used as a converter for
     * <code>javafx.scene.control.ComboBox</code>. This converter handles the configuration needed for the combo box to
     * work with the interface of this class.
     *
     * @param options Reference to the list of options that will be used in the combo box being configured
     * @return Converter for the combo box being configured
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
     * Creates a new class instance and sets all fields
     * @param id Unique identifier that can be used to identify the option
     * @param label Unique text to be displayed in the combo box as an option
     * @param value Value associated with the label displayed to/selected by the user
     */
    public ComboBoxOption(int id, String label, Object value) {
        this.id = id;
        this.label = label;
        this.value = value;
    }

    /**
     * Gets the unique identifier
     * @return Unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the unique label
     * @return Unique label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the value
     * @return Value associated with the unique label
     */
    public Object getValue() {
        return value;
    }
}
