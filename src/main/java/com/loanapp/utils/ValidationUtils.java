package com.loanapp.utils;

import com.loanapp.exceptions.BadRequestException;

public class ValidationUtils {

    public static void isInvalid(boolean isInvalid, String message) {
        if (isInvalid) {
            throw new BadRequestException(message);
        }
    }
}
