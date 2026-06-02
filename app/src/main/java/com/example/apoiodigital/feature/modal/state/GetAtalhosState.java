package com.example.apoiodigital.feature.modal.state;

public class GetAtalhosState {

    private boolean isLoading;
    private String errorMessage;
    private boolean isSuccess;

    public GetAtalhosState(boolean isLoading, String errorMessage, boolean isSuccess) {
        this.isLoading = isLoading;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
