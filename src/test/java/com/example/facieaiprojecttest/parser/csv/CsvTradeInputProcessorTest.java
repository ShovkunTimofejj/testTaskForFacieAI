package com.example.facieaiprojecttest.parser.csv;

import com.example.facieaiprojecttest.model.Trade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvTradeInputProcessorTest {

    private final CsvTradeInputProcessor processor = new CsvTradeInputProcessor();

    @Test
    void parseValidDataShouldReturnTrades() throws Exception {
        String csvData = "date,productId,currency,price\n" +
                "20230101,1,USD,100.25\n" +
                "20230102,2,EUR,200.45";
        List<Trade> trades = processor.parse(csvData);
        assertEquals(2, trades.size());
        assertEquals("20230101", trades.get(0).getDate());
        assertEquals("1", trades.get(0).getProductId());
        assertEquals(100.25, trades.get(0).getPrice());
    }

    @Test
    void parseInvalidDateShouldSkipRecord() throws Exception {
        String csvData = "date,productId,currency,price\n" +
                "invalidDate,1,USD,100.25\n" +
                "20230102,2,EUR,200.45";
        List<Trade> trades = processor.parse(csvData);
        assertEquals(1, trades.size());
        assertEquals("20230102", trades.get(0).getDate());
    }

    @Test
    void parseInvalidPriceShouldSkipRecord() throws Exception {
        String csvData = "date,productId,currency,price\n" +
                "20230101,1,USD,abc\n" +
                "20230102,2,EUR,200.45";
        List<Trade> trades = processor.parse(csvData);
        assertEquals(1, trades.size());
        assertEquals("20230102", trades.get(0).getDate());
    }
}
