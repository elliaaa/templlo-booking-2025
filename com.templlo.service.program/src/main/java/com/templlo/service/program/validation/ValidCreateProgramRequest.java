package com.templlo.service.program.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CreateProgramRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCreateProgramRequest {
    String message() default "Invalid CreateProgramRequest";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}