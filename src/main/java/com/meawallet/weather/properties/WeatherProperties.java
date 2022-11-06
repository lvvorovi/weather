package com.meawallet.weather.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
    private String schedulingDeleteCron;

}
