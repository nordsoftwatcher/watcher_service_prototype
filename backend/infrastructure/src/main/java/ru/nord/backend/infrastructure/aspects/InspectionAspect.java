package ru.nord.backend.infrastructure.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.nord.backend.infrastructure.utils.Stopwatch;

import java.time.Duration;

@Aspect
@Component @Profile("inspection")
public class InspectionAspect
{
    private static final Logger log = LoggerFactory.getLogger(InspectionAspect.class);

    @Around("@annotation(ru.nord.backend.infrastructure.annotations.Inspect))")
    public Object doInspection(ProceedingJoinPoint joinPoint) throws Throwable
    {
        final Signature signature = joinPoint.getSignature();
        final String name = signature.getDeclaringTypeName()+"::"+signature.getName();
        log.info("Entering "+name);
        final Stopwatch stopwatch = Stopwatch.start();
        boolean success = false;
        try {
            final Object result = joinPoint.proceed();
            success = true;
            return result;
        }
        finally {
            final Duration elapsed = stopwatch.elapsed();
            log.info((success ? "Exited " : "Failed ") + name + " after " + elapsed.toMillis() + "ms");
        }
    }
}
