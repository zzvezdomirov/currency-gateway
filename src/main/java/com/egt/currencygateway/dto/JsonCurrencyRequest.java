package com.egt.currencygateway.dto;

import lombok.Data;

@Data
public class JsonCurrencyRequest {
    private String requestId;
    private String currency;
    private int period;
}
