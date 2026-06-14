package com.example.apoiodigital.feature.screen_question;

public class AnswerValidatorState {

    private boolean isLoading;
    private String error;
    private boolean isSuccess;

    public AnswerValidatorState(boolean isLoading, String error, boolean isSuccess) {
        this.isLoading = isLoading;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
