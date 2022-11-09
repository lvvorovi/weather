package com.meawallet.weather.business.message.store;

import java.time.LocalDateTime;

public class WeatherValidationMessageStore {

    private static final String API_RETURN_MESSAGE = "Weather API returned null value for ";
    public static final String WEATHER_API_LATITUDE_NULL_MESSAGE = API_RETURN_MESSAGE + "'lat'";
    public static final String WEATHER_API_LONGITUDE_NULL_MESSAGE = API_RETURN_MESSAGE + "'lon'";
    public static final String WEATHER_API_TEMPERATURE_NULL_MESSAGE = API_RETURN_MESSAGE + "'temperature'";
    public static final String WEATHER_API_TIMESTAMP_NULL_MESSAGE = API_RETURN_MESSAGE + "'timeStamp'";

    private WeatherValidationMessageStore() {
    }

    public static String buildApiDtoExistsMessage(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp) {
        return "Weather Entity already exists in DataBase for WeatherApiDto with parameters: 'lat'=" + lat +
                ", 'lon'=" + lon + ", 'altitude'=" + altitude + ", 'timeStamp'=" + timeStamp;
    }
}
