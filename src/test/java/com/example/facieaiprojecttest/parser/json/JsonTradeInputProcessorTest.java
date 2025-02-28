package com.example.facieaiprojecttest.parser.json;

import com.example.facieaiprojecttest.model.Trade;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonTradeInputProcessorTest {

    private JsonTradeInputProcessor jsonTradeInputProcessor;
    private ObjectMapper objectMapper;

    private Trade trade;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        jsonTradeInputProcessor = new JsonTradeInputProcessor(objectMapper);
        trade = new Trade();
    }

    @Test
    void testParseValidJson() throws Exception {
        List<Trade> trades = new ArrayList<>();
        trades.add(trade);
        String json = "[{\"currency\": \"USD\", \"productId\": \"123\", \"date\": \"2023-01-01\", \"price\": 10.0}]";

        List<Trade> expectedTrades = objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Trade.class));

        List<Trade> result = jsonTradeInputProcessor.parse(json);

        assertEquals(expectedTrades, result);
    }

    @Test
    void testParseEmptyJson() {
        String json = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> jsonTradeInputProcessor
                .parse(json));
        assertEquals("JSON input cannot be null or empty", exception.getMessage());
    }

    @Test
    void testParseNullJson() {
        String json = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> jsonTradeInputProcessor
                .parse(json));
        assertEquals("JSON input cannot be null or empty", exception.getMessage());
    }

    @Test
    void testParseInvalidJson() {
        String json = "[{\"currency\": \"USD\", \"productId\": \"123\", \"date\": \"Invalid Date\", \"price\": \"Invalid Price\"}]";

        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Trade.class)));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> jsonTradeInputProcessor
                .parse(json));
        assertEquals("Invalid JSON structure: ", exception.getMessage().substring(0, 24));
    }

    @Test
    void testGetSupportedFormat() {
        String format = jsonTradeInputProcessor.getSupportedFormat();

        assertEquals("json", format);
    }
}

