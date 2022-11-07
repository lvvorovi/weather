package com.meawallet.weather.message.store;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;

public class WeatherServiceFacadeMessageStore {

    private WeatherServiceFacadeMessageStore() {
    }

    public static String buildFindRequestMessage(Float lat, Float lon, Integer altitude) {
        return "Request with parameters: 'lat'=" + lat + ", 'lon'=" + lon + ", 'altitude'=" + altitude +
                "at:" + LocalDateTime.now().truncatedTo(HOURS);
    }
}
