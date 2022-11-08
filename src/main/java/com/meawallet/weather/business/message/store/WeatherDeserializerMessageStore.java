package com.meawallet.weather.business.message.store;

public class WeatherDeserializerMessageStore {

    public static final String TIME = "time";
    public static final String DATA = "data";
    public static final String INSTANT = "instant";
    public static final String DETAILS = "details";
    public static final String AIR_TEMPERATURE = "air_temperature";
    public static final String GEOMETRY = "geometry";
    public static final String COORDINATES = "coordinates";
    public static final String PROPERTIES = "properties";
    public static final String TIMESERIES = "timeseries";
    public static final String DESERIALIZER_CODEC_READ_FAIL_MESSAGE = "Exception while reading three by codec";


    private WeatherDeserializerMessageStore() {
    }

    public static String buildDeserializerFailMessage(String request) {
        return "Could not deserialize json: " + request;
    }

    public static String buildDeserializerNullResponseMessage(String jsonString) {
        return "Deserializer returned null for json: " + jsonString;
    }

    public static String buildYrDeserializerStartMessage(String json) {
        return "YrDeserializer begin deserialization of json: " + json;
    }

    public static String buildYrDeserializerEndMessage(String result) {
        return "YrDeserializer deserialization resul is: " + result;
    }
}
