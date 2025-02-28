package com.example.facieaiprojecttest.parser;

import com.example.facieaiprojecttest.model.Trade;

import java.util.List;

public interface TradeInputProcessor {
    List<Trade> parse(String inputData) throws Exception;
    String getSupportedFormat();
}
