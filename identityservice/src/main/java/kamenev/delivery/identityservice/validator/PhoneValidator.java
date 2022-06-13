package kamenev.delivery.identityservice.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneNumberConstraint, String> {

    @Override
    public void initialize(PhoneNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String numbers, ConstraintValidatorContext constraintValidatorContext) {
        return numbers != null && numbers.matches("\\+\\d+") && numbers.length() == 12;
    }
}
