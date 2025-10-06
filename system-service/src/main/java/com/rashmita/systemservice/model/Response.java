package com.rashmita.systemservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String id;

    private String message;

    private HttpStatus httpStatus;

    private String operationStatus;

    private Boolean success;

    private Integer code;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
