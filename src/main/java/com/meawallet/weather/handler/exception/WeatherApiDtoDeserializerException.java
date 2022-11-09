package com.meawallet.weather.handler.exception;

public class WeatherApiDtoDeserializerException extends CustomInternalServerErrorException {

    public WeatherApiDtoDeserializerException(String message) {
        super(message);
    }

}
