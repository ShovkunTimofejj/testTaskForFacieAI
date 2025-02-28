package com.example.facieaiprojecttest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSaveData() {
        String key = "testKey";
        String value = "testValue";
        long timeout = 10L;

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        redisService.saveData(key, value, timeout);

        verify(valueOperations).set(key, value, timeout, TimeUnit.MINUTES);
    }

    @Test
    void testGetData() {
        String key = "testKey";
        String expectedValue = "testValue";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(expectedValue);

        String actualValue = redisService.getData(key);

        assertEquals(expectedValue, actualValue);
        verify(valueOperations).get(key);
    }

    @Test
    void testDeleteData() {
        String key = "testKey";

        redisService.deleteData(key);

        verify(redisTemplate).delete(key);
    }
}

