package com.sprint.mission.discodeit.loggin;

import com.sprint.mission.discodeit.dto.common.Sanitizable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

  //각 케이스에 해당하는 유형 리스트
  private final List<ArgSanitizer> sanitizers;

  // Pointcut : Controller에서 GET 요청 빼고, CUD 관련 메서드들만
  @Pointcut("execution(public * com.sprint.mission.discodeit.controller..*(..))")
  public void controllerMethods() {
  }


  @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
  public void writeOperations() {
  }

  // Advice :
  @Around("controllerMethods() && writeOperations()")
  public Object logWriteOps(ProceedingJoinPoint pjp) throws Throwable {
    MethodSignature signature = (MethodSignature) pjp.getSignature();
    Method method = signature.getMethod();

    log.info("➡️ [{}] {}.{} 호출",
        getHttpMethod(method),
        signature.getDeclaringTypeName(),
        signature.getName()
    );

    // 파라미터 안전 로그
    log.info("📌 요청 파라미터: {}", sanitizeArgs(pjp.getArgs()));

    Object result = pjp.proceed();

    log.info("✅ 완료: {}.{}",
        signature.getDeclaringTypeName(),
        signature.getName()
    );

    return result;
  }

  //HTTP 메소드에 따라서 로그에 남길 단어 추가
  private String getHttpMethod(Method method) {
    if (method.isAnnotationPresent(PostMapping.class)) {
      return "POST";
    }
    if (method.isAnnotationPresent(PutMapping.class)) {
      return "PUT";
    }
    if (method.isAnnotationPresent(PatchMapping.class)) {
      return "PATCH";
    }
    if (method.isAnnotationPresent(DeleteMapping.class)) {
      return "DELETE";
    }
    return "UNKNOWN";
  }

  //매개변수 있는지 검사 해서 있으면 매개변수 처리 함수 호출
  private List<?> sanitizeArgs(Object[] args) {
    if (args == null || args.length == 0) {
      return List.of();
    }
    return Arrays.stream(args)
        .map(this::sanitizeArg)
        .toList();
  }

  //매개변수 처리 함수 : multipartfile, password 관련 필드 없애기
  private Object sanitizeArg(Object arg) {
    if (arg instanceof Sanitizable<?> s) {
      return s.toLoggingDTO();
    }
    for (ArgSanitizer sanitizer : sanitizers) {
      if (sanitizer.isFilterCase(arg)) {
        return sanitizer.sanitize(arg);
      }
    }

    return arg;
  }
}
