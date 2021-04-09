package model;

public class Cell {
    private boolean checked;
    private int value;

    public Cell() {}

    public Cell(boolean checked, int value) {
        this.checked = checked;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int newValue) {
        this.value = newValue;
    }

    public void check() {
        this.checked = true;
    }

    public void unCheck() {
        this.checked = false;
        this.value = 0;
    }

    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public String toString() {
        if (checked) {
            return value + "";
        } else {
            return "-";
        }
    }
}
