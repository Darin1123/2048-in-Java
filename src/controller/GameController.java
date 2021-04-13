package controller;

import model.Cell;
import model.ErrorMessage;
import model.GameBoard;
import runner.ViewRegistry;
import util.enumeration.Direction;
import util.random.RandomService;
import view.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameController {
    private final GameBoard gameBoard;
    private final ErrorMessage errorMessage;
    private ViewRegistry viewRegistry;

    public GameController(
            GameBoard gameBoard,
            ErrorMessage errorMessage,
            ViewRegistry viewRegistry) {
        this.gameBoard = gameBoard;
        this.errorMessage = errorMessage;
        this.viewRegistry = viewRegistry;
    }

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

        return viewRegistry.get("hello").render() +
                viewRegistry.get("board").render();
    }

    public boolean checkGameOver() {
        // check game over
        Cell[][] data = gameBoard.getState();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!data[i][j].isChecked()) { // contains empty cell
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

    public String collapse(Direction direction) {
        switch (direction) {
            case LEFT:
                return collapseLeft();
            case RIGHT:
                return collapseRight();
            case UP:
                return collapseUP();
            case DOWN:
                return collapseDown();
            default:
                return ((View) () -> "Unknown direction").render();
        }
    }

    private String collapseDown() {
        Cell[][] state = this.gameBoard.getState();
        // TODO

        if (checkGameOver()) {
            return ((View) () -> "game over.").render();
        }
        this.uncheckAll(state);
        // TODO generate a new number in a random empty cell
        this.gameBoard.setState(state);
        return viewRegistry.get("board").render();
    }

    private String collapseUP() {
        Cell[][] state = this.gameBoard.getState();
        // TODO

        if (checkGameOver()) {
            return ((View) () -> "game over.").render();
        }
        this.uncheckAll(state);
        // TODO generate a new number in a random empty cell
        this.gameBoard.setState(state);
        return viewRegistry.get("board").render();
    }

    private String collapseLeft() {
        Cell[][] state = this.gameBoard.getState();
        for (int i = 0; i < 4; i++) {  // iterate by rows
            for (int j = 0; j < 4; j++) {  // iterate row items
                if (state[i][j].getValue() != 0) {  // find the first non-zero item
                    for (int k = j - 1; k >= 0; k--) {  // look back and find farthest non-zero cell
                        if (state[i][k].getValue() != 0) {  // find the non-zero target
                            if (state[i][k].isChecked()) {  // if checked, directly move that to the next
                                state[i][k + 1].setValue(state[i][j].getValue());  // move
                                state[i][j].setValue(0);  // set that to 0
                                break;
                            } else {  // it is unchecked
                                // compare values
                                if (state[i][k].getValue() == state[i][j].getValue()) {  // same -> merge
                                    state[i][k].setValue(state[i][k].getValue() * 2);  // grow
                                    state[i][k].check();  // check only after merging
                                    state[i][j].setValue(0);  // set that to zero
                                } else {  // different -> move
                                    state[i][k + 1].setValue(state[i][j].getValue());  // move
                                    state[i][j].setValue(0);  // set that to zero
                                }
                                break;
                            }
                        } else if (k == 0) {  // reaches the head, and it is unchecked
                            state[i][0].setValue(state[i][j].getValue());  // place that item to the head
                            state[i][j].setValue(0);  // set that to 0
                        }
                    }
                }
            }
        }

        if (checkGameOver()) {
            return ((View) () -> "game over.").render();
        }
        this.uncheckAll(state);
        // TODO generate a new number in a random empty cell
        this.gameBoard.setState(state);
        return viewRegistry.get("board").render();
    }

    private String collapseRight() {
        Cell[][] state = this.gameBoard.getState();
        // TODO

        if (checkGameOver()) {
            return ((View) () -> "game over.").render();
        }
        this.uncheckAll(state);
        // TODO generate a new number in a random empty cell
        this.gameBoard.setState(state);
        return viewRegistry.get("board").render();
    }

    private void uncheckAll(Cell[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j].unCheck();
            }
        }
    }


    public String displayError(String errorMessage) {
        this.errorMessage.setMessage(errorMessage);
        return this.viewRegistry.get("error").render();
    }

    public String displayGameBoard() {
        return this.viewRegistry.get("board").render();
    }

    public String displayHelp() {
        return this.viewRegistry.get("help").render();
    }

    public String displayWelcome() {
        return this.viewRegistry.get("welcome").render();
    }
}
