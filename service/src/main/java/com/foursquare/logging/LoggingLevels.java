package com.foursquare.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;

public enum LoggingLevels {

    TRACE {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            log.trace("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    DEBUG {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            log.debug("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    INFO {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            log.info("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    WARN {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            log.warn("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));;
        }
    },
    ERROR {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            log.error("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    },
    FATAL {
        @Override
        void loggingExecution(JoinPoint joinPoint) {
            log.fatal("Class: " + joinPoint.getTarget().getClass().getName() + ". Method: " + joinPoint.getSignature()
                    .getName() + ". Params: " + Arrays.toString(joinPoint.getArgs()));
        }
    };

    private static final Log log = LogFactory.getLog(LoggingLevels.class);
    abstract void loggingExecution(JoinPoint joinPoint);
}