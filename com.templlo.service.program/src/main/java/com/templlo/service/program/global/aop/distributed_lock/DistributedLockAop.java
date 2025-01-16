package com.templlo.service.program.global.aop.distributed_lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {
    private static final String LOCK_PREFIX = "lock:";

    private final RedissonClient redissonClient;
    private final AopTransaction aopTransaction;

    /**
     * DistributedLock 어노테이션이 달린 메서드의 작업 전/후로 redis 분산락을 적용
     */
    @Around("@annotation(com.templlo.service.program.global.aop.distributed_lock.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DistributedLock lockAnnotation = signature.getMethod().getAnnotation(DistributedLock.class);

        String key = getKey(joinPoint, lockAnnotation, signature);
        RLock rLock = redissonClient.getLock(key);
        try{
            if (!isAvailable(rLock, lockAnnotation)) {
                return false;
            }
            return aopTransaction.proceedWithTransaction(joinPoint);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 락을 적용할 키 생성
     */
    private String getKey(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation, MethodSignature signature) {
        return LOCK_PREFIX + lockAnnotation.keyType().getKeyName() + CustomSpELParser.getDynamicValue(lockAnnotation.idSpEL(), joinPoint, signature);
    }

    /**
     * 락 획득 시도
     */
    private boolean isAvailable(RLock rLock, DistributedLock lockAnnotation) throws InterruptedException {
        return rLock.tryLock(lockAnnotation.maxWaitTime(), lockAnnotation.maxLeaseTime(), TimeUnit.SECONDS);
    }
}
