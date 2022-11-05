package com.meawallet.weather;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WeatherApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .properties("spring.config.name=config")
                .sources(WeatherApp.class)
                .run(args);
    }

    /*
     * TODO:
     *  - refactor @Value to class approach
     *  - refactor test moving all creations to util.
     *  - refactor test util to hardcode variable, don't take them from static values
     * */

}