package com.meawallet.weather.message.store;

import com.meawallet.weather.repository.entity.WeatherEntity;

import java.time.LocalDateTime;
import java.util.List;

public class WeatherServiceMessageStore {

    public static final String LAT = "'lat'=";
    public static final String LON = ", 'lon'=";
    public static final String ALTITUDE = ", 'altitude'=";
    public static final String TIME_STAMP = ", 'timeStamp'=";

    private WeatherServiceMessageStore() {
    }

    public static String buildDtoNotFoundMessage(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp) {
        return "WeatherResponseDto with parameters:" +
                LAT + lat +
                LON + lon +
                ALTITUDE + altitude +
                TIME_STAMP + timeStamp +
                " was not found";
    }

    public static String buildDtoFoundMessage(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp) {
        return "WeatherResponseDto with parameters:" +
                LAT + lat +
                LON + lon +
                ALTITUDE + altitude +
                TIME_STAMP + timeStamp +
                " found in DB";
    }

    public static String buildDeletedMessage(List<WeatherEntity> deletedEntityList) {
        StringBuilder result = new StringBuilder();
        deletedEntityList.forEach(entity ->
                result.append("\n")
                        .append("WeatherEntity with id: ")
                        .append(entity.getId())
                        .append(" deleted from DB"));
        return result.toString();
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
        return "WeatherEntity with parameters:" +
                LAT + entity.getLat() +
                LON + entity.getLon() +
                ALTITUDE + entity.getAltitude() +
                TIME_STAMP + entity.getTimeStamp() +
                " already exists in DB. Check if it was saved by another thread or service";
    }
}
