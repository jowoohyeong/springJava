package com.example.demo.weather.service;

import com.example.demo.weather.config.WeatherProperties;
import com.example.demo.weather.dto.WeatherApiResponse;
import com.example.demo.weather.dto.WeatherPageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherProperties weatherProperties;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        when(weatherProperties.getApiKey()).thenReturn("test-api-key");
        when(weatherProperties.getBaseUrl()).thenReturn("https://api.weatherapi.com/v1");
        when(weatherProperties.getLang()).thenReturn("ko");
    }

    @Test
    @DisplayName("도시 정보를 기반으로 날씨 페이지 뷰를 성공적으로 생성한다")
    void getWeatherPage_Success() {
        // given
        String city = "Seoul";
        LocalDate targetDate = LocalDate.now();
        WeatherApiResponse mockResponse = createMockResponse();

        when(restTemplate.getForObject(any(URI.class), eq(WeatherApiResponse.class)))
                .thenReturn(mockResponse);

        // when
        WeatherPageView result = weatherService.getWeatherPage(city, targetDate);

        // then
        assertThat(result.city()).isEqualTo("Seoul");
        assertThat(result.currentTemp()).isEqualTo("10°");
        assertThat(result.hourlyForecast()).hasSize(6);
    }

    private WeatherApiResponse createMockResponse() {
        WeatherApiResponse.Condition cond = new WeatherApiResponse.Condition("맑음", "//cdn.weatherapi.com/icon.png", 1000);
        WeatherApiResponse.Location loc = new WeatherApiResponse.Location("Seoul", "Seoul", "South Korea", 37.5, 127.0, "2026-03-31 12:00");
        WeatherApiResponse.Current cur = new WeatherApiResponse.Current(10.0, 9.0, cond, 10.0, 50, 0.0, 1);
        
        WeatherApiResponse.Hour hour = new WeatherApiResponse.Hour("2026-03-31 12:00", 10.0, cond, 10.0, 50, 0);
        List<WeatherApiResponse.Hour> hours = List.of(hour, hour, hour, hour, hour, hour);
        
        WeatherApiResponse.ForecastDay forecastDay = new WeatherApiResponse.ForecastDay(
                LocalDate.now().toString(),
                new WeatherApiResponse.Day(15.0, 5.0, 10.0, 0, cond),
                new WeatherApiResponse.Astro("06:00 AM", "06:00 PM"),
                hours
        );
        
        return new WeatherApiResponse(loc, cur, new WeatherApiResponse.Forecast(List.of(forecastDay)));
    }
}
