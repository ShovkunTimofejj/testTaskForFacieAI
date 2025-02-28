package com.example.facieaiprojecttest.parser.csv;

import com.example.facieaiprojecttest.model.Trade;
import com.example.facieaiprojecttest.parser.TradeInputProcessor;
import com.example.facieaiprojecttest.util.DateUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvTradeInputProcessor implements TradeInputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CsvTradeInputProcessor.class);

    @Override
    public List<Trade> parse(String inputData) throws Exception {
        List<Trade> records = new ArrayList<>();
        List<String> errorRecords = new ArrayList<>();

        try (Reader in = new StringReader(inputData)) {
            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

            for (CSVRecord record : csvRecords) {
                try {
                    Trade trade = parseTrade(record);
                    records.add(trade);
                } catch (IllegalArgumentException e) {
                    logger.error("Skipping invalid record: {} | Error: {}", record, e.getMessage());
                    errorRecords.add(record.toString());
                }
            }
        }

        logger.info("Parsed {} valid trade records, {} invalid records", records.size(), errorRecords.size());
        return records;
    }

    @Override
    public String getSupportedFormat() {
        return "csv";
    }

    private Trade parseTrade(CSVRecord record) {
        String date = record.get("date");
        String productId = record.get("productId");
        String currency = record.get("currency");
        String priceStr = record.get("price");

        if (!DateUtils.isValidDate(date)) {
            throw new IllegalArgumentException("Invalid date format: " + date);
        }
        if (!isValidPrice(priceStr)) {
            throw new IllegalArgumentException("Invalid price format: " + priceStr);
        }

        Trade trade = new Trade();
        trade.setDate(date);
        trade.setProductId(productId);
        trade.setCurrency(currency);
        trade.setPrice(Double.parseDouble(priceStr));
        return trade;
    }

    private boolean isValidPrice(String priceStr) {
        try {
            return Double.parseDouble(priceStr) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


