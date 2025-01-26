package com.templlo.service.program.global.aop.distributed_lock;

import lombok.Getter;

@Getter
public enum DistributedLockKey {
    PROGRAM_CAPACITY_PREFIX("program:capacity:");

    private final String keyName;

    DistributedLockKey(String keyName) {
        this.keyName = keyName;
    }
}
