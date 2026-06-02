package com.example.apoiodigital.feature.register;

public class RegisterState {


    private boolean isLoading;
    private String errorMessage;
    private boolean isSuccess;

    public RegisterState(boolean isLoading, String errorMessage, boolean isSuccess) {
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
