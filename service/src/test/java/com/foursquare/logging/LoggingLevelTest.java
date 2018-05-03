package com.foursquare.logging;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class LoggingLevelTest {

    @Mock
    private Logger logger;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(LoggerFactory.class);
        when(LoggerFactory.getLogger(anyString())).thenReturn(logger);
    }

    @Test
    public void testExecuteLogging_ShouldInvokeTraceLogger() {
        doNothing().when(logger).trace(anyString());

        LoggingLevel.TRACE.executeLogging("firstClass", "secondMethod", new Object[2]);
        verify(logger).trace("Method invocation. Class: firstClass. Method: secondMethod. Params: [null, null]");
    }

    @Test
    public void testExecuteLogging_ShouldInvokeDebugLogger() {
        doNothing().when(logger).debug(anyString());

        LoggingLevel.DEBUG.executeLogging("firstClass", "secondMethod", new Object[2]);
        verify(logger).debug("Method invocation. Class: firstClass. Method: secondMethod. Params: [null, null]");
    }

    @Test
    public void testExecuteLogging_ShouldInvokeInfoLogger() {
        doNothing().when(logger).info(anyString());

        LoggingLevel.INFO.executeLogging("firstClass", "secondMethod", new Object[2]);
        verify(logger).info("Method invocation. Class: firstClass. Method: secondMethod. Params: [null, null]");
    }

    @Test
    public void testExecuteLogging_ShouldInvokeWarnLogger() {
        doNothing().when(logger).warn(anyString());

        LoggingLevel.WARN.executeLogging("firstClass", "secondMethod", new Object[2]);
        verify(logger).warn("Method invocation. Class: firstClass. Method: secondMethod. Params: [null, null]");
    }

    @Test
    public void testExecuteLogging_ShouldInvokeErrorLogger() {
        doNothing().when(logger).error(anyString());

        LoggingLevel.ERROR.executeLogging("firstClass", "secondMethod", new Object[2]);
        verify(logger).error("Method invocation. Class: firstClass. Method: secondMethod. Params: [null, null]");
    }
}