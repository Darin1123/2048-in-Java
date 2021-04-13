package model;

public class ErrorMessage {
    private String message;

    public ErrorMessage() {
        message = "Error!";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
