package com.rashmita.systemservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAccessGroupResponse extends ModelBase {
    private String name;
    private String description;
    private StatusDto status;
}
