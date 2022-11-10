package com.meawallet.weather.message.store;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;

public class WeatherServiceFacadeMessageStore {

    private WeatherServiceFacadeMessageStore() {
    }

    public static String buildFindRequestMessage(Float lat, Float lon, Integer altitude) {
        return "Request with parameters: 'lat'=" + lat + ", 'lon'=" + lon + ", 'altitude'=" + altitude +
                " at:" + LocalDateTime.now().truncatedTo(HOURS);
    }

    public static String buildNotFoundWhileHasToBeFoundMessage(String exceptionMessage, Exception nestedException) {
        return "WeatherEntity was not found, while DB claims it is stored: " + exceptionMessage + " nested exception:" +
                " " +
                nestedException;
    }
}
