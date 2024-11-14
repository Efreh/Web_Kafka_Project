package com.efr.OrderProcessingService.aspect.logger;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.efr.OrderProcessingService..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Executing method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.efr.OrderProcessingService..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method executed: {} with result: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.efr.OrderProcessingService..*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("Exception in method: {} with message: {}", joinPoint.getSignature(), error.getMessage());
    }

}
