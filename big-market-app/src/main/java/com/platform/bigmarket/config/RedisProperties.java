package com.platform.bigmarket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.data.redis")
@Data
@Component
public class RedisProperties {
    private String host;
    private Integer port;
    private Lettuce lettuce;

    @Data
    public static class Lettuce {
        private Pool pool;

        @Data
        public static class Pool {
            private int maxActive;
            private int maxIdle;
            private int minIdle;
            private String maxWait;
        }

    }
}
