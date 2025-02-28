package com.example.facieaiprojecttest.service;

import com.example.facieaiprojecttest.model.EnrichedTrade;
import com.example.facieaiprojecttest.model.Trade;
import com.example.facieaiprojecttest.parser.TradeInputProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
@EnableAsync
class TradeServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private List<TradeInputProcessor> tradeInputProcessors;

    @Mock
    private TradeInputProcessor tradeInputProcessor;

    @Mock
    private Executor taskExecutor;

    @InjectMocks
    private TradeService tradeService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        lenient().when(tradeInputProcessors.stream()).thenReturn(List.of(tradeInputProcessor).stream());
    }

    @Test
    void processTradesAsyncShouldProcessValidTrades() throws Exception {
        Trade trade = new Trade();
        trade.setDate("20240226");
        trade.setCurrency("USD");
        trade.setPrice(100.0);
        trade.setProductId("123");

        when(tradeInputProcessor.getSupportedFormat()).thenReturn("CSV");
        when(tradeInputProcessor.parse(anyString())).thenReturn(Collections.singletonList(trade));
        when(valueOperations.get("product:123")).thenReturn("Test Product");

        CompletableFuture<List<EnrichedTrade>> future = CompletableFuture.completedFuture(
                tradeService.processTrades("inputData", "CSV")
        );

        List<EnrichedTrade> result = future.join();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("20240226", result.get(0).getDate());
        assertEquals("USD", result.get(0).getCurrency());
        assertEquals(100.0, result.get(0).getPrice());
        assertEquals("Test Product", result.get(0).getProductName());
    }

    @Test
    void processTradesAsyncShouldSkipInvalidDates() throws Exception {
        Trade trade = new Trade();
        trade.setDate("invalid");
        trade.setCurrency("USD");
        trade.setPrice(100.0);
        trade.setProductId("123");

        lenient().when(tradeInputProcessor.getSupportedFormat()).thenReturn("CSV");
        lenient().when(tradeInputProcessor.parse(anyString())).thenReturn(Collections.singletonList(trade));

        CompletableFuture<List<EnrichedTrade>> future = CompletableFuture.completedFuture(
                tradeService.processTrades("inputData", "CSV")
        );

        List<EnrichedTrade> result = future.join();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void processTradesAsyncShouldUseDefaultProductNameIfNotInRedis() throws Exception {
        Trade trade = new Trade();
        trade.setDate("20240226");
        trade.setCurrency("USD");
        trade.setPrice(100.0);
        trade.setProductId("123");

        when(tradeInputProcessor.getSupportedFormat()).thenReturn("CSV");
        when(tradeInputProcessor.parse(anyString())).thenReturn(Collections.singletonList(trade));
        when(valueOperations.get("product:123")).thenReturn(null);

        CompletableFuture<List<EnrichedTrade>> future = CompletableFuture.completedFuture(
                tradeService.processTrades("inputData", "CSV")
        );

        List<EnrichedTrade> result = future.join();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Missing Product Name", result.get(0).getProductName());
    }

    @Test
    void processTradesAsyncShouldThrowExceptionForUnsupportedFormat() {
        when(tradeInputProcessors.stream()).thenAnswer(invocation -> Stream.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                tradeService.processTrades("inputData", "invalidDate")
        );
        assertTrue(exception.getMessage().contains("Unsupported input format"));


        assertTrue(exception.getMessage().contains("Unsupported input format"));
    }
}

