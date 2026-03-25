package com.example.demo.weather.service;

import com.example.demo.weather.config.WeatherProperties;
import com.example.demo.weather.dto.OpenForecastResponse;
import com.example.demo.weather.exception.WeatherApiException;
import com.example.demo.weather.exception.WeatherConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherProperties weatherProperties;
    private final RestTemplate weatherRestTemplate;

    public OpenForecastResponse getForecast(String city) {
        validateApiKey();

        String targetCity = resolveCity(city);
        URI uri = buildForecastUri(targetCity);

        return requestForecast(uri, targetCity);
    }

    private void validateApiKey() {
        if (!StringUtils.hasText(weatherProperties.getApiKey())) {
            throw new WeatherConfigurationException("weather.openweathermap.api-key is not configured");
        }
    }

    private String resolveCity(String city) {
        return StringUtils.hasText(city) ? city.trim() : weatherProperties.getDefaultCity();
    }

    private URI buildForecastUri(String city) {
        return UriComponentsBuilder
                .fromHttpUrl(weatherProperties.getBaseUrl())
                .path("/data/2.5/forecast")
                .queryParam("q", city)
                .queryParam("appid", weatherProperties.getApiKey())
                .queryParam("cnt", 5)
                .queryParam("units", weatherProperties.getUnits())
                .queryParam("lang", weatherProperties.getLang())
                .build(true)
                .toUri();
    }

    private OpenForecastResponse requestForecast(URI uri, String city) {
        try {
            ResponseEntity<OpenForecastResponse> response = weatherRestTemplate.getForEntity(uri,  OpenForecastResponse.class);
            OpenForecastResponse body = response.getBody();
            System.out.println("body = " + body);

           /* if (body == null || body.main() == null || !StringUtils.hasText(body.name())) {
                throw new WeatherApiException("Weather API returned an invalid response");
            }*/

            return body;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new WeatherApiException("City not found: " + city, ex);
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new WeatherApiException("Invalid OpenWeather API key", ex);
        } catch (HttpServerErrorException ex) {
            throw new WeatherApiException("OpenWeather server error", ex);
        } catch (RestClientException ex) {
            throw new WeatherApiException("Failed to fetch weather from OpenWeather", ex);
        }
    }
/*
    private ForecastInfo toForecastInfo(OpenForecastResponse response) {
        return new ForecastInfo(
                response.name(),
                response.sys() != null ? response.sys().country() : "",
                Optional.ofNullable(response.weather())
                        .filter(items -> !items.isEmpty())
                        .map(items -> items.get(0).description())
                        .orElse(""),
                Optional.ofNullable(response.main().temp()).orElse(0.0),
                Optional.ofNullable(response.main().feels_like()).orElse(0.0),
                Optional.ofNullable(response.main().humidity()).orElse(0),
                response.wind() != null && response.wind().speed() != null ? response.wind().speed() : 0.0
        );
    }*/
}
