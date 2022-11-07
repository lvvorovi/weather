package com.meawallet.weather.handler.exception;

public class WeatherApiDtoValidationException extends CustomInternalServerErrorException {

    public WeatherApiDtoValidationException(String message) {
        super(message);
    }

}
