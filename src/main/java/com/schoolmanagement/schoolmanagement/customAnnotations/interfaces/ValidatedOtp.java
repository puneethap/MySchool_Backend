package com.schoolmanagement.schoolmanagement.customAnnotations.interfaces;

import com.schoolmanagement.schoolmanagement.customAnnotations.classes.ValidatedOtpValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatedOtpValidator.class)
public @interface ValidatedOtp {
    public String message() default "OTP is not validated";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
