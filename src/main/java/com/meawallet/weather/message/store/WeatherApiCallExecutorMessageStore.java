package com.meawallet.weather.message.store;

public class WeatherApiCallExecutorMessageStore {

    private WeatherApiCallExecutorMessageStore() {
    }

    public static String buildApiCallExceptionMessage(String message) {
        return "Exception caught while contacting external api. Exception message: " + message;
    }

}
