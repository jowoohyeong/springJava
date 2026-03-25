package com.example.demo.weather.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("weather.openweathermap")
public class WeatherProperties {

    private String apiKey;
    private String baseUrl = "https://api.openweathermap.org";
    private String defaultCity = "Seoul";
    private String units = "metric";
    private String lang = "kr";
}
