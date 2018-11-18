package ru.nord.siwatch.backend.services.common.api;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public abstract class ApiBase
{
    public static final String PATH = "/api/";

    @Autowired
    private Validator validator;

    protected void ensureValid(Object o, Class... groups) throws ConstraintViolationException
    {
        final Set<ConstraintViolation<Object>> violations = validator.validate(o, groups);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
