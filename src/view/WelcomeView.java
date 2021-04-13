package view;

/**
 * @author Zefeng Wang - wangz217
 * @brief Welcome View Module
 */

public class WelcomeView implements View {

    @Override
    public String render() {
        return  " ____  _____  _   _  _____\n" +
                "| _  ||  _  || |_| ||  _  |\n" +
                "  / / | |_| || ___ ||  _  |\n" +
                "|____||_____|    |_||_____|\n" +
                "\n" +
                "Welcome to game 2048!\n" +
                "- Press key [W], [A], [S], [D] and then [Enter] to play.\n" +
                "- Enter 'help' to read game instructions.\n";
    }
}
