package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import javafx.util.StringConverter;

import java.util.List;

/**
 * Data structure that allows for easy creation and predictable interaction with items/options in JavaFx ComboBoxes.
 */
public class ComboBoxOption {
    private final int id;
    private final String label;
    private final Object value;

    /**
     * Get a <code>StringConverter</code> that is can be used as a converter for JavaFx ComboBoxes. This handles the
     * configuration needed for the ComboBox to work with ComboBoxOptions.
     *
     * @param options Reference to the list of ComboBoxOptions that will be used in the ComboBox being configured
     * @return StringConverter to set as a converter for the ComboBox being configured
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
     * Create a new class instance and set all fields
     * @param id Unique identifier that can be used to identify the option
     * @param label Unique text to be displayed in the ComboBox as an option
     * @param value Value associated with the label displayed to/selected by the user
     */
    public ComboBoxOption(int id, String label, Object value) {
        this.id = id;
        this.label = label;
        this.value = value;
    }

    /**
     * Get the unique identifier for this ComboBoxOption
     * @return Unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Get the unique label for this ComboBoxOption
     * @return Unique label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the value associated with this ComboBoxOption
     * @return Value associated with the unique label
     */
    public Object getValue() {
        return value;
    }
}
