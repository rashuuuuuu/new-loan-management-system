package com.rashmita.commoncommon.model;

import java.util.Map;

public record TokenPayload(Map<String, Object> claims, String customerNumber, String bankCode, Purpose purpose, State state) {
}