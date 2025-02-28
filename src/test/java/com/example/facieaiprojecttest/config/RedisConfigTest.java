package com.example.facieaiprojecttest.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedisConnectionFactoryBeanExists() {
        assertThat(redisConnectionFactory).isNotNull();
        assertThat(context.getBean(RedisConnectionFactory.class)).isNotNull();
    }

    @Test
    void testRedisTemplateBeanExists() {
        assertThat(redisTemplate).isNotNull();
        assertThat(context.getBean("redisTemplate", RedisTemplate.class)).isNotNull();
    }

    @Test
    void testRedisTemplateSerializers() {
        assertThat(redisTemplate.getKeySerializer()).isInstanceOf(
                org.springframework.data.redis.serializer.StringRedisSerializer.class);
        assertThat(redisTemplate.getValueSerializer()).isInstanceOf(
                org.springframework.data.redis.serializer.StringRedisSerializer.class);
    }
}

