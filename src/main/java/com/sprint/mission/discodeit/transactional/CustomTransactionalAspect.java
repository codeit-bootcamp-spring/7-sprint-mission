package com.sprint.mission.discodeit.transactional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//AOP Aspect 구현 : 트랜잭션 시작 - 커밋 - 롤백 흉내
/*
CustomTransaction 이 붙은 클래스의 프록시 객체를 생성(AOP)하여 try-catch-finally 문에서 트랜잭션을 제어한다.
try 문에서는 기존 로직을 수행하고 성공하면 commit 한다.
실패하면 catch 문에서 rollback 한다.
finally 에서는 ThreadLocal을 비워준다.
*/
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
