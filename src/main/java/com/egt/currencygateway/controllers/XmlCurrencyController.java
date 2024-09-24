package com.egt.currencygateway.controllers;

import com.egt.currencygateway.entities.CurrencyData;
import com.egt.currencygateway.dto.XmlCurrencyRequest;
import com.egt.currencygateway.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/xml_api")
@RequiredArgsConstructor
public class XmlCurrencyController {

    private final CurrencyService currencyService;

    @PostMapping(value = "/command", consumes = "application/xml", produces = "application/xml")
    public ResponseEntity<?> handleXmlRequest(@RequestBody XmlCurrencyRequest request) {
        if (currencyService.isDuplicateRequest(request.getRequestId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate request ID");
        }

        if (request.get != null) {
            CurrencyData currentData = currencyService.getLatestCurrencyData(request.get.getCurrency());
            return ResponseEntity.ok(currentData);
        } else if (request.history != null) {
            List<CurrencyData> historyData = currencyService.getCurrencyHistory(request.history.getCurrency(), request.history.getPeriod());
            return ResponseEntity.ok(historyData);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid XML request");
    }
}
