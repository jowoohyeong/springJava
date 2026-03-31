package com.example.demo.weather.controller;

import com.example.demo.dto.common.ApiResponse;
import com.example.demo.weather.dto.WeatherApiResponse;
import com.example.demo.weather.exception.WeatherApiException;
import com.example.demo.weather.exception.WeatherConfigurationException;
import com.example.demo.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    @Operation(summary = "현재 날씨 및 예보 조회", description = "도시명을 기준으로 WeatherAPI.com 날씨 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<WeatherApiResponse>> weatherInfo(
            @RequestParam(required = false) String city
    ) {
        try {
            WeatherApiResponse forecastInfo = weatherService.getForecast(city);
            return ResponseEntity.ok(new ApiResponse<>(true, "weather fetched", forecastInfo));
        } catch (WeatherConfigurationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        } catch (WeatherApiException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }
}
