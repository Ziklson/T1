package com.kuvarin.taskcrud.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.AfterReturning;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Aspect
@Component
public class TaskLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(TaskLoggingAspect.class);

    @Before("@annotation(com.kuvarin.taskcrud.aspect.annotation.LogException)")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing method {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "@annotation(com.kuvarin.taskcrud.aspect.annotation.LogException)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Handling result in method {}", joinPoint.getSignature().getName());
        logger.info("Method executed successfully. Result: {}", result);
    }

    @AfterThrowing(pointcut = "@annotation(com.kuvarin.taskcrud.aspect.annotation.LogException)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.error("Exception occurred in method: {}. Exception is \"{}\"", joinPoint.getSignature().getName(), exception.getMessage(), exception);
    }

    @Around("@annotation(com.kuvarin.taskcrud.aspect.annotation.LogException)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("Method {} executed in {} ms", joinPoint.getSignature().getName(), (endTime - startTime));
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            logger.error("Method {} failed after {} ms", joinPoint.getSignature().getName(), (endTime - startTime));
            throw throwable;
        }
    }

}
