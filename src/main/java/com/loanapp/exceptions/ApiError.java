package com.loanapp.exceptions;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {

    private boolean success;
    private String message;
    private List<String> details;

    public ApiError(String message, List<String> details) {
        this.success = false;
        this.message = message;
        this.details = details;
    }
}
