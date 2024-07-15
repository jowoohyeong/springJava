package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}") 
    private int port;

    /**
     * Redis 와의 연결을 위한 'Connection'을 생성합니다.
     *
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        System.out.println("host = " + host);
        System.out.println("port = " + port);
        return new LettuceConnectionFactory(host, port);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // redis 연결.
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // key-value 형태로 직렬화 수행
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        // Hash를 사용할 경우 Serializer
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        System.out.println("redisTemplate = " + redisTemplate);
        return redisTemplate;
    }
}
