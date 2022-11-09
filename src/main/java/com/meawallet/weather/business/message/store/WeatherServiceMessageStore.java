package com.meawallet.weather.business.message.store;

import java.time.LocalDateTime;

public class WeatherServiceMessageStore {

    private WeatherServiceMessageStore() {
    }

    public static String buildNotFoundMessage(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp) {
        return "WeatherEntity with parameters 'lat'=" + lat + ", 'lon'=" + lon + ", 'altitude'=" + altitude +
                ", 'timestamp'=" + timeStamp + " was not found";
    }

    public static String buildFoundMessage(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp) {
        return "WeatherEntity with parameters 'lat'=" + lat + ", 'lon'=" + lon + ", 'altitude'=" + altitude +
                ", 'timestamp'=" + timeStamp + " found in DB";
    }

    public static String buildDeletedMessage(String id) {
        return "WeatherEntity with id: " + id + " deleted from DB";
    }

    public static String buildLatValueWasAdjustedMessage(Float from, Float to) {
        return "Request 'lat' value was adjusted from: " + from + " to: " + to;
    }

    public static String buildLonValueWasAdjustedMessage(Float from, Float to) {
        return "Request 'lon' value was adjusted from: " + from + " to: " + to;
    }

    public static String buildSavedMessage(String id) {
        return "WeatherEntity saved with Id: " + id;
    }
}
