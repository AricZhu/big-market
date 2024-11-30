package com.platform.bigmarket.infrastructure.persistent.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService implements IRedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> void setValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public <T> void setValueWithExpiry(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public <T> T getValue(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean deleteValue(String key) {
        return redisTemplate.delete(key);
    }
}
