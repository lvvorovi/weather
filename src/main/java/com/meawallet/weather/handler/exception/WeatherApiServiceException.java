package com.meawallet.weather.handler.exception;

public class WeatherApiServiceException extends CustomInternalServerErrorException {

    public WeatherApiServiceException(String message) {
        super(message);
    }

    public WeatherApiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
