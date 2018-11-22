package ru.nord.siwatch.backend.facade.operator.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.nord.siwatch.backend.facade.operator.api.v1.dto.Fault;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice(assignableTypes = ApiBase.class)
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler
{
    @Autowired
    private ObjectMapper json;

    private String getJsonPropertyName(String name) {
        PropertyNamingStrategy namingStrategy = json.getDeserializationConfig().getPropertyNamingStrategy();
        if(namingStrategy instanceof PropertyNamingStrategy.PropertyNamingStrategyBase) {
            return ((PropertyNamingStrategy.PropertyNamingStrategyBase) namingStrategy).translate(name);
        }
        return name;
    }

//    @ExceptionHandler({AuthenticationException.class})
//    public ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request)
//    {
//        final Fault error = new Fault("S000", "Доступ запрещен", ex.toString());
//        log.error(error.toString(), ex);
//        return new ResponseEntity<>(Collections.singletonList(error), HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler({AccessDeniedException.class})
//    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request)
//    {
//        final Fault error = new Fault("S000", "Доступ запрещен", ex.toString());
//        log.error(error.toString(), ex);
//        return new ResponseEntity<>(Collections.singletonList(error), HttpStatus.FORBIDDEN);
//    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        String message = "Указано некорректное значение";
        if(ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException e = (MethodArgumentTypeMismatchException)ex;
            message = "Указано некорректное значение параметра "+e.getName();
        }
        final Fault error = new Fault("V000", message, ex.toString());
        log.error(error.toString(), ex);

        return new ResponseEntity<>(Collections.singletonList(error), HttpStatus.BAD_REQUEST);
    }

    private List<Fault> mapBindErrors(BindingResult bindingResult)
    {
        final List<Fault> errors = new ArrayList<Fault>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.add(new Fault("V000", getJsonPropertyName(error.getField()), error.getDefaultMessage()));
        }
        for (ObjectError error : bindingResult.getGlobalErrors()) {
            errors.add(new Fault("V000", error.getObjectName(), error.getDefaultMessage()));
        }
        return errors;
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return new ResponseEntity<>(mapBindErrors(ex.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        final Fault fault = new Fault("E000", ex.getLocalizedMessage(), ex.toString());
        return super.handleExceptionInternal(ex, Collections.singletonList(fault), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        final Fault fault = new Fault(
            "V001",
            ex.getMostSpecificCause().getClass().getSimpleName(),
            ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()
        );
        return new ResponseEntity<>(Collections.singletonList(fault), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return new ResponseEntity<>(mapBindErrors(ex.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, WebRequest request)
    {
        final List<Fault> errors = new ArrayList<Fault>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new Fault("V000", getJsonPropertyName(violation.getPropertyPath().toString()), violation.getMessage()));
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler({BusinessException.class})
//    public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request)
//    {
//        final Fault error = new Fault(ex.getCode(), ex.getMessage(), ex.getDetails());
//        log.error(error.toString(), ex);
//        return new ResponseEntity<>(Collections.singletonList(error), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleAnyException(Exception ex, WebRequest request)
    {
        final Fault error = new Fault("E000", "Произошла внутренняя ошибка", ex.toString());
        log.error(error.toString(), ex);
        if (ex instanceof HttpClientErrorException) {
            log.error("ERROR REQUEST BODY " + ((HttpClientErrorException) ex).getResponseBodyAsString());
        }
        return new ResponseEntity<>(Collections.singletonList(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
