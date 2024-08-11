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

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
