package com.meawallet.weather.business.message.store;

import com.meawallet.weather.business.repository.entity.WeatherEntity;

import java.time.LocalDateTime;

public class WeatherServiceMessageStore {

    private WeatherServiceMessageStore() {
    }

    public static String buildNotFoundMessage(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp) {
        return "WeatherEntity with parameters: 'lat'=" + lat + ", 'lon'=" + lon + ", 'altitude'=" + altitude +
                ", 'timestamp'=" + timeStamp + " was not found";
    }

    public static String buildFoundMessage(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp) {
        return "WeatherEntity with parameters: 'lat'=" + lat + ", 'lon'=" + lon + ", 'altitude'=" + altitude +
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

    public static String buildAlreadyExistsMessage(WeatherEntity entity) {
        return "WeatherEntity with parameters: 'lat'=" + entity.getLat() + ", 'lon'=" + entity.getLon() +
                ", 'altitude'=" + entity.getAltitude() + ", 'timestamp'=" + entity.getTimeStamp() +
                " already exists in DB. Check if it was saved by another thread or service";
    }
}
