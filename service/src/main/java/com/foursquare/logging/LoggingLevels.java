package com.foursquare.logging;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public enum LoggingLevels {

    TRACE {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LOG.trace("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    DEBUG {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LOG.debug("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    INFO {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LOG.info("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    WARN {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LOG.warn("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));;
        }
    },
    ERROR {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            LOG.error("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    };

    private static final Logger LOG = LoggerFactory.getLogger(LoggingLevels.class);
    abstract void loggingExecution(JoinPoint joinPoint);
}