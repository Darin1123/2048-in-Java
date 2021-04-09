package controller;

import model.Cell;
import model.GameBoard;
import util.enumeration.Command;
import util.enumeration.Direction;
import util.random.RandomService;
import view.ErrorView;
import view.GameBoardView;
import view.HelpView;
import view.WelcomeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameController {
    private static final String GAME_EXIT = "exit";

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
        int y1 = -1, y2 = -1;

        try {
            if (x1 == x2) {
                y1 = RandomService.pick(indices);
                List<Integer> remaining = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
                remaining.remove(y1);
                y2 = remaining.get(RandomService.random(0, 2));
            } else {
                y1 = RandomService.pick(indices);
                y2 = RandomService.pick(indices);
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(y1);
        }

        state[x1][y1].setValue(v1);
        state[x1][y1].check();

        state[x2][y2].setValue(v2);
        state[x2][y2].check();
        this.gameBoard.setState(state);
        this.gameBoard.notifyObserver();

        return welcomeView.render() + gameBoardView.render();
    }

    public String process(String input) {
        // process the input
        if (input.equals(GAME_EXIT)) {
            System.out.println("See you around!");
            System.exit(1);
        }
        if (input.equals("help")) {
            return this.helpView.render();
        }

        Command command;
        try {
            command = Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            errorView.setErrorMessage("Unknown command '" + input + "', enter 'help' to see how to play the game.");
            return errorView.render();
        }

        if (command == Command.R) {
            return this.reset();
        }

        if (command == Command.A) {
            return this.collapse(Direction.LEFT);
        }

        // TODO do something about the game board
        Cell[][] data = gameBoard.getState();
        // TODO modify data according to input
        gameBoard.setState(data);
        // notify observer
        gameBoard.notifyObserver();
        return gameBoardView.render();
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
        if (direction == Direction.LEFT) {
            return collapseLeft();
        }
        return "---";
    }

    private String collapseLeft() {
        Cell[][] state = this.gameBoard.getState();
        for (int i = 0; i < 4; i++) {
            state[i] = collapse(state[i]);
        }
        this.gameBoard.setState(state);
        this.gameBoard.notifyObserver();
        return gameBoardView.render();
    }

    /**
     * [2, 2, 2, 2] -> [4, 4, -, -]
     * [32, 16, 16, 2] -> [32, 32, 2, -]
     *
     * @param sequence
     * @return
     */
    private static Cell[] collapse(Cell[] sequence) {
        Cell[] result = new Cell[4];
        for (int i = 0; i < 4; i++) {
            result[i] = new Cell();
        }
        int index = 0;
        int currentValue = 0;
        int nextValue = 0;
        int nextIndex = index;
        while (index < 4) {
            // find the current value
            for (int i = index; i < 4; i++) {
                if (sequence[i].isChecked()) {
                    currentValue = sequence[i].getValue();
                    result[index].setValue(currentValue);
                    result[index].check();
                    break;
                }
                index++;
            }
            if (index >= 3) {
                break;
            }
            // find the next value
            nextIndex = index + 1;
            for (int i = index + 1; i < 4; i++) {
                if (sequence[i].isChecked()) {
                    nextValue = sequence[i].getValue();
                    break;
                }
                nextIndex++;
            }
            if (nextIndex > 3) {
                break;
            }
            if (currentValue == nextValue) {
                result[index].setValue(currentValue * 2);
                result[index].check();
                result[nextIndex].unCheck();
                index = nextIndex + 1;
            } else {
                currentValue = nextValue;
                index = nextIndex;
            }
        }
        // move to left
        for (int i = 1; i < result.length; i++) {
            if (result[i].isChecked()) {
                int j = i - 1;
                while (j >= 0 && !result[j].isChecked()) {
                    j--;
                }
                if ((j + 1) != i) {
                    result[j + 1].setValue(result[i].getValue());
                    result[j + 1].check();
                    result[i].unCheck();
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Cell[] cells = new Cell[]{
                new Cell(false, 32),
                new Cell(true, 4),
                new Cell(true, 2),
                new Cell(false, 2)
        };
        System.out.println(Arrays.toString(GameController.collapse(cells)));
    }

}
