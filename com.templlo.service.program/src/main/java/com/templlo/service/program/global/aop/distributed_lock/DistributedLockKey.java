package com.templlo.service.program.global.aop.distributed_lock;

public enum DistributedLockKey {
    TEMPLE_STAY_PROGRAM_CAPACITY_PREFIX("temple-stay:capacity:");

    private final String keyName;

    DistributedLockKey(String keyName) {
        this.keyName = keyName;
    }
}
