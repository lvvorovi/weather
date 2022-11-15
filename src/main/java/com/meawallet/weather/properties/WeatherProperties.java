package com.meawallet.weather.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "weather")
@Data
@Component
@Validated
public class WeatherProperties {

    @NotNull
    private String apiUrlCompact;
    @NotNull
    private long timeDifference;
    @NotNull
    private int entityTtlHours;
    @NotNull
    private String userAgentHeaderValue;
    @NotNull
    private int apiLatMaxDecimalValue;
    @NotNull
    private int apiLatMaxValue;
    @NotNull
    private int apiLatMinValue;
    @NotNull
    private int apiLonMaxDecimalValue;
    @NotNull
    private int apiLonMaxValue;
    @NotNull
    private int apiLonMinValue;
    @NotNull
    private int apiAltitudeMaxValue;
    @NotNull
    private int apiAltitudeMinValue;
    @NotNull
    private Boolean isMasterJvm;

}
