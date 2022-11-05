package com.meawallet.weather.util;

import com.meawallet.weather.business.repository.entity.WeatherEntity;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.web.controller.impl.WeatherControllerImpl;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_API_HEADER_USER_AGENT_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class WeatherTestUtil {

    //Request Parameters
    public static final Float LAT = 58.321456F;
    public static final Float LON = 24.987654F;
    public static final Integer ALTITUDE = 10;

    //Controller Test urls
    public static final URI FIND_BY_LAT_AND_LON_AND_ALT_URL =
            linkTo(methodOn(WeatherControllerImpl.class).findByLatAndLonAndAlt(LAT, LON, ALTITUDE)).toUri();
    public static final URI FIND_BY_LAT_AND_LON_AND_ALT_URL_MISSING_REQUIRED_PARAM =
            linkTo(WeatherControllerImpl.class).toUri();
    public static final String FIND_BY_LAT_AND_LON_AND_ALT_URL_WRONG_TYPE_PARAM =
            linkTo(WeatherControllerImpl.class).toUri().toString().concat("?lat=a&lot=b");

    //API Service Test urls
    public static final String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact";
    public static final String WEATHER_API_URL_NO_ALT_PARAM = "?lat=" + LAT + "&lon=" + LON;
    public static final String WEATHER_API_URL_ALL_PARAMS = WEATHER_API_URL_NO_ALT_PARAM + "&altitude=" + ALTITUDE;
    public static final String WEATHER_API_URL_WITH_ALL_PARAMS = WEATHER_API_URL + WEATHER_API_URL_ALL_PARAMS;
    public static final String WEATHER_API_URL_WITH_NO_ALT_PARAM = WEATHER_API_URL + WEATHER_API_URL_NO_ALT_PARAM;


    //Variables
    public static WeatherResponseDto weatherResponseDto() {
        WeatherResponseDto dto = new WeatherResponseDto();
        dto.setTemperature(10.01F);
        return dto;
    }

    public static WeatherApiDto weatherApiDto() {
        WeatherApiDto dto = new WeatherApiDto();
        dto.setTemperature(10.1F);
        dto.setLat(10F);
        dto.setAltitude(10);
        dto.setLon(10F);
        dto.setTimeStamp(LocalDateTime.now());
        return dto;
    }

    public static WeatherEntity weatherEntity() {
        WeatherEntity entity = new WeatherEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setTemperature(10.1F);
        entity.setLat(10F);
        entity.setAltitude(10);
        entity.setLon(10F);
        entity.setTimeStamp(LocalDateTime.now());
        return entity;
    }

    public static Map<String, String> getUrlParamsWithAlt() {
        Map<String, String> params = new HashMap<>();
        params.put("lat", LAT.toString());
        params.put("lon", LON.toString());
        params.put("altitude", ALTITUDE.toString());
        return params;
    }

    public static Map<String, String> getUrlParamsNoALt() {
        Map<String, String> params = getUrlParamsWithAlt();
        params.remove("altitude");
        return params;
    }

    public static HttpHeaders getRequiredHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_AGENT, WEATHER_API_HEADER_USER_AGENT_VALUE);
        headers.add(ACCEPT, APPLICATION_JSON_VALUE);
        return headers;
    }


}
