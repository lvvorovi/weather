package com.meawallet.weather.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@Validated
@Data
@ConfigurationProperties(prefix = "task-scheduler")
public class TaskSchedulerProperties {

    @NotNull
    private int poolSize;
    @NotNull
    private String threadNamePrefix;

}
