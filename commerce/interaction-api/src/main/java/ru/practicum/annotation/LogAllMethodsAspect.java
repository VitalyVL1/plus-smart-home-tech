package ru.practicum.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class LogAllMethodsAspect {
    @Around("@within(ru.practicum.annotation.LogAllMethods)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName;

        long startTime = System.currentTimeMillis();

        Object[] args = joinPoint.getArgs();

        log.info("[{}] Starting execution", fullMethodName);
        log.info("Arguments: {}", args);

        try {
            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            log.info("Exiting method [{}] with result {}", fullMethodName, result);
            log.info("Execution time: {} ms", executionTime);

            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            log.error("Exception in method [{}] after {} ms", fullMethodName, executionTime, throwable);
            throw throwable;
        }
    }
}
