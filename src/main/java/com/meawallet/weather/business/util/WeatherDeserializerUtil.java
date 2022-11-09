package com.meawallet.weather.business.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.meawallet.weather.model.WeatherApiDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.meawallet.weather.business.message.store.WeatherDeserializerMessageStore.TIME;
import static java.time.temporal.ChronoUnit.HOURS;

@Component
public class WeatherDeserializerUtil {

    public ObjectMapper getWeatherObjectMapper(JsonDeserializer<? extends WeatherApiDto> deserializer) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule(deserializer.getClass().toString());
        module.addDeserializer(WeatherApiDto.class, deserializer);
        mapper.registerModule(module);

        return mapper;
    }

    public JsonNode getCurrentNode(JsonNode jsonNode, ObjectMapper mapper, Long hourDifference) throws JsonProcessingException {
        for (JsonNode jn : jsonNode) {
            LocalDateTime dateTime = mapper.readValue(jn.get(TIME).toString(), LocalDateTime.class);
            if (dateTime.equals(LocalDateTime.now().minusHours(hourDifference).truncatedTo(HOURS))) {
                return jn;
            }
        }

        return jsonNode.get(0);
    }

}
