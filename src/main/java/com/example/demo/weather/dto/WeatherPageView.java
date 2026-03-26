package com.example.demo.weather.dto;

import java.util.List;

public record WeatherPageView(
        String city,
        String country,
        String currentDateTime,
        String currentDescription,
        String currentIconUrl,
        String currentTemp,
        String feelsLike,
        String highTemp,
        String lowTemp,
        String humidity,
        String windSpeed,
        String rainProbability,
        String sunrise,
        String sunset,
        String temperatureChartAreaPoints,
        String temperatureChartPolyline,
        List<TemperaturePointView> temperatureChart,
        List<ForecastSlotView> hourlyForecast
) {

    public record TemperaturePointView(
            String timeLabel,
            String temperature,
            int xPercent,
            int yPercent
    ) {
    }

    public record ForecastSlotView(
            String dayLabel,
            String timeLabel,
            String description,
            String iconUrl,
            String temperature,
            String tempRange,
            String rainProbability,
            String windSpeed
    ) {
    }
}
