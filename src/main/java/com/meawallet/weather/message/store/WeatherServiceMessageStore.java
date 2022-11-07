package com.meawallet.weather.message.store;

import java.time.LocalDateTime;

public class WeatherServiceMessageStore {

    private WeatherServiceMessageStore() {
    }

    public static String buildEntityNotFoundMessage(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp) {
        return "WeatherEntity with parameters 'lat'=" + lat + ", 'lon'=" + lon + ", 'altitude'=" + altitude +
                ", 'timestamp'=" + timeStamp + " was not found";
    }

    public static String buildEntityFoundMessage(String id) {
        return "WeatherEntity with id: " + id + " found in DB";
    }

    public static String buildEntityDeletedMessage(String id) {
        return "WeatherEntity with id: " + id + " deleted from DB";
    }

    public static String buildLatValueWasAdjustedMessage(Float from, Float to) {
        return "Request Lat value was adjusted from: " + from + " to: " + to;
    }

    public static String buildLonValueWasAdjustedMessage(Float from, Float to) {
        return "Request Lon value was adjusted from: " + from + " to: " + to;
    }

    public static String buildEntitySavedMessage(String id) {
        return "WeatherEntity saved with Id: " + id;
    }
}
