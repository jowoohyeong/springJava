package com.example.demo.weather.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("weather.api")
public class WeatherProperties {

    private String apiKey;
    private String baseUrl = "https://api.weatherapi.com/v1";
    private String defaultCity = "Seoul";
    private String lang = "ko";
}
