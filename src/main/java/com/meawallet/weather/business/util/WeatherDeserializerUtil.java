package com.meawallet.weather.business.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.meawallet.weather.model.WeatherApiDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.meawallet.weather.business.ConstantsStore.TIME;

public class WeatherDeserializerUtil {

    private WeatherDeserializerUtil() {
    }

    public static ObjectMapper getWeatherObjectMapper(JsonDeserializer<? extends WeatherApiDto> deserializer) {
        ObjectMapper weatherDtoMapper = new ObjectMapper();

        weatherDtoMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule(deserializer.getClass().toString());
        module.addDeserializer(WeatherApiDto.class, deserializer);
        weatherDtoMapper.registerModule(module);

        return weatherDtoMapper;
    }

    public static JsonNode getCurrentNode(JsonNode jsonNode, ObjectMapper mapper, Long hourDifference)
            throws JsonProcessingException {

        for (JsonNode jn : jsonNode) {
            LocalDateTime dateTime = mapper.readValue(jn.get(TIME).toString(), LocalDateTime.class);
            if (dateTime.equals(LocalDateTime.now().minusHours(hourDifference).truncatedTo(ChronoUnit.HOURS))) {
                return jn;
            }
        }

        return jsonNode.get(0);
    }

}
