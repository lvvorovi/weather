package com.meawallet.weather.deserializer.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meawallet.weather.util.WeatherDeserializerUtil;
import com.meawallet.weather.handler.exception.WeatherApiDtoCustomDeserializerException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.properties.WeatherProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.AIR_TEMPERATURE;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.COORDINATES;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.DATA;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.DESERIALIZER_CODEC_READ_FAIL_MESSAGE;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.DETAILS;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.GEOMETRY;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.INSTANT;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.PROPERTIES;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.TIME;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.TIMESERIES;

@Component
public class CustomStdDeserializer extends StdDeserializer<WeatherApiDto> {


    @Autowired
    private transient WeatherProperties properties;
    @Autowired
    private transient WeatherDeserializerUtil util;

    public CustomStdDeserializer() {
        this(null);
    }

    public CustomStdDeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public WeatherApiDto deserialize(JsonParser parser, DeserializationContext ctx) {
        WeatherApiDto requestDto = new WeatherApiDto();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ObjectCodec codec = parser.getCodec();

        try {
            JsonNode node = codec.readTree(parser);

            JsonNode geometryNode = node.get(GEOMETRY);
            Float lon = mapper.readValue(geometryNode.get(COORDINATES).get(0).toString(), Float.class);
            Float lat = mapper.readValue(geometryNode.get(COORDINATES).get(1).toString(), Float.class);
            Integer alt = mapper.readValue(geometryNode.get(COORDINATES).get(2).toString(), Integer.class);

            JsonNode propertiesNode = node.get(PROPERTIES);
            JsonNode timeSeriesNodeArray = mapper.readTree(propertiesNode.toString()).get(TIMESERIES);
            JsonNode currentHourNode = util.getCurrentNode(timeSeriesNodeArray, mapper, properties.getTimeDifference());

            LocalDateTime dateTime = mapper.readValue(currentHourNode.get(TIME).toString(), LocalDateTime.class);

            JsonNode dataNode = currentHourNode.get(DATA);
            JsonNode instantNode = dataNode.get(INSTANT);
            JsonNode detailsNode = instantNode.get(DETAILS);
            JsonNode temperatureNode = detailsNode.get(AIR_TEMPERATURE);
            Float temperature = Float.parseFloat(temperatureNode.asText());

            requestDto.setLon(lon);
            requestDto.setLat(lat);
            requestDto.setAltitude(alt);
            requestDto.setTimeStamp(dateTime.plusHours(properties.getTimeDifference()));
            requestDto.setTemperature(temperature);

        } catch (IOException e) {
            throw new WeatherApiDtoCustomDeserializerException(DESERIALIZER_CODEC_READ_FAIL_MESSAGE, e);
        }

        return requestDto;
    }
}
