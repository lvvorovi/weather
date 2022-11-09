package com.meawallet.weather.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "connection")
@Data
@Component
@Validated
public class ConnectionProperties {

    @NotNull
    private int maxTotalConnections;
    @NotNull
    private int maxConnectionsPerRouteDefault;
    @NotNull
    private int connectionRequestTimeout;
    @NotNull
    private int socketTimeout;
    @NotNull
    private int connectTimeout;
    @NotNull
    private long connectionKeepAlive;

}
