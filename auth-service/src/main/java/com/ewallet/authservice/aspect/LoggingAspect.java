package com.ewallet.authservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    @Pointcut("within(com.ewallet.authservice.service..*)")
    public void serviceLayer() {}


    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[AOP] Calling: {} with args: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }


    @Around("serviceLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("[AOP] {} executed in {}ms",
                joinPoint.getSignature().toShortString(), duration);
        return result;
    }


    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        log.error("[AOP] Exception in {}: {}",
                joinPoint.getSignature().toShortString(),
                exception.getMessage());
    }
}