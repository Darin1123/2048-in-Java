package runner;

import controller.GameController;
import model.ErrorMessage;
import model.GameBoard;
import util.enumeration.Command;
import util.enumeration.Direction;
import view.ErrorView;
import view.GameBoardView;
import view.HelpView;
import view.WelcomeView;

import java.util.Scanner;

public class GameRunner {

    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void exit() {
        System.exit(1);
    }

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
    }

    public static void main(String[] args) {
        new GameRunner().run();
    }
}
