package com.rashmita.systemservice.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseUtility {

    public static Response getCreatedResponse(String message) {
        return Response.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public static Response getUpdatedResponse(String message) {
        return Response.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.ACCEPTED)
                .build();
    }

    public static Response getSuccessfulResponse(String message) {
        return Response.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public static Response getSuccessfulResponse(String message, String operationStatus) {
        return Response.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.OK)
                .operationStatus(operationStatus)
                .build();
    }

    public static Response getSuccessfulResponse(String message, Integer code) {
        return Response.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.OK)
                .code(code)
                .build();
    }

    public static Response getSuccessfulResponse(String message, String operationStatus, String id) {
        return Response.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.OK)
                .operationStatus(operationStatus)
                .id(id)
                .build();
    }
    public static Response getFailedResponse(Integer code, String message) {
        return Response.builder()
                .success(false)
                .code(code)
                .message(message)
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .build();
    }

    public static Response getFailedResponse(String message, String operationStatus) {
        return Response.builder()
                .message(message)
                .success(false)
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .operationStatus(operationStatus)
                .build();
    }

    public static Response getTimeoutResponse(String message, String operationStatus) {
        return Response.builder()
                .message(message)
                .httpStatus(HttpStatus.REQUEST_TIMEOUT)
                .operationStatus(operationStatus)
                .build();
    }

    public static Response getOtpSuccessfulResponse(String message, String id) {
        return Response.builder()
                .message(message)
                .id(id)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public static ServerResponse getSuccessfulServerResponse(String message) {
        return ServerResponse.builder()
                .success(true)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ServerResponse getSuccessfulServerResponse(Object data, String message) {
        return ServerResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ServerResponse<T> getSuccessfulServerResponseDTO(T data, String message) {
        ServerResponse<T> serverResponse = new ServerResponse<>();
        serverResponse.setSuccess(true);
        serverResponse.setMessage(message);
        serverResponse.setData(data);
        serverResponse.setHttpStatus(HttpStatus.OK);

        return serverResponse;
    }

    public static ServerResponse getFailedServerResponse(String message) {
        return ServerResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .build();
    }

    public static ServerResponse getServerFailedResponse(String message,String statusCode) {
        return ServerResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .statusCode(statusCode)
                .build();
    }

    public static ServerResponse getFailedServerResponse(String message, Object data) {
        return ServerResponse.builder()
                .success(false)
                .message(message)
                .data(data)
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .build();
    }

    public static ServerResponse getFailedServerResponse(String message, HttpStatus httpStatus) {
        return ServerResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }

    public static ServerResponse getCreatedServerResponse(String message) {
        return ServerResponse.builder()
                .success(true)
                .message(message)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public static ServerResponse getUpdatedServerResponse(String message) {
        return ServerResponse.builder()
                .success(true)
                .message(message)
                .httpStatus(HttpStatus.ACCEPTED)
                .build();
    }

    public static ServerResponse getNotFoundServerResponse(String message) {
        return ServerResponse.builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .message(message)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }
    public static ServerResponse getBadRequestServerResponse(String message) {
        return ServerResponse.builder()
                .success(false)
                .message(message)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    public static ServerResponse getTimeoutResponse(String message) {
        return ServerResponse.builder()
                .message(message)
                .httpStatus(HttpStatus.OK)
                .build();
    }

}
