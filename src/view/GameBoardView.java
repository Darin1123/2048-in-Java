package view;

import model.Cell;
import model.GameBoard;

public class GameBoardView implements View {
    private GameBoard gameBoard;
    private char tableCharacter;
    private final char DEFAULT_TABLE_CHAR = '.';
    public GameBoardView(GameBoard gameBoard) {
        tableCharacter = DEFAULT_TABLE_CHAR;
        this.gameBoard = gameBoard;
    }

    public void setTableCharacter(char newChar) {
        this.tableCharacter = newChar;
    }


    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        Cell[][] data = this.gameBoard.getState();
        for (int i = 0; i < 4; i++) {
            builder.append(".............................\n");
//            builder.append(".      .      .      .      .\n");
            for (int j = 0; j < 4; j++) {
                builder.append(formatNumber(data[i][j]));
            }
            builder.append(".\n");
//            builder.append(".      .      .      .      .\n");
        }
        builder.append(".............................\n");
        if (this.tableCharacter != DEFAULT_TABLE_CHAR) {
            return builder.toString().replace(DEFAULT_TABLE_CHAR, tableCharacter);
        }
        return builder.toString();
    }

    private String formatNumber(Cell cell) {
        if (cell.getValue() == 0) {
            return ".      ";
        }
        int numOfDigits = String.valueOf(cell.getValue()).length();
        int numOfSpaces = 6 - numOfDigits;
        StringBuilder builder = new StringBuilder();
        builder.append(".");
        int halfSpace = numOfSpaces / 2;
        for (int i = 0; i < halfSpace; i++) {
            builder.append(" ");
        }
        builder.append(cell.getValue());
        for (int i = 0; i < (numOfSpaces - halfSpace); i++) {
            builder.append(" ");
        }
        return builder.toString();
    }
}
