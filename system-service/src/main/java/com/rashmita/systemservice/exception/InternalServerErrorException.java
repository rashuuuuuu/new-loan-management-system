package com.rashmita.systemservice.exception;


public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException() {
        super();
    }
    public InternalServerErrorException(String message) {
        super(message);
    }
}
