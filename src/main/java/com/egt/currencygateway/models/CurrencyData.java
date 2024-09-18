package com.egt.currencygateway.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class CurrencyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseCurrency;
    private LocalDateTime timestamp;

    @ElementCollection
    @CollectionTable(name = "currency_rates", joinColumns = @JoinColumn(name = "currency_data_id"))
    @MapKeyColumn(name = "currency_code")
    @Column(name = "rate")
    private Map<String, Double> rates;

    public CurrencyData(String baseCurrency, LocalDateTime timestamp, Map<String, Double> rates) {
        this.baseCurrency = baseCurrency;
        this.timestamp = timestamp;
        this.rates = rates;
    }
}
