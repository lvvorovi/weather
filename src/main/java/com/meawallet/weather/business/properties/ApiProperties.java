package com.meawallet.weather.business.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "api")
@Data
@Component
@Validated
public class ApiProperties {

    @NotNull
    private String url;
    @NotNull
    private Long timeDifference;
}
