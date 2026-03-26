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
            CloudsInfo clouds,
            WindInfo wind,
            Double visibility,
            Double pop,
            SysInfo sys,
            String dt_txt

    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record MainInfo(
                Double temp,
                Double feels_like,
                Double temp_min,
                Double temp_max
                , Long pressure
                , Long sea_level
                , Long grnd_level
                , Integer humidity
                , Long temp_kf
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
        public record CloudsInfo(
                String all
        ) {
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record WindInfo(
                Double speed,
                Long deg,
                Double gust
        ) {
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record SysInfo(
                String pod
        ) {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityInfo(
            Double id
            , String name
            , CoordInfo coord
            , String country
            , Double population
            , double timezone
            , double sunrise
            , double sunset
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record CoordInfo(
                Double lat
                , Double lon
        ) {
        }
    }
}
