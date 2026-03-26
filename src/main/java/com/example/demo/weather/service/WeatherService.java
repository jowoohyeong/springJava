package com.example.demo.weather.service;

import com.example.demo.weather.config.WeatherProperties;
import com.example.demo.weather.dto.OpenForecastResponse;
import com.example.demo.weather.dto.OpenGeoResponse;
import com.example.demo.weather.dto.OpenWeatherResponse;
import com.example.demo.weather.dto.WeatherPageView;
import com.example.demo.weather.exception.WeatherApiException;
import com.example.demo.weather.exception.WeatherConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter CURRENT_TIME_FORMAT = DateTimeFormatter.ofPattern("M월 d일(E) a h:mm", Locale.KOREAN);
    private static final DateTimeFormatter DAY_LABEL_FORMAT = DateTimeFormatter.ofPattern("M/d (E)", Locale.KOREAN);
    private static final DateTimeFormatter TIME_LABEL_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final WeatherProperties weatherProperties;
    private final RestTemplate weatherRestTemplate;

    public OpenForecastResponse getForecast(String cityOrDistrict) {
        validateApiKey();

        String query = resolveLocationQuery(cityOrDistrict);
        OpenGeoResponse geo = requestGeocoding(query);
        URI forecastUri = buildForecastUri(geo.lat(), geo.lon());

        return requestForecast(forecastUri, query);
    }

    public WeatherPageView getWeatherPage(String city, LocalDate targetDate) {
        validateApiKey();

        String query = resolveLocationQuery(city);
        OpenGeoResponse geo = requestGeocoding(query);
        OpenWeatherResponse currentWeather = requestCurrentWeather(geo.lat(), geo.lon(), query);
        OpenForecastResponse forecast = requestForecast(buildForecastUri(geo.lat(), geo.lon()), query);
        List<OpenForecastResponse.ForecastItem> visibleItems = selectVisibleItems(forecast.list(), targetDate);

        if (visibleItems.isEmpty()) {
            throw new WeatherApiException("선택한 날짜의 예보 데이터가 없습니다");
        }

        return new WeatherPageView(
                currentWeather.name() != null ? currentWeather.name() : resolveCity(city),
                currentWeather.sys() != null ? currentWeather.sys().country() : "",
                formatNow(),
                firstCurrentDescription(currentWeather),
                currentIconUrl(currentWeather),
                temperatureText(currentWeather.main() != null ? currentWeather.main().temp() : null),
                temperatureText(currentWeather.main() != null ? currentWeather.main().feels_like() : null),
                temperatureText(currentWeather.main() != null ? currentWeather.main().temp_max() : null),
                temperatureText(currentWeather.main() != null ? currentWeather.main().temp_min() : null),
                percentText(currentWeather.main() != null ? currentWeather.main().humidity() : null),
                windSpeedText(currentWeather.wind() != null ? currentWeather.wind().speed() : null),
                popText(visibleItems.get(0).pop()),
                formatEpochTime(currentWeather.sys() != null ? currentWeather.sys().sunrise() : null),
                formatEpochTime(currentWeather.sys() != null ? currentWeather.sys().sunset() : null),
                buildTemperatureChartAreaPoints(visibleItems),
                buildTemperatureChartPolyline(visibleItems),
                buildTemperatureChart(visibleItems),
                buildForecastSlots(visibleItems)
        );
    }

    private void validateApiKey() {
        if (!StringUtils.hasText(weatherProperties.getApiKey())) {
            throw new WeatherConfigurationException("weather.openweathermap.api-key is not configured");
        }
    }

    private String resolveLocationQuery(String input) {
        String query = StringUtils.hasText(input) ? input.trim() : weatherProperties.getDefaultCity();

        if (!query.contains(",")) {
            query = query + ",KR";
        }

        return query;
    }

    private OpenGeoResponse requestGeocoding(String query) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(weatherProperties.getBaseUrl())
                .path("/geo/1.0/direct")
                .queryParam("q", query)
                .queryParam("limit", 5)
                .queryParam("appid", weatherProperties.getApiKey())
                .encode()
                .build()
                .toUri();

        try {
            ResponseEntity<List<OpenGeoResponse>> response = weatherRestTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<OpenGeoResponse>>() {
                    }
            );

            List<OpenGeoResponse> body = response.getBody();

            if (body == null || body.isEmpty()) {
                throw new WeatherApiException("Location not found: " + query);
            }

            return body.get(0);
        } catch (HttpClientErrorException ex) {
            throw new WeatherApiException("Failed to resolve location: " + query, ex);
        } catch (RestClientException ex) {
            throw new WeatherApiException("Failed to call geocoding API", ex);
        }
    }

    private String resolveCity(String city) {
        return StringUtils.hasText(city) ? city.trim() : weatherProperties.getDefaultCity();
    }

    private URI buildForecastUri(double lat, double lon) {
        return UriComponentsBuilder
                .fromHttpUrl(weatherProperties.getBaseUrl())
                .path("/data/2.5/forecast")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", weatherProperties.getApiKey())
                .queryParam("units", weatherProperties.getUnits())
                .queryParam("lang", weatherProperties.getLang())
                .encode()
                .build()
                .toUri();
    }

    private URI buildCurrentWeatherUri(double lat, double lon) {
        return UriComponentsBuilder
                .fromHttpUrl(weatherProperties.getBaseUrl())
                .path("/data/2.5/weather")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", weatherProperties.getApiKey())
                .queryParam("units", weatherProperties.getUnits())
                .queryParam("lang", weatherProperties.getLang())
                .encode()
                .build()
                .toUri();
    }

    private OpenWeatherResponse requestCurrentWeather(double lat, double lon, String city) {
        URI uri = buildCurrentWeatherUri(lat, lon);

        try {
            ResponseEntity<OpenWeatherResponse> response =
                    weatherRestTemplate.getForEntity(uri, OpenWeatherResponse.class);
            OpenWeatherResponse body = response.getBody();

            if (body == null || body.main() == null) {
                throw new WeatherApiException("Current weather API returned an invalid response");
            }

            return body;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new WeatherApiException("City not found: " + city, ex);
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new WeatherApiException("Invalid OpenWeather API key", ex);
        } catch (HttpServerErrorException ex) {
            throw new WeatherApiException("OpenWeather server error", ex);
        } catch (RestClientException ex) {
            throw new WeatherApiException("Failed to fetch current weather from OpenWeather", ex);
        }
    }

    private OpenForecastResponse requestForecast(URI uri, String city) {
        try {
            ResponseEntity<OpenForecastResponse> response =
                    weatherRestTemplate.getForEntity(uri, OpenForecastResponse.class);
            OpenForecastResponse body = response.getBody();

            if (body == null || body.city() == null || body.list() == null || body.list().isEmpty()) {
                throw new WeatherApiException("Weather API returned an invalid response");
            }

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

    private List<WeatherPageView.ForecastSlotView> buildForecastSlots(List<OpenForecastResponse.ForecastItem> items) {
        return items.stream()
                .map(item -> new WeatherPageView.ForecastSlotView(
                        formatDayLabel(item.dt_txt()),
                        formatTimeLabel(item.dt_txt()),
                        firstDescription(item),
                        iconUrl(firstIcon(item)),
                        temperatureText(item.main() != null ? item.main().temp() : null),
                        tempRangeText(item),
                        popText(item.pop()),
                        windSpeedText(item.wind() != null ? item.wind().speed() : null)
                ))
                .toList();
    }

    private List<WeatherPageView.TemperaturePointView> buildTemperatureChart(List<OpenForecastResponse.ForecastItem> items) {
        List<Double> temperatures = items.stream()
                .map(item -> item.main() != null ? item.main().temp() : null)
                .filter(value -> value != null)
                .toList();

        if (temperatures.isEmpty()) {
            return List.of();
        }

        double min = temperatures.stream().min(Comparator.naturalOrder()).orElse(0.0);
        double max = temperatures.stream().max(Comparator.naturalOrder()).orElse(min);
        double range = max - min;
        int lastIndex = Math.max(items.size() - 1, 1);

        return java.util.stream.IntStream.range(0, items.size())
                .mapToObj(index -> {
                    OpenForecastResponse.ForecastItem item = items.get(index);
                    Double temp = item.main() != null ? item.main().temp() : null;
                    return new WeatherPageView.TemperaturePointView(
                            formatTimeLabel(item.dt_txt()),
                            temperatureText(temp),
                            chartXPercent(index, lastIndex),
                            chartYPercent(temp, min, range)
                    );
                })
                .toList();
    }

    private String buildTemperatureChartPolyline(List<OpenForecastResponse.ForecastItem> items) {
        return buildTemperatureChart(items).stream()
                .map(point -> point.xPercent() + "," + point.yPercent())
                .reduce((left, right) -> left + " " + right)
                .orElse("");
    }

    private String buildTemperatureChartAreaPoints(List<OpenForecastResponse.ForecastItem> items) {
        List<WeatherPageView.TemperaturePointView> points = buildTemperatureChart(items);

        if (points.isEmpty()) {
            return "";
        }

        String linePoints = points.stream()
                .map(point -> point.xPercent() + "," + point.yPercent())
                .reduce((left, right) -> left + " " + right)
                .orElse("");

        return "0,100 " + linePoints + " 100,100";
    }

    private List<OpenForecastResponse.ForecastItem> filterByDate(List<OpenForecastResponse.ForecastItem> items, LocalDate targetDate) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        return items.stream()
                .filter(item -> parseForecastDateTime(item.dt_txt()).toLocalDate().equals(targetDate))
                .toList();
    }

    private List<OpenForecastResponse.ForecastItem> selectVisibleItems(List<OpenForecastResponse.ForecastItem> items, LocalDate targetDate) {
        if (items.isEmpty()) {
            return List.of();
        }

        List<OpenForecastResponse.ForecastItem> dailyItems = filterByDate(items, targetDate);

        if (dailyItems.isEmpty()) {
            return List.of();
        }

        int startIndex;

        if (targetDate.equals(LocalDate.now(KOREA_ZONE))) {
            LocalDateTime now = LocalDateTime.now(KOREA_ZONE);
            startIndex = java.util.stream.IntStream.range(0, items.size())
                    .boxed()
                    .min(Comparator.comparingLong(index ->
                            Math.abs(Duration.between(now, parseForecastDateTime(items.get(index).dt_txt())).toMinutes())))
                    .orElse(0);
        }
        else {
            OpenForecastResponse.ForecastItem firstDailyItem = dailyItems.get(0);
            startIndex = items.indexOf(firstDailyItem);
        }

        int endIndex = Math.min(startIndex + 6, items.size());
        return items.subList(startIndex, endIndex);
    }

    private String firstDescription(OpenForecastResponse.ForecastItem item) {
        if (item.weather() == null || item.weather().isEmpty()) {
            return "-";
        }
        return item.weather().get(0).description();
    }

    private String firstIcon(OpenForecastResponse.ForecastItem item) {
        if (item.weather() == null || item.weather().isEmpty()) {
            return "01d";
        }
        return item.weather().get(0).icon();
    }

    private String iconUrl(String iconCode) {
        return "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
    }

    private String firstCurrentDescription(OpenWeatherResponse response) {
        if (response.weather() == null || response.weather().isEmpty()) {
            return "-";
        }
        return response.weather().get(0).description();
    }

    private String currentIconUrl(OpenWeatherResponse response) {
        if (response.weather() == null || response.weather().isEmpty() || response.weather().get(0).icon() == null) {
            return iconUrl("01d");
        }
        return iconUrl(response.weather().get(0).icon());
    }

    private String formatNow() {
        return LocalDateTime.now(KOREA_ZONE).format(CURRENT_TIME_FORMAT);
    }

    private String formatCurrentDateTime(String dtText) {
        return parseForecastDateTime(dtText).format(CURRENT_TIME_FORMAT);
    }

    private String formatDayLabel(String dtText) {
        return parseForecastDateTime(dtText).format(DAY_LABEL_FORMAT);
    }

    private String formatTimeLabel(String dtText) {
        return parseForecastDateTime(dtText).format(TIME_LABEL_FORMAT);
    }

    private LocalDateTime parseForecastDateTime(String dtText) {
        return LocalDateTime.parse(dtText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String formatEpochTime(Number epochSeconds) {
        if (epochSeconds == null) {
            return "-";
        }
        return Instant.ofEpochSecond(epochSeconds.longValue())
                .atZone(KOREA_ZONE)
                .format(TIME_LABEL_FORMAT);
    }

    private String temperatureText(Double temperature) {
        if (temperature == null) {
            return "-";
        }
        return Math.round(temperature) + "\u00b0";
    }

    private String percentText(Number value) {
        if (value == null) {
            return "-";
        }
        return value.longValue() + "%";
    }

    private String popText(Number value) {
        if (value == null) {
            return "-";
        }
        double number = value.doubleValue();
        if (number <= 1) {
            number *= 100;
        }
        return Math.round(number) + "%";
    }

    private String windSpeedText(Double speed) {
        if (speed == null) {
            return "-";
        }
        return String.format(Locale.US, "%.1f m/s", speed);
    }

    private String tempRangeText(OpenForecastResponse.ForecastItem item) {
        if (item.main() == null) {
            return "-";
        }
        return "최저 " + temperatureText(item.main().temp_min()) + " / 최고 " + temperatureText(item.main().temp_max());
    }

    private int chartXPercent(int index, int lastIndex) {
        if (lastIndex == 0) {
            return 50;
        }
        return (int) Math.round((index * 100.0) / lastIndex);
    }

    private int chartYPercent(Double temperature, double min, double range) {
        if (temperature == null) {
            return 60;
        }

        if (range == 0) {
            return 42;
        }

        double normalized = (temperature - min) / range;
        return (int) Math.round(78 - (normalized * 56));
    }
}
