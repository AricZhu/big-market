package com.platform.bigmarket.infrastructure.persistent.redis;

import org.redisson.api.RBlockingQueue;

public interface IRedisService {
    <T> void setValue(String key, T value);
    <T> void setValue(String key, T value, long timeout);
    <T> T getValue(String key);
    void remove(String key);
    void setAtomicLong(String key, long value);
    Long getAtomicLong(String key);
    long decr(String key);
    boolean decrBug(String key);
    <T> RBlockingQueue <T>getBlockingQueue(String key);
}
