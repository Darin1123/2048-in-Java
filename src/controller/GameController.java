package controller;

import model.Cell;
import model.GameBoard;
import util.enumeration.Command;
import util.enumeration.Direction;
import util.random.RandomService;
import view.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameController {
    private final GameBoard gameBoard;
    private final GameBoardView gameBoardView;
    private final HelpView helpView;
    private final ErrorView errorView;
    private final WelcomeView welcomeView;

    public GameController(
            GameBoard gameBoard,
            GameBoardView gameBoardView,
            HelpView helpView,
            ErrorView errorView,
            WelcomeView welcomeView) {
        this.gameBoard = gameBoard;
        this.gameBoardView = gameBoardView;
        this.helpView = helpView;
        this.errorView = errorView;
        this.welcomeView = welcomeView;
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
        this.gameBoard.setState(state);
        this.gameBoard.notifyObserver();

        return welcomeView.render() + gameBoardView.render();
    }

    public String process(String input) {
        Command command;
        try {
            command = Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            errorView.setErrorMessage("Unknown command '" + input + "', enter 'help' to see how to play the game.");
            return errorView.render();
        }

        // process the input
        if (command == Command.EXIT) {
            System.out.println("See you around!");
            System.exit(1);
        }

        if (command == Command.HELP) {
            return this.helpView.render();
        }

        if (command == Command.R) {
            return this.reset();
        }

        if (command == Command.A) {
            return this.collapse(Direction.LEFT);
        }

        if (command == Command.D) {
            return this.collapse(Direction.RIGHT);
        }

        if (command == Command.W) {
            return this.collapse(Direction.UP);
        }

        return welcomeView.render();
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
        this.gameBoard.notifyObserver();
        return gameBoardView.render();
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
        this.gameBoard.notifyObserver();
        return gameBoardView.render();
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
        this.gameBoard.notifyObserver();
        return gameBoardView.render();
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
        this.gameBoard.notifyObserver();
        return gameBoardView.render();
    }

    private void uncheckAll(Cell[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j].unCheck();
            }
        }
    }


}
