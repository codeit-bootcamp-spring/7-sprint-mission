package com.sprint.mission.discodeit.transactional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//AOP Aspect 구현 : 트랜잭션 시작 - 커밋 - 롤백 흉내
@Aspect
@Component
public class CustomTransactionalAspect {
    //메서드 실행 전/후 예외 발생 시점까지 모두 제어
    @Around("@annotation(com.sprint.mission.discodeit.transactional.CustomTransactional)")
    public Object aroundTransactionalMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        TransactionContext context = new TransactionContext();
        TransactionManager.setContext(context);

        try{
            Object result = joinPoint.proceed();
            context.commit();
            return result;
        } catch (Throwable ex){
            context.rollback();
            throw ex;
        } finally {
            TransactionManager.clear();
        }
    }
}
