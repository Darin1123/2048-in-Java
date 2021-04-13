package model;

/**
 * @author Zefeng Wang - wangz217
 * @brief Error Message View Module
 */

public class ErrorMessage {
    private String message;

    /**
     * @brief Constructor
     */
    public ErrorMessage() {
        message = "Error!";
    }

    /**
     * @brief Get the message
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @brief set the message
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
