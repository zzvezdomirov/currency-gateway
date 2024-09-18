package com.egt.currencygateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class FixerResponse {

    private boolean success;
    private long timestamp;
    private String base;
    private String date;

    @JsonProperty("rates")
    private Map<String, Double> rates;

}
