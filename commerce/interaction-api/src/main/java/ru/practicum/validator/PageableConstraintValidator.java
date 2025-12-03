package ru.practicum.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;

public class PageableConstraintValidator implements ConstraintValidator<ValidPageable, Pageable> {

    private int minPage;
    private int maxPage;
    private int minSize;
    private int maxSize;

    @Override
    public void initialize(ValidPageable constraintAnnotation) {
        this.minPage = constraintAnnotation.minPage();
        this.maxPage = constraintAnnotation.maxPage();
        this.minSize = constraintAnnotation.minSize();
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(Pageable value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean valid = true;

        if (value.getPageNumber() < minPage) {
            addViolation(context, "page",
                    String.format("Page number must be >= %d", minPage));
            valid = false;
        }

        if (value.getPageNumber() > maxPage) {
            addViolation(context, "page",
                    String.format("Page number must be <= %d", maxPage));
            valid = false;
        }

        if (value.getPageSize() < minSize) {
            addViolation(context, "size",
                    String.format("Page size must be >= %d", minSize));
            valid = false;
        }

        if (value.getPageSize() > maxSize) {
            addViolation(context, "size",
                    String.format("Page size must be <= %d", maxSize));
            valid = false;
        }

        return valid;
    }

    private void addViolation(ConstraintValidatorContext context,
                              String property, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}
