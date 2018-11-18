package ru.nord.siwatch.backend.services.common.domain;

public enum BusinessError
{
    BE000 ("General error"),
    ;

    private String message;

    public String getMessage()
    {
        return message;
    }

    BusinessError(String message)
    {
        this.message = message;
    }
}
