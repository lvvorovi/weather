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
     *  - Prometheus monitoring with some custom metrics related to cache or performance
     *  - Make your code deliverable. Please use Gradle (or Maven) in order to be able to build artefacts and to
     *      launch unit tests
     * */

}