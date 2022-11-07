package com.meawallet.weather.handler.exception;

public class WeatherApiDtoCustomDeserializerException extends CustomInternalServerErrorException {

    public WeatherApiDtoCustomDeserializerException(String message) {
        super(message);
    }

    public WeatherApiDtoCustomDeserializerException(String message, Throwable cause) {
        super(message, cause);
    }
}
