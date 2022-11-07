package com.meawallet.weather.message.store;

import org.springframework.http.HttpStatus;

public class WeatherApiServiceMessageStore {

    public static final String WEATHER_API_PARAM_LAT = "lat";
    public static final String WEATHER_API_PARAM_LON = "lon";
    public static final String WEATHER_API_PARAM_ALT = "altitude";

    private WeatherApiServiceMessageStore() {
    }

    public static String buildApiCallExceptionMessage(String message) {
        return "Exception caught while contacting external api. Exception message: " + message;
    }

    public static String buildApiCallMessage(String url) {
        return "Calling Weather API: " + url;
    }

    public static String buildApiResponseMessage(String response) {
        return "Weather API response: " + response;
    }

    public static String buildApiInvalidRequestMessage(String message) {
        return "Invalid request. Details: " + message;
    }

    public static String buildApiNoResponseMessage(HttpStatus status) {
        return "Weather API returned no body. Status code: " + status.value();
    }
}
