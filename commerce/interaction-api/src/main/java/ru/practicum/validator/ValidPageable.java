package ru.practicum.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PageableConstraintValidator.class)
public @interface ValidPageable {
    String message() default "Invalid pagination parameters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minPage() default 0;

    int maxPage() default Integer.MAX_VALUE;

    int minSize() default 1;

    int maxSize() default 1000;
}
