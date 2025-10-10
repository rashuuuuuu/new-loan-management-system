package com.rashmita.common.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccessGroupModel {
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 55, message = "Name must be between {min} and {max} digits long")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 3, max = 200, message = "Description must be between {min} and {max} digits long")
    private String description;

    @Valid
    private List<AssignRoleModel> roles;

    @Valid
    private AssignGroupType accessGroupType;

}
