package com.example.demo.weather.service;

import com.example.demo.weather.config.WeatherProperties;
import com.example.demo.weather.dto.WeatherApiResponse;
import com.example.demo.weather.dto.WeatherPageView;
import com.example.demo.weather.exception.WeatherApiException;
import com.example.demo.weather.exception.WeatherConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
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

    public WeatherApiResponse getForecast(String city) {
        validateApiKey();
        String query = StringUtils.hasText(city) ? city.trim() : weatherProperties.getDefaultCity();
        return requestForecast(query);
    }

    public WeatherPageView getWeatherPage(String city, LocalDate targetDate) {
        validateApiKey();

        String query = StringUtils.hasText(city) ? city.trim() : weatherProperties.getDefaultCity();
        WeatherApiResponse response = requestForecast(query);

        List<WeatherApiResponse.Hour> visibleItems = selectVisibleItems(response, targetDate);

        if (visibleItems.isEmpty()) {
            throw new WeatherApiException("선택한 날짜의 예보 데이터가 없습니다");
        }

        WeatherApiResponse.Current current = response.current();
        WeatherApiResponse.ForecastDay dayForecast = findTargetDayForecast(response.forecast().forecastDay(), targetDate);

        return new WeatherPageView(
                response.location().name(),
                response.location().country(),
                formatNow(),
                current.condition().text(),
                ensureHttps(current.condition().icon()),
                temperatureText(current.tempC()),
                temperatureText(current.feelsLikeC()),
                temperatureText(dayForecast.day().maxTempC()),
                temperatureText(dayForecast.day().minTempC()),
                percentText(current.humidity()),
                windSpeedText(current.windKph() / 3.6),
                percentText(dayForecast.day().dailyChanceOfRain()),
                dayForecast.astro().sunrise(),
                dayForecast.astro().sunset(),
                Math.round(current.airQuality().pm10()) + "㎍/㎥",
                Math.round(current.airQuality().pm25()) + "㎍/㎥",
                getAqiGrade(current.airQuality().pm10()),
                buildTemperatureChartAreaPoints(visibleItems),
                buildTemperatureChartPolyline(visibleItems),
                buildTemperatureChart(visibleItems),
                buildForecastSlots(visibleItems)
        );
    }

    private String getAqiGrade(double pm10) {
        if (pm10 <= 30) return "좋음";
        if (pm10 <= 80) return "보통";
        if (pm10 <= 150) return "나쁨";
        return "매우나쁨";
    }

    private void validateApiKey() {
        if (!StringUtils.hasText(weatherProperties.getApiKey())) {
            throw new WeatherConfigurationException("weather.api.api-key is not configured");
        }
    }

    private WeatherApiResponse requestForecast(String query) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(weatherProperties.getBaseUrl())
                .path("/forecast.json")
                .queryParam("key", weatherProperties.getApiKey())
                .queryParam("q", query)
                .queryParam("days", 3)
                .queryParam("aqi", "yes")
                .queryParam("alerts", "no")
                .queryParam("lang", weatherProperties.getLang())
                .encode()
                .build()
                .toUri();

        try {
            return weatherRestTemplate.getForObject(uri, WeatherApiResponse.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new WeatherApiException("도시를 찾을 수 없습니다: " + query, ex);
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new WeatherApiException("API 키가 올바르지 않습니다.", ex);
        } catch (RestClientException ex) {
            throw new WeatherApiException("날씨 정보를 가져오는 데 실패했습니다.", ex);
        }
    }

    private List<WeatherApiResponse.Hour> selectVisibleItems(WeatherApiResponse response, LocalDate targetDate) {
        List<WeatherApiResponse.ForecastDay> forecastDays = response.forecast().forecastDay();
        
        // 모든 날짜의 hour 데이터를 하나의 리스트로 통합
        List<WeatherApiResponse.Hour> allHours = forecastDays.stream()
                .flatMap(day -> day.hour().stream())
                .toList();

        LocalDateTime now = LocalDateTime.now(KOREA_ZONE);
        
        // 현재 시간(또는 선택한 날짜의 시작 시간)과 가장 가까운 인덱스 찾기
        int startIndex = 0;
        long minDiff = Long.MAX_VALUE;

        for (int i = 0; i < allHours.size(); i++) {
            LocalDateTime hourTime = parseDateTime(allHours.get(i).time());
            // 오늘인 경우 현재 시간 이후부터, 다른 날짜인 경우 해당 날짜 00시부터
            LocalDateTime compareTime = targetDate.equals(LocalDate.now(KOREA_ZONE)) ? now : targetDate.atStartOfDay();
            
            long diff = java.time.Duration.between(compareTime, hourTime).toMinutes();
            if (diff >= -30 && diff < minDiff) { // 과거 30분 이내 데이터부터 포함
                minDiff = diff;
                startIndex = i;
            }
        }

        return allHours.subList(startIndex, allHours.size());
    }

    private WeatherApiResponse.ForecastDay findTargetDayForecast(List<WeatherApiResponse.ForecastDay> days, LocalDate targetDate) {
        return days.stream()
                .filter(d -> LocalDate.parse(d.date()).equals(targetDate))
                .findFirst()
                .orElse(days.get(0));
    }

    private List<WeatherPageView.ForecastSlotView> buildForecastSlots(List<WeatherApiResponse.Hour> items) {
        return items.stream()
                .map(item -> {
                    double t = item.tempC();
                    // 절대적 비율 (영하 10도 0% ~ 영상 40도 100% 로 매핑)
                    int percent = (int) Math.round(((t + 10) / 50.0) * 100);
                    percent = Math.max(5, Math.min(95, percent)); // 상하한선

                    // 온도 구간별 클래스 (상온 15~25도: mild/하늘색)
                    String tClass = "temp-cool"; // 15도 미만: 차가움
                    if (t >= 15 && t < 25) tClass = "temp-mild"; // 15~25도: 상온 (하늘색)
                    else if (t >= 25) tClass = "temp-warm"; // 25도 이상: 따뜻함

                    return new WeatherPageView.ForecastSlotView(
                        formatDayLabel(item.time()),
                        formatTimeLabel(item.time()),
                        item.condition().text(),
                        ensureHttps(item.condition().icon()),
                        temperatureText(t),
                        "-",
                        percentText(item.chanceOfRain()),
                        windSpeedText(item.windKph() / 3.6),
                        percent,
                        tClass
                    );
                })
                .toList();
    }

    private List<WeatherPageView.TemperaturePointView> buildTemperatureChart(List<WeatherApiResponse.Hour> items) {
        List<Double> temperatures = items.stream().map(WeatherApiResponse.Hour::tempC).toList();
        double min = temperatures.stream().min(Comparator.naturalOrder()).orElse(0.0);
        double max = temperatures.stream().max(Comparator.naturalOrder()).orElse(min);
        
        double range = max - min;
        if (range < 5.0) {
            double padding = (5.0 - range) / 2.0;
            min -= padding;
            max += padding;
            range = 5.0;
        }

        int lastIndex = Math.max(items.size() - 1, 1);
        final double finalMin = min;
        final double finalRange = range;

        return java.util.stream.IntStream.range(0, items.size())
                .mapToObj(index -> {
                    WeatherApiResponse.Hour item = items.get(index);
                    return new WeatherPageView.TemperaturePointView(
                            formatTimeLabel(item.time()),
                            temperatureText(item.tempC()),
                            (int) Math.round((index * 100.0) / lastIndex),
                            (int) Math.round(78 - (((item.tempC() - finalMin) / finalRange) * 56))
                    );
                })
                .toList();
    }

    private String buildTemperatureChartPolyline(List<WeatherApiResponse.Hour> items) {
        return buildTemperatureChart(items).stream()
                .map(point -> point.xPercent() + "," + point.yPercent())
                .reduce((left, right) -> left + " " + right)
                .orElse("");
    }

    private String buildTemperatureChartAreaPoints(List<WeatherApiResponse.Hour> items) {
        String linePoints = buildTemperatureChartPolyline(items);
        return linePoints.isEmpty() ? "" : "0,100 " + linePoints + " 100,100";
    }

    private String ensureHttps(String url) {
        if (url == null) return null;
        return url.startsWith("//") ? "https:" + url : url;
    }

    private String formatNow() {
        return LocalDateTime.now(KOREA_ZONE).format(CURRENT_TIME_FORMAT);
    }

    private String formatDayLabel(String timeStr) {
        LocalDateTime dateTime = parseDateTime(timeStr);
        LocalDate date = dateTime.toLocalDate();
        LocalDate today = LocalDate.now(KOREA_ZONE);

        if (date.equals(today)) return "오늘";
        if (date.equals(today.plusDays(1))) return "내일";
        if (date.equals(today.plusDays(2))) return "모레";

        return date.format(DAY_LABEL_FORMAT);
    }

    private String formatTimeLabel(String timeStr) {
        return parseDateTime(timeStr).format(TIME_LABEL_FORMAT);
    }

    private LocalDateTime parseDateTime(String timeStr) {
        return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private String temperatureText(Double temp) {
        return Math.round(temp) + "°";
    }

    private String percentText(int value) {
        return value + "%";
    }

    private String windSpeedText(double speedMs) {
        return String.format(Locale.US, "%.1f m/s", speedMs);
    }
}
