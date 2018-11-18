package ru.nord.siwatch.backend.facade.device.api.v1;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice(assignableTypes = ApiBase.class)
public class ApiConfig
{
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }
}
