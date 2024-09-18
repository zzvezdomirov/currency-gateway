package com.egt.currencygateway.services;

import com.egt.currencygateway.dto.FixerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    @Value("${fixer.api.url}")
    private String fixerApiUrl;

    @Value("${fixer.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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

    // Handle saving currency data
    private void saveCurrencyData(FixerResponse response) {
        // Logic for saving to the database
    }
}
