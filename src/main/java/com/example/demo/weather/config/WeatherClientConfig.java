package com.example.demo.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherClientConfig {

    @Bean
    public RestTemplate weatherRestTemplate() {
        return new RestTemplate();
    }
}
