package com.egt.currencygateway.dto;

import lombok.Data;

@Data
public class JsonCurrencyRequest {
    private String requestId;
    private long timestamp;
    private String client;
    private String currency;
    private int period; // Optional, used for history request
}
