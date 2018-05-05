package com.foursquare.logging;

import org.aspectj.lang.JoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class LoggingAspectIntegrationTest {

    private static class LoggingAspectNested {

        @LoggingInvocation(logLevel = LoggingLevel.INFO)
        public void invocationOfAspect() {
        }
    }

    @SpringBootApplication
    public static class ConfigNested {

        @Bean
        public LoggingAspectNested loggingAspectNested() {
            return new LoggingAspectNested();
        }
    }

    @MockBean
    private LoggingAspect loggingAspect;

    @Autowired
    private LoggingAspectNested loggingAspectNested;

    @Test
    public void testLoggingAspect() {
        loggingAspectNested.invocationOfAspect();
        verify(loggingAspect, times(1)).methodInvocationLogging(any(JoinPoint.class),
                any(LoggingInvocation.class));
    }
}