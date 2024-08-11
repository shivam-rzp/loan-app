package com.loanapp.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {

    FAILED ("failed"),
    PAID ("paid");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
