package runner;

import controller.GameController;
import model.ErrorMessage;
import model.GameBoard;
import util.enumeration.Command;
import util.enumeration.Direction;
import view.*;

import java.util.Scanner;

/**
 * @author Zefeng Wang - wangz217
 * @brief Game Runner Module
 */

public class GameRunner {

    private GameController gameController;

    /**
     * @brief set the game controller
     * @param gameController the game controller
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * @brief exit the game
     */
    public void exit() {
        System.exit(1);
    }

    /**
     * @brief process the input
     * @param input the input
     * @return the result
     */
    public String process(String input) {
        Command command;
        try {
            command = Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return gameController.displayError(
                    "Unknown command '" + input + "', enter 'help' to see how to play the game."
            );
        }

        // process the input
        if (command == Command.EXIT) {
            System.out.println("See you around!");
            exit();
        }

        if (command == Command.P) {
            return gameController.displayGameBoard();
        }

        if (command == Command.HELP) {
            return gameController.displayHelp();
        }

        if (command == Command.R) {
            return gameController.reset();
        }

        if (command == Command.A) {
            return gameController.collapse(Direction.LEFT);
        }

        if (command == Command.D) {
            return gameController.collapse(Direction.RIGHT);
        }

        if (command == Command.W) {
            return gameController.collapse(Direction.UP);
        }

        if (command == Command.S) {
            return gameController.collapse(Direction.DOWN);
        }

        return gameController.displayWelcome();

    }

    /**
     * @brief the main run code here
     */
    public void run() {
        // initialize beans
        ErrorMessage errorMessage = new ErrorMessage();
        GameBoard gameBoard = new GameBoard();
        ViewRegistry viewRegistry = new ViewRegistry();
        WelcomeView welcomeView = new WelcomeView();
        GameBoardView gameBoardView = new GameBoardView(gameBoard);

        viewRegistry.register("board", gameBoardView)
                .register("help", new HelpView())
                .register("error", new ErrorView(errorMessage))
                .register("welcome", welcomeView);

        GameController gameController = new GameController(
                gameBoard, errorMessage, viewRegistry);
        this.setGameController(gameController);

        // configuration
        gameBoardView.setTableCharacter('.');

        System.out.println(welcomeView.render());
        System.out.println(gameBoardView.render());
        Scanner keyboardInputScanner = new Scanner(System.in);
        // loop forever
        while (!gameController.checkGameOver()) {
            System.out.print(">> ");
            System.out.println(this.process(keyboardInputScanner.next()));
        }
        System.out.println(((View) () -> "Game Over!\n").render());
        while (true) {
            System.out.println(((View) () -> "Press [R] to restart, 'exit' to quit.\n").render());
            System.out.print(">> ");
            String input = keyboardInputScanner.next();
            if (input.equalsIgnoreCase("R")) {
                this.run();
                break;
            } else if (input.equalsIgnoreCase("exit")) {
                System.out.println(((View) () -> "Bye!\n").render());
                break;
            }
        }
    }

    public static void main(String[] args) {
        new GameRunner().run();
    }
}
