package com.sprint.mission.discodeit.global.aop;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Around("execution(public * com.sprint.mission.discodeit.controller..*(..))")
  public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {

    long start = System.currentTimeMillis();
    // MethodSignature : Signature 하위 인터페이스, 조인포인트가 메서드일 때 사용
    // Signature 실행되는 메서드의 정보 제공, 리턴, 파라미터타입, 이름 등
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String className = signature.getDeclaringTypeName()
        .substring(signature.getDeclaringTypeName().lastIndexOf('.') + 1);
    String methodName = signature.getName();

    // Request 값 (파라미터)
    Object[] args = joinPoint.getArgs();
    log.info("{}.{}() Request: {}",
        className,
        methodName,
        Arrays.toString(args));

    Object result = null;
    try {
      result = joinPoint.proceed(); // 실제 컨트롤러 실행
      long duration = System.currentTimeMillis() - start; // 성능 측정

      // Response 값
      log.info("{}.{}() Response: {} ({} ms)",
          className,
          methodName,
          formatResult(result),
          duration);

      return result;
    } catch (Exception e) {
      long duration = System.currentTimeMillis() - start;
      log.error("{}.{}() Exception: {} ({} ms)",
          className,
          methodName,
          e.getMessage(),
          duration,
          e);
      throw e;
    }
  }

  private String formatResult(Object result) {
    if (result == null) {
      return "null";
    }
    String str = result.toString();
    return str.length() > 500 ? str.substring(0, 500) + "..." : str;
  }

}
