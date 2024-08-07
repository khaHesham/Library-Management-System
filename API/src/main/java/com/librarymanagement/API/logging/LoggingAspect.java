package com.librarymanagement.API.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.librarymanagement.API.service..*(..)) || execution(* com.librarymanagement.API.controller..*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Entering method: {} with arguments: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @Around("execution(* com.librarymanagement.API.service..*(..)) || execution(* com.librarymanagement.API.controller..*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("Method: {} executed in {} ms",
                    joinPoint.getSignature().toShortString(),
                    elapsedTime);
            return result;
        } catch (Throwable throwable) {
            logger.error("Exception in method: {} with cause: {}",
                    joinPoint.getSignature().toShortString(),
                    throwable.getCause());
            throw throwable;
        }
    }

    @AfterThrowing(pointcut = "execution(* com.librarymanagement.API.service..*(..)) || execution(* com.librarymanagement.API.controller..*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {} with cause: {}",
                joinPoint.getSignature().toShortString(),
                exception.getCause());
    }
}
