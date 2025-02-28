package com.example.facieaiprojecttest.parser.json;

import com.example.facieaiprojecttest.model.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.facieaiprojecttest.parser.TradeInputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonTradeInputProcessor implements TradeInputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(JsonTradeInputProcessor.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public JsonTradeInputProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Trade> parse(String inputData) throws Exception {
        if (inputData == null || inputData.trim().isEmpty()) {
            logger.error("Received empty JSON input.");
            throw new IllegalArgumentException("JSON input cannot be null or empty");
        }

        try {
            List<Trade> trades = objectMapper.readValue(inputData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Trade.class));

            logger.info("Successfully parsed {} trade(s).", trades.size());
            return trades;

        } catch (JsonMappingException e) {
            logger.error("Invalid JSON structure: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Invalid JSON structure: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON input: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while parsing JSON input: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while parsing JSON", e);
        }
    }

    @Override
    public String getSupportedFormat() {
        return "json";
    }
}

