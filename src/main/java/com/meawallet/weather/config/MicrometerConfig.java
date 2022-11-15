package com.meawallet.weather.config;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class MicrometerConfig {

    @Bean
    public TimedAspect timedAspect(HikariDataSource dataSource, MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    @Qualifier("WeatherFindLatLonAltCounter")
    public Counter counter(MeterRegistry meterRegistry) {
        return Counter.builder("WeatherController.findByLatAndLonAndAlt visit counter")
                .description("Registers number of calls to the WeatherController.findByLatAndLonAndAlt")
                .register(meterRegistry);
    }
}