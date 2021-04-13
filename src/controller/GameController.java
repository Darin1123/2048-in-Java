package controller;

import model.Cell;
import model.ErrorMessage;
import model.GameBoard;
import runner.ViewRegistry;
import util.Point;
import util.enumeration.Direction;
import util.random.RandomService;
import view.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zefeng Wang - wangz217
 * @brief Game Controller Module
 */

public class GameController {
    private final GameBoard gameBoard;
    private final ErrorMessage errorMessage;
    private final ViewRegistry viewRegistry;

    /**
     * @brief constructor
     * @param gameBoard the game board object
     * @param errorMessage the error message object
     * @param viewRegistry the view registry
     */
    public GameController(
            GameBoard gameBoard,
            ErrorMessage errorMessage,
            ViewRegistry viewRegistry) {
        this.gameBoard = gameBoard;
        this.errorMessage = errorMessage;
        this.viewRegistry = viewRegistry;
    }

    /**
     * @brief reset the game
     * @return the rendered content after reset
     */
    public String reset() {
        Cell[][] state = new Cell[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = new Cell();
            }
        }
        int[] indices = new int[]{0, 1, 2, 3};
        int x1 = RandomService.pick(indices);
        int x2 = RandomService.pick(indices);
        int v1 = RandomService.random2or4();
        int v2 = RandomService.random2or4();
        int y1, y2;

        if (x1 == x2) {
            y1 = RandomService.pick(indices);
            List<Integer> remaining = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
            remaining.remove(y1);
            y2 = remaining.get(RandomService.random(0, 2));
        } else {
            y1 = RandomService.pick(indices);
            y2 = RandomService.pick(indices);
        }


        state[x1][y1].setValue(v1);
        state[x2][y2].setValue(v2);

        gameBoard.setState(state);

        return viewRegistry.get("welcome").render() +
                viewRegistry.get("board").render();
    }

    /**
     * @brief check if game over
     * @return the result
     */
    public boolean checkGameOver() {
        // check game over
        Cell[][] data = gameBoard.getState();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (data[i][j].getValue() == 0) { // contains empty cell
                    return false;
                }
                int value = data[i][j].getValue();
                if (i < 3 && data[i + 1][j].getValue() == value) { // vertical identical number
                    return false;
                }
                if (j < 3 && data[i][j + 1].getValue() == value) { // horizontal identical number
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @brief move logic
     * @param direction the move direction
     * @return the rendered board content after moving
     */
    public String collapse(Direction direction) {
        switch (direction) {
            case LEFT:
                return collapseLeft();
            case RIGHT:
                return collapseRight();
            case UP:
                return collapseUp();
            case DOWN:
                return collapseDown();
            default:
                return ((View) () -> "Unknown direction").render();
        }
    }

    /**
     * @brief display error view
     * @param errorMessage the error message
     * @return rendered error view
     */
    public String displayError(String errorMessage) {
        this.errorMessage.setMessage(errorMessage);
        return this.viewRegistry.get("error").render();
    }

    /**
     * @brief display game board view
     * @return rendered game board view
     */
    public String displayGameBoard() {
        return this.viewRegistry.get("board").render();
    }

    /**
     * @brief display help view
     * @return rendered help view
     */
    public String displayHelp() {
        return this.viewRegistry.get("help").render();
    }

    /**
     * @brief display welcome view
     * @return rendered welcome view
     */
    public String displayWelcome() {
        return this.viewRegistry.get("welcome").render();
    }

    /**
     * @brief move down
     * @return the rendered board content after moving
     */
    private String collapseDown() {
        Cell[][] state = this.gameBoard.getState();
        boolean changed = false;
        for (int i = 0; i < 4; i++) {  // iterate by columns
            for (int j = 3; j >= 0; j--) {  // iterate column items
                if (state[j][i].getValue() != 0) {  // find the first non-zero item
                    for (int k = j + 1; k <= 3; k++) {  // look back and find farthest non-zero cell
                        if (state[k][i].getValue() != 0) {  // find the non-zero target
                            if (state[k][i].isChecked()) {  // if checked, directly move that to the next
                                state[k - 1][i].setValue(state[j][i].getValue());  // move
                                if (k - 1 != j) {
                                    state[j][i].setValue(0);  // set that to 0
                                    changed = true;
                                }
                                break;
                            } else {  // it is unchecked
                                // compare values
                                if (state[k][i].getValue() == state[j][i].getValue()) {  // same -> merge
                                    state[k][i].setValue(state[k][i].getValue() * 2);  // grow
                                    state[k][i].check();  // check only after merging
                                    state[j][i].setValue(0);  // set that to zero
                                    changed = true;
                                } else {  // different -> move
                                    state[k - 1][i].setValue(state[j][i].getValue());  // move
                                    if (k - 1 != j) {
                                        state[j][i].setValue(0);  // set that to 0
                                        changed = true;
                                    }
                                }
                                break;
                            }
                        } else if (k == 3) {  // reaches the head, and it is unchecked
                            state[3][i].setValue(state[j][i].getValue());  // place that item to the head
                            state[j][i].setValue(0);  // set that to 0
                            changed = true;
                        }
                    }
                }
            }
        }

        this.uncheckAll(state);
        // generate a new number in a random empty cell
        if (changed) {
            generateNew(state);
        }
        this.gameBoard.setState(state);
        return viewRegistry.get("board").render();
    }

    /**
     * @brief move up
     * @return the rendered board content after moving
     */
    private String collapseUp() {
        Cell[][] state = this.gameBoard.getState();
        boolean changed = false;
        for (int i = 0; i < 4; i++) {  // iterate by columns
            for (int j = 0; j < 4; j++) {  // iterate column items
                if (state[j][i].getValue() != 0) {  // find the first non-zero item
                    for (int k = j - 1; k >= 0; k--) {  // look back and find farthest non-zero cell
                        if (state[k][i].getValue() != 0) {  // find the non-zero target
                            if (state[k][i].isChecked()) {  // if checked, directly move that to the next
                                state[k + 1][i].setValue(state[j][i].getValue());  // move
                                if (k + 1 != j) {
                                    state[j][i].setValue(0);  // set that to 0
                                    changed = true;
                                }
                                break;
                            } else {  // it is unchecked
                                // compare values
                                if (state[k][i].getValue() == state[j][i].getValue()) {  // same -> merge
                                    state[k][i].setValue(state[k][i].getValue() * 2);  // grow
                                    state[k][i].check();  // check only after merging
                                    state[j][i].setValue(0);  // set that to zero
                                    changed = true;
                                } else {  // different -> move
                                    state[k + 1][i].setValue(state[j][i].getValue());  // move
                                    if (k + 1 != j) {
                                        state[j][i].setValue(0);  // set that to zero
                                        changed = true;
                                    }
                                }
                                break;
                            }
                        } else if (k == 0) {  // reaches the head, and it is unchecked
                            state[0][i].setValue(state[j][i].getValue());  // place that item to the head
                            state[j][i].setValue(0);  // set that to 0
                            changed = true;
                        }
                    }
                }
            }
        }

        this.uncheckAll(state);
        // generate a new number in a random empty cell
        if (changed) {
            generateNew(state);
        }
        this.gameBoard.setState(state);
        return viewRegistry.get("board").render();
    }

    /**
     * @brief move left
     * @return the rendered board content after moving
     */
    private String collapseLeft() {
        Cell[][] state = this.gameBoard.getState();
        boolean changed = false;
        for (int i = 0; i < 4; i++) {  // iterate by rows
            for (int j = 0; j < 4; j++) {  // iterate row items
                if (state[i][j].getValue() != 0) {  // find the first non-zero item
                    for (int k = j - 1; k >= 0; k--) {  // look back and find farthest non-zero cell
                        if (state[i][k].getValue() != 0) {  // find the farthest non-zero target
                            if (state[i][k].isChecked()) {  // if checked, directly move that to the next
                                state[i][k + 1].setValue(state[i][j].getValue());  // move
                                if (k + 1 != j) {
                                    state[i][j].setValue(0);  // set that to 0
                                    changed = true;
                                }
                                break;
                            } else {  // it is unchecked
                                // compare values
                                if (state[i][k].getValue() == state[i][j].getValue()) {  // same -> merge
                                    changed = true;
                                    state[i][k].setValue(state[i][k].getValue() * 2);  // grow
                                    state[i][k].check();  // check only after merging
                                    state[i][j].setValue(0);  // set that to zero
                                } else {  // different -> move
                                    state[i][k + 1].setValue(state[i][j].getValue());  // move
                                    if (k + 1 != j) {
                                        state[i][j].setValue(0);  // set that to 0
                                        changed = true;
                                    }
                                }
                                break;
                            }
                        } else if (k == 0) {  // reaches the head, and it is unchecked
                            state[i][0].setValue(state[i][j].getValue());  // place that item to the head
                            state[i][j].setValue(0);  // set that to 0
                            changed = true;
                        }
                    }
                }
            }
        }
        this.uncheckAll(state);
        // generate a new number in a random empty cell
        if (changed) {
            generateNew(state);
        }
        this.gameBoard.setState(state);
        return viewRegistry.get("board").render();
    }

    /**
     * @brief move right
     * @return the rendered board content after moving
     */
    private String collapseRight() {
        Cell[][] state = this.gameBoard.getState();
        boolean changed = false;
        for (int i = 0; i < 4; i++) {  // iterate by rows
            for (int j = 3; j >= 0; j--) {  // iterate row items
                if (state[i][j].getValue() != 0) {  // find the first non-zero item
                    for (int k = j + 1; k <= 3; k++) {  // look back and find farthest non-zero cell
                        if (state[i][k].getValue() != 0) {  // find the non-zero target
                            if (state[i][k].isChecked()) {  // if checked, directly move that to the next
                                state[i][k - 1].setValue(state[i][j].getValue());  // move
                                if (k - 1 != j) {
                                    state[i][j].setValue(0);  // set that to 0
                                    changed = true;
                                }
                                break;
                            } else {  // it is unchecked
                                // compare values
                                if (state[i][k].getValue() == state[i][j].getValue()) {  // same -> merge
                                    changed = true;
                                    state[i][k].setValue(state[i][k].getValue() * 2);  // grow
                                    state[i][k].check();  // check only after merging
                                    state[i][j].setValue(0);  // set that to zero
                                } else {  // different -> move
                                    state[i][k - 1].setValue(state[i][j].getValue());  // move
                                    if (k - 1 != j) {
                                        state[i][j].setValue(0);  // set that to zero
                                        changed = true;
                                    }
                                }
                                break;
                            }
                        } else if (k == 3) {  // reaches the head, and it is unchecked
                            state[i][3].setValue(state[i][j].getValue());  // place that item to the head
                            state[i][j].setValue(0);  // set that to 0
                            changed = true;
                        }
                    }
                }
            }
        }

        this.uncheckAll(state);
        // generate a new number in a random empty cell
        if (changed) {
            generateNew(state);
        }
        this.gameBoard.setState(state);
        return viewRegistry.get("board").render();
    }

    /**
     * @brief generate new number after moving
     */
    private void generateNew(Cell[][] state) {
        // find all empty spots
        List<Point> emptySpots = new ArrayList<>();
        // iterate all entries
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (state[i][j].getValue() == 0 ) {
                    emptySpots.add(new Point(i, j));
                }
            }
        }
        int value = RandomService.random2or4();
        Point target = emptySpots.get(RandomService.random(0, emptySpots.size() - 1));
        state[target.getX()][target.getY()].setValue(value);
    }

    /**
     * @brief uncheck all cells
     */
    private void uncheckAll(Cell[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j].unCheck();
            }
        }
    }
}
