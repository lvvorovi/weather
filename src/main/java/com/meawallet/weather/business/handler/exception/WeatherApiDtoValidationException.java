package com.meawallet.weather.business.handler.exception;

public class WeatherApiDtoValidationException extends CustomInternalServerErrorException {

    public WeatherApiDtoValidationException(String message) {
        super(message);
    }

}
