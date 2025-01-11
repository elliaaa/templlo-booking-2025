package com.templlo.service.program.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UpdateProgramScheduleRequestValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUpdateProgramScheduleRequest {
    String message() default "Invalid UpdateProgramScheduleRequest";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
