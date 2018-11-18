package ru.nord.siwatch.backend.services.common.domain;

import lombok.Getter;

public class BusinessException extends RuntimeException
{
    @Getter
    private final String code;
    @Getter
    private final String details;

    public BusinessException(String code, String message) {
        this(code, message, null);
    }

    public BusinessException(String code, String message, String details) {
        super(message);
        this.code = code;
        this.details = details;
    }
}
