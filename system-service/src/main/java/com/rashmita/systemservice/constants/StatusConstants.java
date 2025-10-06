package com.rashmita.systemservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusConstants {
    CREATED("CREATED", 1),
    UPDATED("UPDATED", 2),
    DELETED("DELETED", 3),
    BLOCKED("BLOCKED",4),
    UNBLOCKED("UNBLOCKED",5),
    ACTIVE("ACTIVE",6);
    private final String name;
    private final int id;
}

