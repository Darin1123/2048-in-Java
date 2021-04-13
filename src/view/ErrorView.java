package view;

import model.ErrorMessage;

public class ErrorView implements View {
    private ErrorMessage errorMessage;

    public ErrorView(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.setMessage(errorMessage);
    }

    @Override
    public String render() {
        return this.errorMessage.getMessage();
    }
}
