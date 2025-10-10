package com.rashmita.common.model;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnblockAccessGroupRequest extends ModelBase {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Remarks cannot be blank")
    private String remarks;
}
