package com.loanapp.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Frequency {

    WEEKLY ("weekly");

    private final String value;

    Frequency(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
