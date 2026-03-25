package com.example.demo.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenForecastResponse(
        String cod,
        Integer message,
        Integer cnt,
        List<ForecastItem> list,
        CityInfo city
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ForecastItem(
            Long dt,
            MainInfo main,
            List<WeatherDescription> weather,
            WindInfo wind,
            String dt_txt,
            Double pop
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MainInfo(
            Double temp,
            Double feels_like,
            Double temp_min,
            Double temp_max,
            Integer pressure,
            Integer humidity
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WeatherDescription(
            Integer id,
            String main,
            String description,
            String icon
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityInfo(Double id, String name, String country, double timezone) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WindInfo(
            Double speed,
            Integer deg,
            Double gust
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SysInfo(String pod) {
    }
}
