package info.ryandorman.simplescheduler.common;

import javafx.util.StringConverter;

import java.util.List;

/**
 * Allows for easy creation and predictable interaction with items/options used set in a
 * <code>javafx.scene.control.ComboBox</code>.
 */
public class ComboBoxOption {
    /**
     * Unique identifier
     */
    private final int id;
    /**
     * Unique label displayed in the combo box
     */
    private final String label;
    /**
     * Value associated with the label
     */
    private final Object value;

    /**
     * Creates a new class instance and sets all fields
     *
     * @param id    Unique identifier
     * @param label Unique label displayed in the combo box
     * @param value Value associated with the label
     */
    public ComboBoxOption(int id, String label, Object value) {
        this.id = id;
        this.label = label;
        this.value = value;
    }

    /**
     * Gets a <code>javafx.util.StringConverter</code> that can be used with a <code>javafx.scene.control.ComboBox</code>.
     * This converter configures the combo box to work with the interface of this class. A Stream and Lambda are used
     * within the created converter to increase the readability of the filter operation that occurs when converting a
     * string to a ComboBoxOption.
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
                return options.stream()
                        .filter(option -> option.getLabel().equals(string))
                        .findFirst().orElse(null);
            }
        };
    }

    /**
     * Gets the unique identifier
     *
     * @return Unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the unique label
     *
     * @return Unique label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the value
     *
     * @return Value associated with the unique label
     */
    public Object getValue() {
        return value;
    }
}
