package com.egt.currencygateway.repositories;

import com.egt.currencygateway.models.CurrencyData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyDataRepository extends JpaRepository<CurrencyData, Long> {
}
