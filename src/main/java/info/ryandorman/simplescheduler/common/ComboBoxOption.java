package info.ryandorman.simplescheduler.common;

import javafx.util.StringConverter;

import java.util.List;

public class ComboBoxOption {
    private final int id;
    private final String label;
    private final Object value;

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

    public ComboBoxOption(int id, String label, Object value) {
        this.id = id;
        this.label = label;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }
}
