package com.foursquare.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class LoggingAspectTest {

    @Mock
    private JoinPoint joinPoint;
    @Mock
    private LoggingInvocation loggingInvocation;
    @Mock
    private Signature signature;
    @Mock
    private LoggingLevel level;

    @Test
    public void testMethodInvocationLogging_ShouldInvokedOneTime() {
        MockitoAnnotations.initMocks(this);

        when(loggingInvocation.logLevel()).thenReturn(level);
        doNothing().when(level).executeLogging(anyString(), anyString(), any(Object[].class));
        when(joinPoint.getTarget()).thenReturn(new LoggingAspectTest());
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("methodName");
        when(joinPoint.getArgs()).thenReturn(new Object[] {"arg"});

        new LoggingAspect().methodInvocationLogging(joinPoint, loggingInvocation);
        verify(level).executeLogging("com.foursquare.logging.LoggingAspectTest",
                "methodName", new Object[] {"arg"});
    }
}