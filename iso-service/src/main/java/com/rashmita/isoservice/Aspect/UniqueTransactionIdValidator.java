package com.rashmita.isoservice.Aspect;
import com.rashmita.isoservice.repository.TransactionDetailRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueTransactionIdValidator implements ConstraintValidator<UniqueTransactionId, String> {

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Override
    public boolean isValid(String transactionId, ConstraintValidatorContext context) {
        if (transactionId == null) return true; // handle @NotNull separately if needed
        return !transactionDetailRepository.existsByTransactionId(transactionId);
    }
}
