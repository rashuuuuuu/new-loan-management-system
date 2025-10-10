package com.rashmita.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
    private String name;
    private String description;
    private String icon;
}