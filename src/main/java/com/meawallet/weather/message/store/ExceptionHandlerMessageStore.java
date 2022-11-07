package com.meawallet.weather.message.store;

import com.meawallet.weather.model.ErrorDto;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ExceptionHandlerMessageStore {

    private ExceptionHandlerMessageStore() {
    }

    public static String buildValidationFailedMessage(ErrorDto errorDto) {
        return "Validation failed. " + errorDto;
    }

    public static String buildApiCallFailedMessage(ErrorDto errorDto) {
        return "Api call failed. " + errorDto;
    }

    public static String buildAccessDeniedFailedMessage(ErrorDto errorDto) {
        return FORBIDDEN.getReasonPhrase() + errorDto;
    }

    public static String buildInternalServerErrorMessage(ErrorDto errorDto) {
        return INTERNAL_SERVER_ERROR.getReasonPhrase() + errorDto;
    }
}
