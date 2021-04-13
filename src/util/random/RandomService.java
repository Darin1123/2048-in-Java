package util.random;

/**
 * @author Zefeng Wang - wangz217
 * @brief Random Service Module
 */

public class RandomService {
    /**
     * @brief generates a real number in range [0.0, 1.0]
     * @return a real number in range [0.0, 1.0]
     */
    public static double random() {
        return Math.random();
    }

    /**
     * @brief generates an integer in range [lo, hi]
     * @param lo lower bound
     * @param hi upper bound
     * @return an integer in range [lo, hi]
     */
    public static int random(int lo, int hi) {
        double r = random();
        return (int) Math.floor(lo + r * (hi + 1 - lo));
    }

    /**
     * @brief generate a new number
     * @details 90% chance get a 2, 10% chance get a 4.
     * @return the generated number
     */
    public static int random2or4() {
        double r = random();
        if (r < 0.9) {
            return 2;
        } else {
            return 4;
        }
    }

    /**
     * @brief pick a random member
     * @param choices the choices
     * @return one of them
     */
    public static int pick(int[] choices) {
        int N = choices.length;
        return choices[random(0, N - 1)];
    }
}
