package util;

/**
 * @author Zefeng Wang - wangz217
 * @brief Point Module
 */

public class Point {
    private final int x;
    private final int y;

    /**
     * @brief the constructor
     * @param x x value
     * @param y y value
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @brief get x value
     * @return x value
     */
    public int getX() {
        return x;
    }

    /**
     * @brief get y value
     * @return y value
     */
    public int getY() {
        return y;
    }
}
