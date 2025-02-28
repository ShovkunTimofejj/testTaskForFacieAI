package com.example.facieaiprojecttest.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final String PRODUCT_CACHE_KEY = "product:";

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void loadProducts() {
        logger.info("Initializing product data loading...");
        try {
            saveProductsToCache();
        } catch (IOException e) {
            logger.error("Error occurred while loading product data.", e);
        }
    }

    private void saveProductsToCache() throws IOException {
        ClassPathResource resource = new ClassPathResource("product.csv");
        if (!resource.exists()) {
            logger.error("product.csv not found in resources!");
            return;
        }

        RedisClient redisClient = RedisClient.create("redis://localhost:6379");

        try (StatefulRedisConnection<String, String> connection = redisClient.connect();
             BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(),
                     StandardCharsets.UTF_8))) {

            RedisAsyncCommands<String, String> asyncCommands = connection.async();

            List<RedisFuture<String>> futures = reader.lines()
                    .skip(1)
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::parseAndSaveProduct)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(cmd -> asyncCommands.set(cmd.getKey(), cmd.getValue()))
                    .collect(Collectors.toList());

            saveToRedis(futures);
            logger.info("Successfully saved {} products to Redis", futures.size());

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error occurred while saving products to Redis", e);
            Thread.currentThread().interrupt();
        } finally {
            redisClient.shutdown();
        }
    }

    private Optional<AbstractMap.SimpleEntry<String, String>> parseAndSaveProduct(String line) {
        String[] parts = line.split(",");
        if (parts.length == 2) {
            String productId = parts[0].trim();
            String productName = parts[1].trim();
            logger.info("Loaded product: ID={}, Name={}", productId, productName);
            return Optional.of(new AbstractMap.SimpleEntry<>(PRODUCT_CACHE_KEY + productId, productName));
        } else {
            logger.warn("Incorrect line format in product.csv: {}", line);
            return Optional.empty();
        }
    }

    private void saveToRedis(List<RedisFuture<String>> futures) throws InterruptedException, ExecutionException {
        for (RedisFuture<String> future : futures) {
            future.get();
        }
    }

    public String getProductName(String productId) {
        String key = PRODUCT_CACHE_KEY + productId;
        logger.info("Retrieving product with ID '{}'", productId);

        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            logger.warn("Product with ID '{}' not found in cache!", productId);
            return "Missing Product Name";
        }

        String productName = value.toString();
        logger.info("Found product: '{}' for ID '{}'", productName, productId);
        return productName;
    }
}

