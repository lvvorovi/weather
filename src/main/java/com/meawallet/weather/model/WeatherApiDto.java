package com.meawallet.weather.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class WeatherApiDto {

    private Float lat;

    private Float lon;

    private Float temperature;

    private Integer altitude;

    @DateTimeFormat(pattern = "yyyy-MM-ddTHH")
    private LocalDateTime timeStamp;

}
