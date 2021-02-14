package info.ryandorman.simplescheduler.common;

public class ComboBoxOption {
    private int id;
    private String label;
    private Object value;

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
