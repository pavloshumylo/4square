package com.foursquare.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoggingAspectTest {

    @Autowired
    private LoggingAspect loggingAspect;

    @Mock
    private JoinPoint joinPoint;
    @Mock
    private LoggingInvocation loggingInvocation;
    @Mock
    private Signature signature;
    @Mock
    private Object object;
    @Mock
    private LoggingLevel level;

    @Test
    public void testMethodInvocationLogging_ShouldInvokedOneTime() {
        when(loggingInvocation.logLevel()).thenReturn(level);
        doNothing().when(level).executeLogging(anyString(), anyString(), any(Object[].class));
        when(joinPoint.getTarget()).thenReturn(object);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("Name");
        when(joinPoint.getArgs()).thenReturn(new Object[4]);

        loggingAspect.methodInvocationLogging(joinPoint, loggingInvocation);
        verify(level, times(1)).executeLogging(anyString(), anyString(), any(Object[].class));
    }
}