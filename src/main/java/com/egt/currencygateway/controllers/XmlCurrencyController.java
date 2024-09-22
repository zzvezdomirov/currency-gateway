package com.egt.currencygateway.controllers;

import com.egt.currencygateway.dto.XmlCurrencyRequest;
import com.egt.currencygateway.services.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;

@RestController
@RequestMapping("/xml_api")
public class XmlCurrencyController {

    private final CurrencyService currencyService;

    public XmlCurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping(value = "/command", consumes = "application/xml", produces = "application/xml")
    public ResponseEntity<?> handleXmlRequest(@RequestBody XmlCurrencyRequest request) throws JAXBException {
        // Call service to handle either 'get' or 'history'
        return currencyService.processXmlRequest(request);
    }
}
