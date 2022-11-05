package com.meawallet.weather.business.handler.exception;

public class WeatherApiServiceException extends CustomInternalServerErrorException {

    public WeatherApiServiceException(String message) {
        super(message);
    }

    public WeatherApiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
