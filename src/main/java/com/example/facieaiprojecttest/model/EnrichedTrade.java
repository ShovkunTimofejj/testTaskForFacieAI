package com.example.facieaiprojecttest.model;

import lombok.Data;

@Data
public class EnrichedTrade {
    private String date;
    private String productName;
    private String currency;
    private double price;
}
