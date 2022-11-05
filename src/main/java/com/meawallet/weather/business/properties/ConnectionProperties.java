package com.meawallet.weather.business.properties;

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
    private Max max;
    @NotNull
    private Timeout timeout;


    @Data
    @Validated
    public static class Max {
        @NotNull
        private int total;
        @NotNull
        private int root;
    }

    @Data
    @Validated
    public static class Timeout {
        @NotNull
        private int request;
        @NotNull
        private int socket;
        @NotNull
        private int connect;
        @NotNull
        private long keepAlive;
    }


}
