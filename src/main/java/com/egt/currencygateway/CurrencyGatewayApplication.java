package com.egt.currencygateway;

import com.egt.currencygateway.services.CurrencyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrencyGatewayApplication implements CommandLineRunner {
	private final CurrencyService currencyService;

	public CurrencyGatewayApplication(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	public static void main(String[] args) {
		SpringApplication.run(CurrencyGatewayApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		currencyService.testRedisConnection();
	}

}
