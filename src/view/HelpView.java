package view;

public class HelpView implements View {
    @Override
    public String render() {
        return "- Enter [W], [A], [S], [D] to play the game.\n" +
                "- Enter [P] to view game board.\n" +
                "- Enter [R] to restart the game.\n" +
                "- Enter 'help' to display this page.\n" +
                "- Enter 'exit' to leave the game.\n";
    }
}
