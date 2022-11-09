package com.meawallet.weather.handler.exception;

public class WeatherEntityAlreadyExistsException extends CustomInternalServerErrorException {

    public WeatherEntityAlreadyExistsException(String message) {
        super(message);
    }

}
