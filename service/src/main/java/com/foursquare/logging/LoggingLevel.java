package com.foursquare.logging;

import org.aspectj.lang.JoinPoint;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public enum LoggingLevel {

    TRACE {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LoggerFactory.getLogger(joinPoint.getTarget().getClass()).trace("Method invocation. Class: " + joinPoint
                    .getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    DEBUG {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LoggerFactory.getLogger(joinPoint.getTarget().getClass()).debug("Method invocation. Class: " + joinPoint
                    .getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    INFO {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LoggerFactory.getLogger(joinPoint.getTarget().getClass()).info("Method invocation. Class: " + joinPoint
                    .getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    WARN {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LoggerFactory.getLogger(joinPoint.getTarget().getClass()).warn("Method invocation. Class: " + joinPoint
                    .getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));;
        }
    },
    ERROR {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LoggerFactory.getLogger(joinPoint.getTarget().getClass()).error("Method invocation. Class: " + joinPoint
                    .getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    };

    abstract void loggingExecution(JoinPoint joinPoint);
}