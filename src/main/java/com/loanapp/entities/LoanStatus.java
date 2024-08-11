package com.loanapp.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LoanStatus {

    PENDING ("pending"),
    APPROVED ("approved"),

    PAID ("paid"),

    REJECTED ("rejected");


    private final String value;

    LoanStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
