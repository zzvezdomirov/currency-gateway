package com.egt.currencygateway.controllers;

import com.egt.currencygateway.dto.JsonCurrencyRequest;
import com.egt.currencygateway.services.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json_api")
public class JsonCurrencyController {

    private final CurrencyService currencyService;

    public JsonCurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/current")
    public ResponseEntity<?> getCurrentCurrency(@RequestBody JsonCurrencyRequest request) {
        // Call the service to get the current currency data
        return currencyService.processCurrentRequest(request);
    }

    @PostMapping("/history")
    public ResponseEntity<?> getCurrencyHistory(@RequestBody JsonCurrencyRequest request) {
        // Call the service to get the currency history
        return currencyService.processHistoryRequest(request);
    }
}
