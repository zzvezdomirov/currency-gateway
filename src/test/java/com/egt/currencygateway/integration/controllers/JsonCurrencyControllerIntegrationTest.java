package com.egt.currencygateway.integration.controllers;

import com.egt.currencygateway.entities.CurrencyData;
import com.egt.currencygateway.repositories.CurrencyDataRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class JsonCurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyDataRepository currencyDataRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        currencyDataRepository.deleteAll();  // Clean the repository before each test
        redisTemplate.delete("processedRequests");  // Ensure Redis is cleared of duplicates
    }

    @Test
    public void getCurrentCurrency_whenValidRequest_shouldReturnOk() throws Exception {
        // Arrange: Save currency data that the controller can find
        CurrencyData currencyData = new CurrencyData();
        currencyData.setBaseCurrency("EUR");
        currencyData.setRates(Collections.singletonMap("USD", 1.18));
        currencyData.setTimestamp(LocalDateTime.now());
        currencyData.setCurrency("EUR");  // Ensure the currency matches the request
        currencyDataRepository.save(currencyData);

        // Act & Assert: Make a valid request and expect 200 OK
        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"requestId\": \"123\", \"currency\": \"EUR\" }"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCurrencyHistory_whenValidRequest_shouldReturnOk() throws Exception {
        // Arrange: Add historical currency data to the repository
        CurrencyData currencyData = new CurrencyData();
        currencyData.setBaseCurrency("EUR");
        currencyData.setRates(Collections.singletonMap("USD", 1.18));
        currencyData.setTimestamp(LocalDateTime.now().minusHours(1));  // Ensure the timestamp is valid
        currencyData.setCurrency("EUR");  // Ensure the currency matches the request

        currencyDataRepository.save(currencyData);

        // Act & Assert: Make a valid request and expect 200 OK
        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"requestId\": \"123\", \"currency\": \"EUR\", \"period\": 24 }"))
                .andExpect(status().isOk());
    }
}
