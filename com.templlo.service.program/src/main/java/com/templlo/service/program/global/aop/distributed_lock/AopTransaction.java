package com.templlo.service.program.global.aop.distributed_lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AopTransaction {

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Object proceedWithTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
