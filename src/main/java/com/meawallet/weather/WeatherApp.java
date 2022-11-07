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
     *  - Auth Server
     *  - Make your code deliverable.
     *  - Concurrency?
     *  - Prometheus monitoring?
     * */

}