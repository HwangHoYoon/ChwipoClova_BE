package com.chwipoClova.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Key serializer 설정 (StringRedisSerializer 사용)
        template.setKeySerializer(new StringRedisSerializer());

        // Value serializer 설정 (GenericJackson2JsonRedisSerializer 사용)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash key serializer 설정 (StringRedisSerializer 사용)
        //template.setHashKeySerializer(new StringRedisSerializer());

        // Hash value serializer 설정 (GenericJackson2JsonRedisSerializer 사용)
        //template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
