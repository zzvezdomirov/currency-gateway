package com.egt.currencygateway.services;

import com.egt.currencygateway.exceptions.ExternalApiException;
import com.egt.currencygateway.entities.CurrencyData;
import com.egt.currencygateway.dto.FixerResponse;
import com.egt.currencygateway.entities.RequestLog;
import com.egt.currencygateway.repositories.CurrencyDataRepository;
import com.egt.currencygateway.repositories.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyDataRepository currencyDataRepository;
    private final RequestLogRepository requestLogRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Value("${fixer.api.url}")
    private String fixerApiUrl;

    @Value("${fixer.api.key}")
    private String fixerApiKey;

    @Scheduled(fixedRate = 3600000) // Every 1 hour
    public void fetchCurrencyData() {
        String apiUrl = fixerApiUrl + "?access_key=" + fixerApiKey;
        FixerResponse response = restTemplate.getForObject(apiUrl, FixerResponse.class);

        if (response != null && response.isSuccess()) {
            CurrencyData currencyData = new CurrencyData();
            currencyData.setBaseCurrency(response.getBase());
            currencyData.setTimestamp(LocalDateTime.now());
            currencyData.setRates(response.getRates());
            currencyDataRepository.save(currencyData);
            log.info("Successfully fetched and saved currency data.");
        } else {
            log.error("Failed to fetch data from Fixer.io");
            throw new ExternalApiException("Failed to fetch data from Fixer.io");
        }
    }
    public void logRequest(String serviceName, String requestId, String clientId) {
        RequestLog log = new RequestLog(serviceName, requestId, clientId, LocalDateTime.now());
        requestLogRepository.save(log);

        // Send to RabbitMQ
        String message = "Service: " + serviceName + ", Request ID: " + requestId + ", Client ID: " + clientId;
        rabbitTemplate.convertAndSend("logs-exchange", "", message);
    }

    public boolean isDuplicateRequest(String requestId) {
        try {
            if (redisTemplate.opsForSet().isMember("processedRequests", requestId)) {
                log.info("Request ID {} is a duplicate", requestId);
                return true;
            }
            redisTemplate.opsForSet().add("processedRequests", requestId);
            log.info("Request ID {} added to processed requests", requestId);
            return false;
        } catch (Exception e) {
            log.error("Error while checking Redis for request ID {}: {}", requestId, e.getMessage());
            throw new RuntimeException("Unable to connect to Redis", e);
        }
    }

    public CurrencyData getLatestCurrencyData(String currency) {
        return currencyDataRepository.findTopByCurrencyOrderByTimestampDesc(currency);
    }

    public List<CurrencyData> getCurrencyHistory(String currency, int period) {
        LocalDateTime fromTime = LocalDateTime.now().minusHours(period);
        return currencyDataRepository.findByCurrencyAndTimestampAfter(currency, fromTime);
    }

    public void testRedisConnection() {
        try {
            String pingResponse = redisTemplate.getConnectionFactory().getConnection().ping();
            log.info("Redis connection successful: " + pingResponse);
        } catch (Exception e) {
            log.error("Unable to connect to Redis: " + e.getMessage());
        }
    }
}
