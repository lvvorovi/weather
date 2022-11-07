package com.meawallet.weather.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meawallet.weather.business.repository.entity.WeatherEntity;
import com.meawallet.weather.controller.impl.WeatherControllerImpl;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class WeatherTestUtil {


    public static final Float LAT = 56.948112F;
    public static final Float LON = 24.108332F;
    public static final Integer ALTITUDE = 90000;
    public static final Float PRECISE_LAT = 56.9481F;
    public static final Float PRECISE_LON = 24.1083F;
    public static final Integer PRECISE_ALTITUDE = 9000;
    public static final int maxTotalConnections = 50;
    public static final int maxConnectionsPerRouteDefault = 20;
    public static final int connectionRequestTimeout = 5000;
    public static final int socketTimeout = 5000;
    public static final int connectTimeout = 5000;


    //Controller Test urls
    public static final String WEATHER_CONTROLLER_FIND_URL_WITH_PARAMS =
            linkTo(methodOn(WeatherControllerImpl.class).findByLatAndLonAndAlt(LAT, LON, ALTITUDE)).toUri().toString();
    public static final String WEATHER_CONTROLLER_FIND_URL_MISSING_REQUIRED_PARAMS =
            linkTo(WeatherControllerImpl.class).toUri().toString();
    public static final String WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS =
            WEATHER_CONTROLLER_FIND_URL_MISSING_REQUIRED_PARAMS
                    .concat("?lat=" + PRECISE_LAT + "&lon=" + PRECISE_LON + "&altitude=" + PRECISE_ALTITUDE);
    public static final String WEATHER_CONTROLLER_FIND_URL_WRONG_TYPE_PARAMS =
            linkTo(WeatherControllerImpl.class).toUri().toString().concat("?lat=a&lon=b");


    //API Service Test urls
    public static final String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact";
    public static final String WEATHER_API_URL_NO_ALT_PARAM = "?lat=" + LAT + "&lon=" + LON;
    public static final String WEATHER_API_URL_ALL_PARAMS = WEATHER_API_URL_NO_ALT_PARAM + "&altitude=" + ALTITUDE;
    public static final String WEATHER_API_URL_WITH_ALL_PARAMS = WEATHER_API_URL + WEATHER_API_URL_ALL_PARAMS;
    public static final String WEATHER_API_URL_WITH_NO_ALT_PARAM = WEATHER_API_URL + WEATHER_API_URL_NO_ALT_PARAM;
    public static final String WEATHER_API_URL_NO_ALT_PRECISE_PARAMS = "?lat=" + PRECISE_LAT + "&lon=" + PRECISE_LON;
    public static final String WEATHER_API_URL_ALL_PRECISE_PARAMS = WEATHER_API_URL_NO_ALT_PRECISE_PARAMS +
            "&altitude=" + PRECISE_ALTITUDE;
    public static final String WEATHER_API_URL_WITH_ALL_PRECISE_PARAMS =
            WEATHER_API_URL + WEATHER_API_URL_ALL_PRECISE_PARAMS;


    //Json node
    public static final String CURRENT_HOUR_NODE_TEMPERATURE = "10.8";
    public static final String CURRENT_HOUR_NODE_STRING =
            "{\"time\":\"" + currentTimeTruncatedToHours().minusHours(2) + "\",\"data" + "\":" + " " +
                    "{\"instant\":{\"details\": {\"air_pressure_at_sea_level\": 1016.8,\"air_temperature\": " +
                    CURRENT_HOUR_NODE_TEMPERATURE + ",\"cloud_area_fraction\": 99.9,\"relative_humidity\": 94.8," +
                    "\"wind_from_direction\": 228.4,\"wind_speed\": 2.2}},\"next_12_hours\": {\"summary\": " +
                    "{\"symbol_code\": \"cloudy\"}}," + "\"next_1_hours\": {\"summary\": {\"symbol_code\": " +
                    "\"cloudy\"},\"details\": {\"precipitation_amount\": 0.0}},\"next_6_hours\": {\"summary\": " +
                    "{\"symbol_code\": " + "\"cloudy\"}," + "\"details\": {\"precipitation_amount\": 0.0}}}}\n";
    public static final String ARRAY_NODE_STRING = "[" + CURRENT_HOUR_NODE_STRING + ",{\"time\": " + "\"2022-11-03T09"
            + ":00:00Z\",\"data\": " + "{\"instant\": {\"details\": {\"air_pressure_at_sea_level\": 1017.0," +
            "\"air_temperature\": 11" + ".1,\"cloud_area_fraction\": 99.8,\"relative_humidity\": 93.7," +
            "\"wind_from_direction\": 246" + ".2,\"wind_speed\": 2.9}},\"next_12_hours\": {\"summary\": " +
            "{\"symbol_code\": " + "\"cloudy\"}}," + "\"next_1_hours\": {\"summary\": {\"symbol_code\": " + "\"cloudy"
            + "\"},\"details\": " + "{\"precipitation_amount\": 0.0}},\"next_6_hours\": {\"summary\": " +
            "{\"symbol_code\": \"cloudy\"}," + "\"details\": {\"precipitation_amount\": 0.0}}}}]";
    public static final String COMPLETE_NODE_STRING = "{\"type\": \"Feature\",\"geometry\": {\"type\": \"Point\"," +
            "\"coordinates\": [" + PRECISE_LON + "," + PRECISE_LAT + "," + PRECISE_ALTITUDE + "]},\"properties\": " +
            "{\"meta\": {\"updated_at\": \"2022-11-03T08:38:17Z\",\"units\": {\"air_pressure_at_sea_level\": \"hPa\"," +
            "\"air_temperature\": \"celsius\",\"cloud_area_fraction\": \"%\",\"precipitation_amount\": \"mm\"," +
            "\"relative_humidity\":\"%\",\"wind_from_direction\": \"degrees\",\"wind_speed\": \"m/s\"}}," +
            "\"timeseries\":" + ARRAY_NODE_STRING + "}}";


    //Variables

    public static WeatherResponseDto weatherResponseDto() {
        WeatherResponseDto dto = new WeatherResponseDto();
        dto.setTemperature(10.01F);
        return dto;
    }

    public static WeatherApiDto weatherApiDto() {
        WeatherApiDto dto = new WeatherApiDto();
        dto.setTemperature(10.1F);
        dto.setLat(LAT);
        dto.setAltitude(ALTITUDE);
        dto.setLon(LON);
        dto.setTimeStamp(LocalDateTime.now());
        return dto;
    }

    public static WeatherEntity weatherEntity() {
        WeatherEntity entity = new WeatherEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setTemperature(10.1F);
        entity.setLat(LAT);
        entity.setAltitude(ALTITUDE);
        entity.setLon(LON);
        entity.setTimeStamp(LocalDateTime.now());
        return entity;
    }

    public static WeatherEntity weatherEntityPreciseParams() {
        WeatherEntity entity = new WeatherEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setTemperature(10.1F);
        entity.setLat(PRECISE_LAT);
        entity.setAltitude(PRECISE_ALTITUDE);
        entity.setLon(PRECISE_LON);
        entity.setTimeStamp(LocalDateTime.now().truncatedTo(HOURS));
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
        headers.add(USER_AGENT, "UniqueUserAgent");
        headers.add(ACCEPT, APPLICATION_JSON_VALUE);
        return headers;
    }

    public static JsonNode stringToJsonNode(String string) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readTree(string);
    }

    public static LocalDateTime currentTimeTruncatedToHours() {
        return LocalDateTime.now().truncatedTo(HOURS);
    }
}
