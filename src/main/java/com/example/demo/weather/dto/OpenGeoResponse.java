package com.example.demo.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenGeoResponse(
        String name
        , LocalNameInfo local_names
        , double lat
        , double lon
        , String country
        , String state
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LocalNameInfo(
            String en
            , String ko
    ) {
    }
}