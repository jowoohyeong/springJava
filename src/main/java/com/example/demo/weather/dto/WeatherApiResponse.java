package com.example.demo.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record WeatherApiResponse(
        Location location,
        Current current,
        Forecast forecast
) {
    public record Location(
            String name,
            String region,
            String country,
            double lat,
            double lon,
            String localtime
    ) {}

    public record Current(
            @JsonProperty("temp_c") double tempC,
            @JsonProperty("feelslike_c") double feelsLikeC,
            Condition condition,
            @JsonProperty("wind_kph") double windKph,
            int humidity,
            @JsonProperty("precip_mm") double precipMm,
            @JsonProperty("is_day") int isDay,
            @JsonProperty("air_quality") AirQuality airQuality
    ) {}

    public record AirQuality(
            double co,
            double no2,
            double o3,
            double so2,
            @JsonProperty("pm2_5") double pm25,
            @JsonProperty("pm10") double pm10,
            @JsonProperty("us-epa-index") int epaIndex
    ) {}

    public record Forecast(
            @JsonProperty("forecastday") List<ForecastDay> forecastDay
    ) {}

    public record ForecastDay(
            String date,
            Day day,
            Astro astro,
            List<Hour> hour
    ) {}

    public record Day(
            @JsonProperty("maxtemp_c") double maxTempC,
            @JsonProperty("mintemp_c") double minTempC,
            @JsonProperty("avgtemp_c") double avgTempC,
            @JsonProperty("daily_chance_of_rain") int dailyChanceOfRain,
            Condition condition
    ) {}

    public record Astro(
            String sunrise,
            String sunset
    ) {}

    public record Hour(
            String time,
            @JsonProperty("temp_c") double tempC,
            Condition condition,
            @JsonProperty("wind_kph") double windKph,
            int humidity,
            @JsonProperty("chance_of_rain") int chanceOfRain
    ) {}

    public record Condition(
            String text,
            String icon,
            int code
    ) {}
}
