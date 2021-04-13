package model;

/**
 * @author Zefeng Wang - wangz217
 * @brief Cell Module
 */

public class Cell {
    private boolean checked;
    private int value;

    /**
     * @brief no-argument constructor
     */
    public Cell() {}

    /**
     * @brief the constructor
     * @param checked checked value
     * @param value the value
     */
    public Cell(boolean checked, int value) {
        this.checked = checked;
        this.value = value;
    }

    /**
     * @brief get the value
     * @return the value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @brief setter
     * @param newValue the new value
     */
    public void setValue(int newValue) {
        this.value = newValue;
    }

    /**
     * @brief check the cell
     */
    public void check() {
        this.checked = true;
    }

    /**
     * @brief uncheck the cell
     */
    public void unCheck() {
        this.checked = false;
    }

    /**
     * @brief get if checked
     * @return if checked
     */
    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public String toString() {
        if (value != 0) {
            return value + "";
        } else {
            return "-";
        }
    }
}
