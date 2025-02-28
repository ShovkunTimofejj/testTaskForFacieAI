package com.example.facieaiprojecttest.parser.xml;

import com.example.facieaiprojecttest.model.Trade;
import com.example.facieaiprojecttest.parser.TradeInputProcessor;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XmlTradeInputProcessor implements TradeInputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(XmlTradeInputProcessor.class);
    private final XmlMapper xmlMapper;

    @Autowired
    public XmlTradeInputProcessor(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }

    @Override
    public List<Trade> parse(String inputData) throws Exception {
        if (inputData == null || inputData.trim().isEmpty()) {
            logger.error("Received empty XML input.");
            throw new IllegalArgumentException("XML input cannot be null or empty");
        }

        try {
            List<Trade> trades = xmlMapper.readValue(
                    inputData, xmlMapper.getTypeFactory().constructCollectionType(List.class, Trade.class)
            );

            if (trades == null || trades.isEmpty()) {
                logger.error("No trades found in XML or XML is invalid");
                throw new Exception("No trades found in XML or XML is invalid");
            }

            logger.info("Successfully parsed {} trade record(s) from XML", trades.size());
            return trades;

        } catch (Exception e) {
            logger.error("Error parsing XML input: {}", e.getMessage(), e);
            throw new Exception("Error parsing XML: " + e.getMessage(), e);
        }
    }

    @Override
    public String getSupportedFormat() {
        return "xml";
    }
}



