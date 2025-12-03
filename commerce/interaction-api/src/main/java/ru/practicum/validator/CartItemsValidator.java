package ru.practicum.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.UUID;

public class CartItemsValidator implements ConstraintValidator<ValidCartItems, Map<UUID, Long>> {
    @Override
    public boolean isValid(Map<UUID, Long> items, ConstraintValidatorContext context) {
        if (items == null || items.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Items cannot be empty")
                    .addConstraintViolation();
            return false;
        }

        boolean valid = true;
        for (Map.Entry<UUID, Long> entry : items.entrySet()) {
            if (entry.getValue() == null || entry.getValue() < 1) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Quantity must be at least 1 for product: " + entry.getKey())
                        .addConstraintViolation();
                valid = false;
            }
        }
        return valid;
    }
}
