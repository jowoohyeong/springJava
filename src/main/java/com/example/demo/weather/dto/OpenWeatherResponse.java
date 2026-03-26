package com.example.demo.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenWeatherResponse(
        String name,
        List<WeatherDescription> weather,
        MainInfo main,
        WindInfo wind,
        SysInfo sys
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WeatherDescription(String main, String description, String icon) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MainInfo(Double temp, Double feels_like, Double temp_min, Double temp_max, Integer humidity) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WindInfo(Double speed) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SysInfo(String country, Long sunrise, Long sunset) {
    }
}
