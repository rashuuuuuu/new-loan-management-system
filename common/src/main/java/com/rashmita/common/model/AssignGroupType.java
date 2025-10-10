package com.rashmita.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignGroupType extends ModelBase {
    @NotNull(message = "Group is required.")
    private Long groupTypeId;
}
