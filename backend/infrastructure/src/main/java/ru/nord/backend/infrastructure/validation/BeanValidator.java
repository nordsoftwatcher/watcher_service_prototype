package ru.nord.backend.infrastructure.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.stream.Collectors;

public final class BeanValidator
{
    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static Collection<String> validate(Object o, Class... groups) {
        final javax.validation.Validator validator = factory.getValidator();
        return validator.validate(o, groups).stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
    }
}
