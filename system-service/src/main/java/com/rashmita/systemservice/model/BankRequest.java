package com.rashmita.systemservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rashmita.systemservice.Aspect.UniqueBankCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BankRequest {
    @NotNull(message = "bank name can't be null")
    @UniqueBankCode(message="bank code must be unique")
    private String bankCode;

//    @NotNull(message="branch code is mandatary")
//    @Size(min=5,max=10)
//    @UniqueBankCode(message="bank code must be unique")
//    @Pattern(regexp = "^[A-Z]{2}[0-9]{4}$", message = "Must be 2 uppercase letters followed by 4 digits")
//    private String bankCode;

    @NotNull(message = "Established Date can't be null")
    @PastOrPresent(message = "Established Date can't be in future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/d")
    private Date establishedDate;

    @NotNull(message = "bank address can't be null")
    private String bankAddress;

    @NotNull(message = "fill the bankAdmin details")
    private BankAdminRequest bankAdminRequest;


    @Data
    public static class BankAdminRequest {
        private String adminName;
        private String gender;
        private String email;
        private String password;
        private String mobile;
    }

}
