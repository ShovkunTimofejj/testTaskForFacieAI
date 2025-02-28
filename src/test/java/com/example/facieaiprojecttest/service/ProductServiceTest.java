package com.example.facieaiprojecttest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.slf4j.Logger;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testLoadProducts() {
        assertDoesNotThrow(() -> productService.loadProducts());
    }

    @Test
    void testGetProductName_ProductExists() {
        String productId = "123";
        String expectedProductName = "Test Product";

        when(valueOperations.get("product:" + productId)).thenReturn(expectedProductName);

        String actualProductName = productService.getProductName(productId);

        assertEquals(expectedProductName, actualProductName);
    }

    @Test
    void testGetProductName_ProductNotFound() {
        String productId = "999";

        when(valueOperations.get("product:" + productId)).thenReturn(null);

        String actualProductName = productService.getProductName(productId);

        assertEquals("Missing Product Name", actualProductName);
    }
}




