package com.rashmita.systemservice.validation;
import com.rashmita.systemservice.Aspect.UniqueBankCode;
import com.rashmita.systemservice.repository.BankRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueBankCodeValidator implements ConstraintValidator<UniqueBankCode, String> {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public boolean isValid(String bankCode, ConstraintValidatorContext context) {
        if (bankCode == null) return true; // handle @NotNull separately if needed
        return !bankRepository.existsByBankCode(bankCode);
    }
}