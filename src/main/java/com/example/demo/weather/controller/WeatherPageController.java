package com.example.demo.weather.controller;

import com.example.demo.weather.dto.WeatherPageView;
import com.example.demo.weather.exception.WeatherApiException;
import com.example.demo.weather.exception.WeatherConfigurationException;
import com.example.demo.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class WeatherPageController {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");
    private static final int MAX_FORECAST_DAYS = 5;

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public String weatherPage(
            @RequestParam(required = false) String city,
            Model model
    ) {
        LocalDate today = LocalDate.now(KOREA_ZONE);

        model.addAttribute("selectedCity", city);
        model.addAttribute("selectedDate", today);

        try {
            WeatherPageView weather = weatherService.getWeatherPage(city, today);
            model.addAttribute("weather", weather);
            // API가 반환한 실제 도시명으로 업데이트 (예: "서울" -> "Seoul")
            model.addAttribute("selectedCity", weather.city());
            return "weather/index";
        } catch (WeatherConfigurationException | WeatherApiException ex) {
            model.addAttribute("weatherError", ex.getMessage());
            return "weather/index";
        }
    }
}
