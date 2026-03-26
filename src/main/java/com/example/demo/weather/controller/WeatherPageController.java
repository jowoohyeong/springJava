package com.example.demo.weather.controller;

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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model
    ) {
        LocalDate today = LocalDate.now(KOREA_ZONE);
        LocalDate maxDate = today.plusDays(MAX_FORECAST_DAYS);
        LocalDate selectedDate = date != null ? date : today;

        if (selectedDate.isBefore(today)) {
            selectedDate = today;
        } else if (selectedDate.isAfter(maxDate)) {
            selectedDate = maxDate;
        }

        model.addAttribute("selectedCity", city);
        model.addAttribute("minDate", today);
        model.addAttribute("maxDate", maxDate);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("availableDates", IntStream.rangeClosed(0, MAX_FORECAST_DAYS)
                .mapToObj(today::plusDays)
                .toList());

        try {
            model.addAttribute("weather", weatherService.getWeatherPage(city, selectedDate));
            return "weather/index";
        } catch (WeatherConfigurationException | WeatherApiException ex) {
            model.addAttribute("weatherError", ex.getMessage());
            return "weather/index";
        }
    }
}
