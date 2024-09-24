package com.egt.currencygateway.controllers;

import com.egt.currencygateway.entities.CurrencyData;
import com.egt.currencygateway.dto.JsonCurrencyRequest;
import com.egt.currencygateway.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/json_api")
@RequiredArgsConstructor
public class JsonCurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("/current")
    public ResponseEntity<?> getCurrentCurrency(@RequestBody JsonCurrencyRequest request) {
        if (currencyService.isDuplicateRequest(request.getRequestId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate request ID");
        }

        CurrencyData latestCurrencyData = currencyService.getLatestCurrencyData(request.getCurrency());
        if (latestCurrencyData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Currency data not found");
        }

        return ResponseEntity.ok(latestCurrencyData);
    }

    @PostMapping("/history")
    public ResponseEntity<?> getCurrencyHistory(@RequestBody JsonCurrencyRequest request) {
        if (currencyService.isDuplicateRequest(request.getRequestId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate request ID");
        }

        List<CurrencyData> historyData = currencyService.getCurrencyHistory(request.getCurrency(), request.getPeriod());
        if (historyData == null || historyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No historical data found");
        }

        return ResponseEntity.ok(historyData);
    }
}
