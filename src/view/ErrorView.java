package view;

import model.ErrorMessage;

/**
 * @author Zefeng Wang - wangz217
 * @brief Error View Module
 */

public class ErrorView implements View {
    private final ErrorMessage errorMessage;

    /**
     * @brief Constructor
     * @param errorMessage error message object (model)
     */
    public ErrorView(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @brief update error message
     * @param errorMessage the new message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage.setMessage(errorMessage);
    }

    @Override
    public String render() {
        return this.errorMessage.getMessage();
    }
}
