package com.meawallet.weather.handler.exception;

public class WeatherEntityNotFoundWhenStoredException extends CustomInternalServerErrorException {

    public WeatherEntityNotFoundWhenStoredException(String message) {
        super(message);
    }

}
