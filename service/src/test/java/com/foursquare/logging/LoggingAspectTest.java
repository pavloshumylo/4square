package com.foursquare.logging;

import org.aspectj.lang.JoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoggingAspectTest {

    @MockBean
    private LoggingAspect loggingAspect;

    private static class NestedTest {

        @LoggingInvocation(logLevel = LoggingLevels.INFO)
        public void invocationOfAspectTest() {
        }
    }

    @SpringBootApplication
    public static class TestConf {

        @Bean
        public LoggingAspectTest.NestedTest nestedTest() {
            return new LoggingAspectTest.NestedTest();
        }
    }

    @Autowired
    private NestedTest nestedTest;

    @Test
    public void testLoggingAspect() {
        nestedTest.invocationOfAspectTest();
        verify(loggingAspect, times(1)).methodInvocationLogging(any(JoinPoint.class),
                any(LoggingInvocation.class));
    }
}