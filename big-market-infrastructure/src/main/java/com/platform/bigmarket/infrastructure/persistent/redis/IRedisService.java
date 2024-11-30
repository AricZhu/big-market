package com.platform.bigmarket.infrastructure.persistent.redis;

import java.util.concurrent.TimeUnit;

public interface IRedisService {
    <T> void setValue(String key, T value);
    <T> void setValueWithExpiry(String key, T value, long timeout, TimeUnit timeUnit);
    <T> T getValue(String key);
    boolean deleteValue(String key);
}
