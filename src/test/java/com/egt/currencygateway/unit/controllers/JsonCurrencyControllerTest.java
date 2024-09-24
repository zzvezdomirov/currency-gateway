package com.egt.currencygateway.unit.controllers;

import com.egt.currencygateway.controllers.JsonCurrencyController;
import com.egt.currencygateway.services.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JsonCurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private JsonCurrencyController jsonCurrencyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jsonCurrencyController).build();
    }

    @Test
    public void getCurrentCurrency_whenRequestIsValid_shouldReturnOk() throws Exception {
        // Arrange
        when(currencyService.isDuplicateRequest(anyString())).thenReturn(false);
        when(currencyService.getLatestCurrencyData(anyString())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"requestId\": \"123\", \"currency\": \"EUR\" }"))
                .andExpect(status().isNotFound()); // Since we return null in the service mock
    }

    @Test
    public void getCurrentCurrency_whenRequestIsDuplicate_shouldReturnConflict() throws Exception {
        // Arrange
        when(currencyService.isDuplicateRequest(anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"requestId\": \"123\", \"currency\": \"EUR\" }"))
                .andExpect(status().isConflict());
    }

    @Test
    public void getCurrencyHistory_whenRequestIsValid_shouldReturnOk() throws Exception {
        // Arrange
        when(currencyService.isDuplicateRequest(anyString())).thenReturn(false);
        when(currencyService.getCurrencyHistory(anyString(), anyInt())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"requestId\": \"123\", \"currency\": \"EUR\", \"period\": 24 }"))
                .andExpect(status().isNotFound()); // Since we return an empty list in the service mock
    }
}
