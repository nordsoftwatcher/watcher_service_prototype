package ru.nord.backend.infrastructure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import ru.nord.backend.infrastructure.annotations.LogError;
import ru.nord.backend.infrastructure.annotations.Named;
import ru.nord.backend.infrastructure.utils.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Objects;

@Aspect
@Component
public class LogErrorAspect
{
    @AfterThrowing(pointcut = "@annotation(ru.nord.backend.infrastructure.annotations.LogError)", throwing = "ex")
    public void logError(JoinPoint joinPoint, Exception ex) {
        final MethodSignature method = (MethodSignature)joinPoint.getSignature();
        final LogError logErrorAnnotation = method.getMethod().getAnnotation(LogError.class);
        if(!logErrorAnnotation.type().isAssignableFrom(ex.getClass()))
            return;

        final Class clazz = method.getDeclaringType();
        final StringBuilder error = new StringBuilder();
        error.append(ex.getClass().getSimpleName()).append(" from:").append(System.lineSeparator());
        error.append(clazz.getCanonicalName()).append("::").append(method.getName()).append("(");

        final Object[] args = joinPoint.getArgs();
        if(args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                final Object arg = args[i];
                final Parameter parameter = method.getMethod().getParameters()[i];
                String parameterName = parameter.getName();
                if (StringUtils.isNullOrEmpty(parameterName)) {
                    if (parameter.isAnnotationPresent(Named.class)) {
                        parameterName = parameter.getAnnotation(Named.class).value();
                    }
                }
                if (StringUtils.isNullOrEmpty(parameterName)) {
                    parameterName = "{" + String.valueOf(i+1) + "}";
                }
                error.append(System.lineSeparator());
                error.append("    ").append(parameterName).append(" = ").append(Objects.toString(arg));
            }
            error.append(System.lineSeparator());
        }
        error.append(")");
        error.append(System.lineSeparator());

        LoggerFactory.getLogger(clazz).error(error.toString(), ex);
    }
}
