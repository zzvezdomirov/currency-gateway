package com.egt.currencygateway.unit.services;

import com.egt.currencygateway.exceptions.ExternalApiException;
import com.egt.currencygateway.entities.CurrencyData;
import com.egt.currencygateway.dto.FixerResponse;
import com.egt.currencygateway.entities.RequestLog;
import com.egt.currencygateway.repositories.CurrencyDataRepository;
import com.egt.currencygateway.repositories.RequestLogRepository;
import com.egt.currencygateway.services.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {

    @Mock
    private CurrencyDataRepository currencyDataRepository;

    @Mock
    private RequestLogRepository requestLogRepository;

    @Mock
    private SetOperations<String, Object> setOperations;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    public void fetchCurrencyData_whenApiSuccess_shouldSaveData() {
        // Arrange
        FixerResponse mockResponse = new FixerResponse();
        mockResponse.setSuccess(true);
        mockResponse.setBase("EUR");
        mockResponse.setRates(Collections.singletonMap("USD", 1.18));
        when(restTemplate.getForObject(anyString(), eq(FixerResponse.class))).thenReturn(mockResponse);

        // Act
        currencyService.fetchCurrencyData();

        // Assert
        verify(currencyDataRepository, times(1)).save(any(CurrencyData.class));
    }

    @Test
    public void fetchCurrencyData_whenApiFailure_shouldThrowException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(FixerResponse.class))).thenReturn(null);

        // Act & Assert
        ExternalApiException exception = assertThrows(ExternalApiException.class, () -> {
            currencyService.fetchCurrencyData();
        });
        assertEquals("Failed to fetch data from Fixer.io", exception.getMessage());
    }

    @Test
    public void isDuplicateRequest_whenRequestNotDuplicate_shouldReturnFalse() {
        // Arrange
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(redisTemplate.opsForSet().isMember(anyString(), anyString())).thenReturn(false);

        // Act
        boolean isDuplicate = currencyService.isDuplicateRequest("test-id");

        // Assert
        assertFalse(isDuplicate);
    }

    @Test
    public void isDuplicateRequest_whenRequestDuplicate_shouldReturnTrue() {
        // Arrange
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(redisTemplate.opsForSet().isMember(anyString(), anyString())).thenReturn(true);

        // Act
        boolean isDuplicate = currencyService.isDuplicateRequest("test-id");

        // Assert
        assertTrue(isDuplicate);
    }

    @Test
    void logRequest_ShouldLogRequestAndSendMessageToRabbitMQ() {
        // Arrange
        String serviceName = "EXT_SERVICE_1";
        String requestId = "12345";
        String clientId = "client_01";

        // Act
        currencyService.logRequest(serviceName, requestId, clientId);

        // Assert
        verify(requestLogRepository, times(1)).save(any(RequestLog.class));
        verify(rabbitTemplate, times(1)).convertAndSend(eq("logs-exchange"), eq(""), contains("Request ID: 12345"));
    }
}
