package com.example.demo.weather.dto;

import java.util.List;

public record ForecastInfo(
        String cod,
        String cnt,
        List<ForecastInfo> list,
        double temperature,
        double feelsLike,
        int humidity,
        double windSpeed
) {
}
