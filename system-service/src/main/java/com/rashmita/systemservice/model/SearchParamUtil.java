package com.rashmita.systemservice.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class SearchParamUtil {

    public static String getString(SearchParam searchParam, String keyName) {
        return (String) searchParam.getParam().get(keyName);
    }

    public static Date getDate(SearchParam searchParam, String keyName, String dateFormat) {
        String stringDate = getString(searchParam, keyName);
        if (stringDate != null && !stringDate.isBlank()) {
            Instant instant = Instant.parse(stringDate);

            Date date = Date.from(instant);

            return DateUtility.getDateFromDate(date, dateFormat);
        }
        return null;
    }

    public static Date getDateWithTime(SearchParam searchParam, String keyName, String dateFormat) {
        String stringDate = getString(searchParam, keyName);
        if (stringDate != null && !stringDate.isBlank()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                LocalDate localDate = LocalDate.parse(stringDate, formatter);
                LocalDateTime localDateTime;
                if ("endDate".equals(keyName)) {
                    localDateTime = localDate.atTime(23, 59, 59);
                } else {
                    localDateTime = localDate.atStartOfDay();
                }
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                Date date = Date.from(instant);
                return DateUtility.getDateFromDate(date, dateFormat);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format: " + stringDate, e);
            }
        }
        return null;
    }
}
