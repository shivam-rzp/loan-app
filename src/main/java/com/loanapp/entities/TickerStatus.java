package com.loanapp.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TickerStatus {

    PENDING ("pending"),
    SUCCESS ("success"),
    FAILED ("failed");

    private final String value;

    TickerStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}