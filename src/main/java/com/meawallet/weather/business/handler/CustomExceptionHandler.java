package com.meawallet.weather.business.handler;

import com.meawallet.weather.business.handler.exception.CustomInternalServerErrorException;
import com.meawallet.weather.business.handler.exception.ValidationException;
import com.meawallet.weather.model.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

import static com.meawallet.weather.business.ConstantsStore.API_CALL_FAILED;
import static com.meawallet.weather.business.ConstantsStore.VALIDATION_FAILED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                              HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(BAD_REQUEST, ex.getMessage(), request);

        log.warn(VALIDATION_FAILED + errorDto);

        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(errorDto);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex,
                                                                                  HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(BAD_REQUEST, ex.getMessage(), request);

        log.warn(VALIDATION_FAILED + errorDto);

        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(errorDto);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> handleValidationException(ValidationException ex, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(BAD_REQUEST, ex.getMessage(), request);

        log.warn(VALIDATION_FAILED + errorDto);

        return ResponseEntity
                .status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(errorDto);
    }

    @ExceptionHandler(CustomInternalServerErrorException.class)
    public ResponseEntity<ErrorDto> handleWeatherApiServiceException(CustomInternalServerErrorException ex,
                                                                     HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(INTERNAL_SERVER_ERROR, ex.getMessage(), request);

        log.warn(API_CALL_FAILED + errorDto);

        return ResponseEntity
                .internalServerError()
                .contentType(APPLICATION_JSON)
                .body(errorDto);
    }

    @ExceptionHandler(Error.class)
    public ResponseEntity<ErrorDto> handleError(Error ex, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(INTERNAL_SERVER_ERROR, ex.getMessage(), request);

        log.error(VALIDATION_FAILED + errorDto);
        ex.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .contentType(APPLICATION_JSON)
                .body(errorDto);
    }

}