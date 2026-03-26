package com.example.demo.weather.service;

import com.example.demo.weather.config.WeatherProperties;
import com.example.demo.weather.exception.WeatherConfigurationException;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class WeatherServiceTest {

    @Test
    void throwsWhenApiKeyIsMissing() {
        WeatherProperties weatherProperties = new WeatherProperties();
        WeatherService weatherService = new WeatherService(weatherProperties, new RestTemplate());

        assertThrows(WeatherConfigurationException.class, () -> weatherService.getForecast("Seoul"));
    }
}
