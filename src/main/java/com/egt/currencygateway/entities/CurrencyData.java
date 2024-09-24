package com.egt.currencygateway.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
public class CurrencyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseCurrency;
    private String currency;
    private LocalDateTime timestamp;

    @ElementCollection
    @CollectionTable(name = "currency_data_rates", joinColumns = @JoinColumn(name = "currency_data_id"))
    @MapKeyColumn(name = "rates_key")
    @Column(name = "rates")
    private Map<String, Double> rates;
}
