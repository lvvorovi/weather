package com.meawallet.weather.business.handler.exception;

public class WeatherApiDtoCustomDeserializerException extends CustomInternalServerErrorException {

    public WeatherApiDtoCustomDeserializerException(String message) {
        super(message);
    }

    public WeatherApiDtoCustomDeserializerException(String message, Throwable cause) {
        super(message, cause);
    }
}
