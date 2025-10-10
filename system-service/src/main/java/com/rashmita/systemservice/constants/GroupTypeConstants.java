package com.rashmita.systemservice.constants;

import lombok.Getter;

@Getter
public enum GroupTypeConstants {
    SYSTEM(1),
    BANK(2),
    CUSTOMER(3);

    private final int id;

    GroupTypeConstants(int id) {
        this.id = id;
    }

    // Optional helper method
    public String getName() {
        return this.name(); // default enum name
    }

    public String toUpperCase() {
        return this.name().toUpperCase();
    }
}
