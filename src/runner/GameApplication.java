package runner;

import controller.GameController;
import model.GameBoard;
import view.ErrorView;
import view.GameBoardView;
import view.HelpView;
import view.WelcomeView;

import java.util.Scanner;

public class GameApplication {
    public static void main(String[] args) {
        // initialize beans
        GameBoard gameBoard = new GameBoard();
        GameBoardView gameBoardView = new GameBoardView(gameBoard);
        HelpView helpView = new HelpView();
        ErrorView errorView = new ErrorView();
        WelcomeView welcomeView = new WelcomeView();
        GameController gameController = new GameController(
                gameBoard, gameBoardView, helpView, errorView, welcomeView);

        // configuration
        gameBoardView.setTableCharacter('.');

        // loop forever
        System.out.println(welcomeView.render());
        System.out.println(gameBoardView.render());
        Scanner keyboardInputScanner = new Scanner(System.in);
        while (!gameController.checkGameOver()) {
            System.out.print(">> ");
            System.out.println(gameController.process(keyboardInputScanner.next()));
        }
    }
}
