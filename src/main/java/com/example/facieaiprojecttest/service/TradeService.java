package com.example.facieaiprojecttest.service;

import com.example.facieaiprojecttest.model.EnrichedTrade;
import com.example.facieaiprojecttest.model.Trade;
import com.example.facieaiprojecttest.parser.TradeInputProcessor;
import com.example.facieaiprojecttest.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final List<TradeInputProcessor> tradeInputProcessors;

    @Qualifier("taskExecutor")
    private final Executor taskExecutor;

    @Async
    public CompletableFuture<List<EnrichedTrade>> processTradesAsync(String inputData, String format) {
        return CompletableFuture.supplyAsync(() -> processTrades(inputData, format), taskExecutor);
    }

     List<EnrichedTrade> processTrades(String inputData, String format) {
        try {
            List<Trade> trades = parseTrades(inputData, format);
            List<Trade> validTrades = validateTrades(trades);
            return enrichTrades(validTrades);
        } catch (Exception e) {
            logger.error("Error processing trades asynchronously", e);
            throw new RuntimeException("Error processing trades: " + e.getMessage(), e);
        }
    }

    private List<Trade> parseTrades(String inputData, String format) throws Exception {
        TradeInputProcessor processor = detectFormat(format);
        logger.info("Using processor: {}", processor.getClass().getSimpleName());
        return processor.parse(inputData);
    }

    private TradeInputProcessor detectFormat(String format) {
        return tradeInputProcessors.stream()
                .filter(p -> p.getSupportedFormat().equalsIgnoreCase(format))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Unsupported format: {}", format);
                    return new IllegalArgumentException("Unsupported input format: " + format);
                });
    }

    private List<Trade> validateTrades(List<Trade> trades) {
        return trades.stream()
                .filter(trade -> {
                    if (!DateUtils.isValidDate(trade.getDate())) {
                        logger.error("Invalid date: {}. Skipping trade: {}", trade.getDate(), trade);
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private List<EnrichedTrade> enrichTrades(List<Trade> trades) {
        return trades.parallelStream()
                .map(this::mapToEnrichedTrade)
                .collect(Collectors.toList());
    }

    private EnrichedTrade mapToEnrichedTrade(Trade trade) {
        EnrichedTrade enrichedTrade = new EnrichedTrade();
        enrichedTrade.setDate(trade.getDate());
        enrichedTrade.setCurrency(trade.getCurrency());
        enrichedTrade.setPrice(trade.getPrice());

        String productName = (String) redisTemplate.opsForValue().get("product:" + trade.getProductId());
        enrichedTrade.setProductName(productName != null ? productName : "Missing Product Name");

        return enrichedTrade;
    }
}


