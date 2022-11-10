package com.meawallet.weather.handler;

import com.meawallet.weather.handler.exception.CustomInternalServerErrorException;
import com.meawallet.weather.model.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;

import static com.meawallet.weather.message.store.ExceptionHandlerMessageStore.buildInternalServerErrorMessage;
import static com.meawallet.weather.message.store.ExceptionHandlerMessageStore.buildValidationFailedMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex,
                                                                       HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(BAD_REQUEST, ex.getMessage(), request);
        log.warn(buildValidationFailedMessage(errorDto));
        return buildResponse(BAD_REQUEST, errorDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                              HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(BAD_REQUEST, ex.getMessage(), request);
        log.warn(buildValidationFailedMessage(errorDto));
        return buildResponse(BAD_REQUEST, errorDto);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex,
                                                                                  HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(BAD_REQUEST, ex.getMessage(), request);
        log.warn(buildValidationFailedMessage(errorDto));
        return buildResponse(BAD_REQUEST, errorDto);
    }

    @ExceptionHandler(CustomInternalServerErrorException.class)
    public ResponseEntity<ErrorDto> handleCustomInternalServerErrorException(CustomInternalServerErrorException ex,
                                                                             HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        log.error(buildInternalServerErrorMessage(errorDto));
        return buildResponse(INTERNAL_SERVER_ERROR, errorDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        log.error(buildInternalServerErrorMessage(errorDto) + "\n" + Arrays.toString(ex.getStackTrace()));
        return buildResponse(INTERNAL_SERVER_ERROR, errorDto);
    }

    private ResponseEntity<ErrorDto> buildResponse(HttpStatus status, ErrorDto errorDto) {
        return ResponseEntity
                .status(status.value())
                .contentType(APPLICATION_JSON)
                .body(errorDto);
    }

}