package com.meawallet.weather.business.deserializer.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meawallet.weather.business.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.business.handler.exception.WeatherApiDtoDeserializerException;
import com.meawallet.weather.model.WeatherApiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static com.meawallet.weather.business.ConstantsStore.AIR_TEMPERATURE;
import static com.meawallet.weather.business.ConstantsStore.COORDINATES;
import static com.meawallet.weather.business.ConstantsStore.DATA;
import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_CODEC_READ_FAIL_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_DATETIME_PARSE_FAIL_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_DESERIALIZED_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_FAIL_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_NODE_READ_FAIL_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_NULL_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.DETAILS;
import static com.meawallet.weather.business.ConstantsStore.GEOMETRY;
import static com.meawallet.weather.business.ConstantsStore.INSTANT;
import static com.meawallet.weather.business.ConstantsStore.PROPERTIES;
import static com.meawallet.weather.business.ConstantsStore.TIME;
import static com.meawallet.weather.business.ConstantsStore.TIMESERIES;
import static com.meawallet.weather.business.util.WeatherDeserializerUtil.getCurrentNode;
import static com.meawallet.weather.business.util.WeatherDeserializerUtil.getWeatherObjectMapper;

@Component
@Slf4j
public class YrWeatherDtoDeserializer extends StdDeserializer<WeatherApiDto> implements WeatherApiDtoDeserializer {

    @Value("${api.weather.time.difference}")
    private Long hourDifference;
    @Autowired
    private JavaTimeModule javaTimeModule;


    public YrWeatherDtoDeserializer() {
        this(null);
    }

    public YrWeatherDtoDeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public WeatherApiDto deserializeApiResponse(String jsonString) {
        ObjectMapper weatherDtoMapper = getWeatherObjectMapper(this);
        WeatherApiDto requestDto;

        try {
            requestDto = weatherDtoMapper.readValue(jsonString, WeatherApiDto.class);
        } catch (JsonProcessingException | NullPointerException e) {
            throw new WeatherApiDtoDeserializerException(DESERIALIZER_FAIL_MESSAGE + e.getMessage());
        }

        if (requestDto == null) {
            throw new WeatherApiDtoDeserializerException(DESERIALIZER_NULL_MESSAGE);
        }

        if (log.isDebugEnabled()) {
            log.debug(jsonString + DESERIALIZER_DESERIALIZED_MESSAGE + requestDto);
        }

        return requestDto;
    }

    @Override
    public WeatherApiDto deserialize(JsonParser parser, DeserializationContext ctx) {

        WeatherApiDto requestDto = new WeatherApiDto();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(javaTimeModule);
        ObjectCodec codec = parser.getCodec();

        try {
            JsonNode node = codec.readTree(parser);

            JsonNode geometryNode = node.get(GEOMETRY);
            Float lon = mapper.readValue(geometryNode.get(COORDINATES).get(0).toString(), Float.class);
            Float lat = mapper.readValue(geometryNode.get(COORDINATES).get(1).toString(), Float.class);
            Integer alt = mapper.readValue(geometryNode.get(COORDINATES).get(2).toString(), Integer.class);

            JsonNode propertiesNode = node.get(PROPERTIES);
            JsonNode timeseriesNodeArray = mapper.readTree(propertiesNode.toString()).get(TIMESERIES);

            JsonNode currentHourNode = getCurrentNode(timeseriesNodeArray, mapper, hourDifference);
            LocalDateTime dateTime = mapper.readValue(currentHourNode.get(TIME).toString(), LocalDateTime.class);

            JsonNode dataNode = currentHourNode.get(DATA);
            JsonNode instantNode = dataNode.get(INSTANT);
            JsonNode detailsNode = instantNode.get(DETAILS);

            JsonNode temperatureNode = detailsNode.get(AIR_TEMPERATURE);
            Float temperature = Float.parseFloat(temperatureNode.asText());

            requestDto.setLon(lon);
            requestDto.setLat(lat);
            requestDto.setAltitude(alt);
            requestDto.setTimeStamp(dateTime.plusHours(hourDifference));
            requestDto.setTemperature(temperature);

        } catch (JsonProcessingException e) {
            throw new WeatherApiDtoDeserializerException(DESERIALIZER_NODE_READ_FAIL_MESSAGE, e);
        } catch (IOException e) {
            throw new WeatherApiDtoDeserializerException(DESERIALIZER_CODEC_READ_FAIL_MESSAGE, e);
        } catch (DateTimeParseException e) {
            throw new WeatherApiDtoDeserializerException(DESERIALIZER_DATETIME_PARSE_FAIL_MESSAGE, e);
        }

        return requestDto;
    }

}
