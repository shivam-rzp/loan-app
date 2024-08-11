package com.loanapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));

        ApiError error = new ApiError("Validation failed", errors.values().stream().collect(Collectors.toList()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError("Resource not found", List.of(ex.getMessage()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadException(ResourceNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError("invalid request", List.of(ex.getMessage()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
