package com.meawallet.weather.business.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather")
@Data
public class WeatherEntity {

    @Id
    private String id;

    @Column(name = "latitude")
    private Float lat;

    @Column(name = "longitude")
    private Float lon;

    @Column(name = "altitude")
    private Integer altitude;

    @Column(name = "temperature")
    private Float temperature;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;
}
