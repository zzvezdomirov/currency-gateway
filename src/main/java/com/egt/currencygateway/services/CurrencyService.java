package com.egt.currencygateway.services;

import com.egt.currencygateway.dto.FixerResponse;
import com.egt.currencygateway.dto.JsonCurrencyRequest;
import com.egt.currencygateway.dto.XmlCurrencyRequest;
import com.egt.currencygateway.models.CurrencyData;
import com.egt.currencygateway.repositories.CurrencyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class CurrencyService {

    @Value("${fixer.api.url}")
    private String fixerApiUrl;

    @Value("${fixer.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private CurrencyDataRepository currencyDataRepository;

    @Autowired
    public CurrencyService(RestTemplate restTemplate, CurrencyDataRepository currencyDataRepository) {
        this.restTemplate = restTemplate;
        this.currencyDataRepository = currencyDataRepository;
    }

    // Scheduled task to fetch data every 1 hour
    @Scheduled(fixedRate = 3600000)
    public void fetchCurrencyData() {
        String url = fixerApiUrl + "?access_key=" + apiKey;

        try {
            FixerResponse response = restTemplate.getForObject(url, FixerResponse.class);
            if (response != null && response.isSuccess()) {
                saveCurrencyData(response);
            }
        } catch (Exception e) {
            // TODO: Handle the error (log it or notify via error handling system)
            System.err.println("Error fetching data from Fixer.io: " + e.getMessage());
        }
    }

    public ResponseEntity<?> processCurrentRequest(JsonCurrencyRequest request) {
        // Handle request validation and fetching data
        // Check for duplicate request ID, then fetch the current currency data from the database or cache
        String data = "placeholder";
        return ResponseEntity.ok(data); // return appropriate response
    }

    public ResponseEntity<?> processHistoryRequest(JsonCurrencyRequest request) {
        // Check for duplicate request ID, then fetch currency history from the database for the given period

        String historyData = "placeholder";
        return ResponseEntity.ok(historyData); // return appropriate response
    }

    public ResponseEntity<?> processXmlRequest(XmlCurrencyRequest request) {
        // Check for duplicate request ID
//        if (request.get != null) {
//            // Handle current currency request
//            return ResponseEntity.ok(data);
//        } else if (request.history != null) {
//            // Handle currency history request
//            return ResponseEntity.ok(historyData);
//        }
        return ResponseEntity.badRequest().build();
    }

    private void saveCurrencyData(FixerResponse response) {
        CurrencyData currencyData = new CurrencyData(
                response.getBase(),
                LocalDateTime.ofEpochSecond(response.getTimestamp(), 0, ZoneOffset.UTC),
                response.getRates()
        );
        currencyDataRepository.save(currencyData);
    }

}
