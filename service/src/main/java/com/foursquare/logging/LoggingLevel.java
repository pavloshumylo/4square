package com.foursquare.logging;

import org.slf4j.LoggerFactory;

import java.util.Arrays;

public enum LoggingLevel {

    TRACE {
        @Override
        void executeLogging(String className, String methodName, Object[] params) {
            LoggerFactory.getLogger(className).trace(buildLoggingMessage(className, methodName, params));
        }
    },
    DEBUG {
        @Override
        void executeLogging(String className, String methodName, Object[] params) {
            LoggerFactory.getLogger(className).debug(buildLoggingMessage(className, methodName, params));
        }
    },
    INFO {
        @Override
        void executeLogging(String className, String methodName, Object[] params) {
            LoggerFactory.getLogger(className).info(buildLoggingMessage(className, methodName, params));
        }
    },
    WARN {
        @Override
        void executeLogging(String className, String methodName, Object[] params) {
            LoggerFactory.getLogger(className).warn(buildLoggingMessage(className, methodName, params));
        }
    },
    ERROR {
        @Override
        void executeLogging(String className, String methodName, Object[] params) {
            LoggerFactory.getLogger(className).error(buildLoggingMessage(className, methodName, params));
        }
    };

    abstract void executeLogging(String className, String methodName, Object[] params);

    private static String buildLoggingMessage(String className, String methodName, Object[] params) {
        return "Method invocation. Class: " + className + ". Method: " + methodName
                + ". Params: " + Arrays.toString(params);
    }
}