package com.rashmita.isoservice.Aspect;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueTransactionIdValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTransactionId {
    String message() default "Transaction is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}