package com.foursquare.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before(value = "@annotation(LoggingInvocation)", argNames = "LoggingInvocation")
    public void methodInvocationLogging(JoinPoint joinPoint, LoggingInvocation loggingInvocation) {
        loggingInvocation.logLevel().executeLogging(joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName(), joinPoint.getArgs());
    }
}