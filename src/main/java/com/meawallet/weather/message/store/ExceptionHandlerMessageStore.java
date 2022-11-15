package com.meawallet.weather.message.store;

import com.meawallet.weather.model.ErrorDto;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class ExceptionHandlerMessageStore {

    private ExceptionHandlerMessageStore() {
    }

    public static String buildValidationFailedMessage(ErrorDto errorDto) {
        return "Validation failed. " + errorDto;
    }

    public static String buildInternalServerErrorMessage(ErrorDto errorDto) {
        return INTERNAL_SERVER_ERROR.getReasonPhrase() + ". " + errorDto;
    }

    public static String buildRuntimeExceptionMessage(ErrorDto errorDto, Throwable throwable) {
        return buildInternalServerErrorMessage(errorDto) +
                "\n" +
                Arrays.toString(throwable.getStackTrace());
    }
}
