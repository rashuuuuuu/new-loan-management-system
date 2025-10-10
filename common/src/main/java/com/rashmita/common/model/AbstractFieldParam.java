package com.rashmita.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractFieldParam extends ModelBase {

    protected String fieldKey;

    protected String fieldOperator;
}