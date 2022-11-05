package com.meawallet.weather.business;

public class ConstantsStore {

    //ExceptionHandler logs
    public static final String VALIDATION_FAILED = "Validation failed. ";
    public static final String API_CALL_FAILED = "Api call failed. ";


    //Weather API DTO Validation logs
    public static final String WEATHER_API_LATITUDE_NULL_MESSAGE = "Weather API returned null value for latitude";
    public static final String WEATHER_API_LONGITUDE_NULL_MESSAGE = "Weather API returned null value for longitude";
    public static final String WEATHER_API_TEMPERATURE_NULL_MESSAGE = "Weather API returned null value for temperature";
    public static final String WEATHER_API_TIMESTAMP_NULL_MESSAGE = "Weather API returned null value for timeStamp";
    public static final String WEATHER_API_DTO_EXISTS_MESSAGE = "Received WeatherApiDto already exists in DataBase";


    //Weather API request params
    public static final String WEATHER_API_PARAM_LAT = "lat";
    public static final String WEATHER_API_PARAM_LON = "lon";
    public static final String WEATHER_API_PARAM_ALT = "altitude";


    //Weather API request headers
    public static final String WEATHER_API_HEADER_USER_AGENT_VALUE = "UniqueUserAgent";


    //Weather API Exception Messages
    public static final String API_CALL_EXCEPTION_MESSAGE = "Exception caught while contacting external api: ";
    public static final String WEATHER_API_NO_RESPONSE_BODY_MESSAGE = "Weather API returned no body. Status code: ";
    public static final String WEATHER_API_NOT_VALID_REQUEST_MESSAGE = "Invalid request. Details: ";
    public static final String WEATHER_ENTITY_NOT_FOUND_MESSAGE = "WeatherEntity was not found for parameters: " +
            "lat=%f, " +
            "lon=%f, altitude=%o";


    //Weather API Service logs
    public static final String WEATHER_API_CALL_LOG = "Calling weather API: ";
    public static final String WEATHER_API_RESPONSE_LOG = "Weather API response: ";

    //Weather Service Facade logs
    public static final String WEATHER_FIND_REQUEST = "Request with parameters: lat=%f, lon=%f, altitude=%o";


    //Weather API Deserializer logs
    public static final String DESERIALIZER_FAIL_MESSAGE = "Could not deserialize request: %s";
    public static final String DESERIALIZER_NULL_MESSAGE = "Deserialization returned null";
    public static final String DESERIALIZER_CODEC_READ_FAIL_MESSAGE = "Exception while reading three by codec";
    public static final String DESERIALIZER_DESERIALIZED_MESSAGE = "%s deserialized to: %s";


    //Weather Controller input data adjustment constants
    public static final int WEATHER_CONTROLLER_LAT_AND_LON_MAX_DECIMAL_NUMBER = 4;
    public static final int WEATHER_CONTROLLER_ALTITUDE_MAX_VALUE = 9000;
    public static final int WEATHER_CONTROLLER_ALTITUDE_MIN_VALUE = -500;


    //Weather Service logs
    public static final String WEATHER_ENTITY_SAVED_LOG = "WeatherEntity saved with Id: %s";
    public static final String WEATHER_ENTITY_DELETED_LOG = "WeatherEntity deleted from DB: ";
    public static final String WEATHER_ENTITY_FOUND_LOG = "WeatherEntity found in DB with Id: %s";


    //Weather Service Util logs
    public static final String FLOAT_VALUE_WAS_ADJUSTED_LOG = "Request Float value was adjusted from {} to {}";
    public static final String INTEGER_VALUE_WAS_ADJUSTED_LOG = "Request Integer value was adjusted from {} to {}";


    //WeatherApiDto deserialization node constants
    public static final String GEOMETRY = "geometry";
    public static final String COORDINATES = "coordinates";
    public static final String PROPERTIES = "properties";
    public static final String TIMESERIES = "timeseries";
    public static final String TIME = "time";
    public static final String DATA = "data";
    public static final String INSTANT = "instant";
    public static final String DETAILS = "details";
    public static final String AIR_TEMPERATURE = "air_temperature";

    private ConstantsStore() {
    }
}
