package com.meawallet.weather.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorDto {

    private int code;
    private String error;
    private String message;
    private LocalDateTime timeStamp;
    private String uri;
    private String method;

    public ErrorDto(HttpStatus status, String message, HttpServletRequest request) {
        this.code = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.uri = request.getRequestURI();
        this.method = request.getMethod();
        this.timeStamp = LocalDateTime.now();
    }
}
