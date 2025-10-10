package com.rashmita.systemservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class RoleGroupMapRequest implements Serializable {
    @NotNull(message = "Id cannot be null")
    private Long mapId;

    @NotNull(message = "Active status caanot be null")
    private Boolean isActive;
}
