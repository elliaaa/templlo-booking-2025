package com.templlo.service.program.global.aop.distributed_lock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.program.exception.ProgramException;
import com.templlo.service.program.exception.ProgramStatusCode;
import com.templlo.service.program.kafka.message.reservation.ReservationCreateMessage;
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
    private final ObjectMapper objectMapper;
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
    private String getKey(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation, MethodSignature signature) throws JsonProcessingException {
        return LOCK_PREFIX + lockAnnotation.keyType() + getProgramId(joinPoint, signature);
    }

    /**
     * 락 획득 시도
     */
    private boolean isAvailable(RLock rLock, DistributedLock lockAnnotation) throws InterruptedException {
        return rLock.tryLock(lockAnnotation.maxWaitTime(), lockAnnotation.maxLeaseTime(), TimeUnit.SECONDS);
    }

    /**
     * 작업 대상 메서드의 인자로부터 programId 값을 추출
     */
    private String getProgramId(ProceedingJoinPoint joinPoint, MethodSignature signature) throws JsonProcessingException {
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();
        Class[] parameterTypes = signature.getParameterTypes();

        String programId = "";
        for (int i=0; i<args.length; i++){
            if (parameterNames[i].equals("reservationCreatedMessage") && parameterTypes[i].equals(String.class)){
                ReservationCreateMessage message = objectMapper.readValue(args[i].toString(), ReservationCreateMessage.class);
                programId = message.programId().toString();
            }
        }

        if(programId.isBlank()) {
            throw new ProgramException(ProgramStatusCode.BAD_REQUEST_WITHOUT_PROGRAM_ID);
        }
        return programId;
    }
}
