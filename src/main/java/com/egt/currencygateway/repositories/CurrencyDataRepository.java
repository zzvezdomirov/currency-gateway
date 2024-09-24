package com.egt.currencygateway.repositories;

import com.egt.currencygateway.entities.CurrencyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CurrencyDataRepository extends JpaRepository<CurrencyData, Long> {

    // Fetch the latest currency data by currency, ordered by timestamp
    CurrencyData findTopByCurrencyOrderByTimestampDesc(String currency);

    // Fetch historical currency data for a given period
    List<CurrencyData> findByCurrencyAndTimestampAfter(String currency, LocalDateTime timestamp);
}
