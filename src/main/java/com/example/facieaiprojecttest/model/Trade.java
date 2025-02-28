package com.example.facieaiprojecttest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Trade {
    private String date;
    private String productId;
    private String currency;
    private double price;
}