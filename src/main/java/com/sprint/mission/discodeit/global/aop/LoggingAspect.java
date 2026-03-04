package com.sprint.mission.discodeit.global.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    @Around("@within(org.springframework.stereotype.Service)")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint, "SERVICE");
    }

    private Object logExecution(ProceedingJoinPoint joinPoint, String layer)
            throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName()
                .substring(signature.getDeclaringTypeName().lastIndexOf('.') + 1);
        String methodName = signature.getName();

        Object[] args = joinPoint.getArgs();

        log.info("[{}] {}.{}() Request: {}", layer, className, methodName, safeSerialize(args));

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("[{}] {}.{}() Response: {} ({} ms)", layer, className, methodName, safeSerialize(result), duration);
            return result;
        } catch (Throwable e) {
            long duration = System.currentTimeMillis() - start;
            log.warn("[{}] {}.{}() Exception ({} ms)", layer, className, methodName, duration, e);
            throw e;
        }
    }

    private String safeSerialize(Object obj) {
        if (obj == null) return "null";
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "[Serialization failed: " + e.getMessage() + "]";
        }
    }
}