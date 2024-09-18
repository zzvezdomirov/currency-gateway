package com.egt.currencygateway.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;

import java.util.HashMap;
import java.util.Map;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.load();

        Map<String, Object> properties = new HashMap<>();
        properties.put("DB_URL", dotenv.get("DB_URL"));
        properties.put("DB_USERNAME", dotenv.get("DB_USERNAME"));
        properties.put("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        properties.put("REDIS_HOST", dotenv.get("REDIS_HOST"));
        properties.put("REDIS_PORT", dotenv.get("REDIS_PORT"));
        properties.put("RABBITMQ_HOST", dotenv.get("RABBITMQ_HOST"));
        properties.put("RABBITMQ_PORT", dotenv.get("RABBITMQ_PORT"));
        properties.put("FIXER_API_URL", dotenv.get("FIXER_API_URL"));
        properties.put("FIXER_API_KEY", dotenv.get("FIXER_API_KEY"));
        properties.put("TASK_SCHEDULING_POOL_SIZE", dotenv.get("TASK_SCHEDULING_POOL_SIZE"));

        PropertySource<Map<String, Object>> propertySource = new MapPropertySource("dotenv-properties", properties);
        environment.getPropertySources().addFirst(propertySource);
    }
}
