package view;

public class ErrorView implements View {
    private String errorMessage;

    public ErrorView() {
        this.errorMessage = "Error!";
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String render() {
        return this.errorMessage;
    }
}
