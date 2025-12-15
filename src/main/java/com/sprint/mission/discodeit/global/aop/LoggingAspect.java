package com.sprint.mission.discodeit.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(public * com.sprint.mission.discodeit.controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint, "CONTROLLER", true);
    }

    @Around("execution(public * com.sprint.mission.discodeit.service..*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint, "SERVICE", false);
    }

    private Object logExecution(ProceedingJoinPoint joinPoint, String layer, boolean isInfo)
            throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName()
                .substring(signature.getDeclaringTypeName().lastIndexOf('.') + 1);
        String methodName = signature.getName();

        Object[] args = joinPoint.getArgs();

        if (isInfo)
            log.info("[{}] {}.{}() Request: {}", layer, className, methodName, Arrays.toString(args));
        else
            log.debug("[{}] {}.{}() Request: {}", layer, className, methodName, Arrays.toString(args));


        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            if (isInfo)
                log.info("[{}] {}.{}() Response: {} ({} ms)", layer, className, methodName, formatResult(result), duration);
            else
                log.debug("[{}] {}.{}() Response: {} ({} ms)", layer, className, methodName, formatResult(result), duration);

            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.warn("[{}] {}.{}() Exception: {} ({} ms)",
                    layer, className, methodName, e.getMessage(), duration);
            throw e;
        }
    }

    private String formatResult(Object result) {
        if (result == null) return "null";
        String str = result.toString();
        return str.length() > 500 ? str.substring(0, 500) + "..." : str;
    }
}