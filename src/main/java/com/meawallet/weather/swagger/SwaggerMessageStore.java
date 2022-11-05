package com.meawallet.weather.swagger;

public class SwaggerMessageStore {

    /*
     * Weather
     * */
    public static final String WEATHER_CONTROLLER_TAG_NAME = "Weather Data";
    public static final String WEATHER_CONTROLLER_TAG_DESCRIPTION = "Provides GET method " +
            "to receive current hour temperature for specified location";
    public static final String WEATHER_OPERATION_VALUE = "Get Weather temperature";
    public static final String WEATHER_OPERATION_NOTES = "Weather temperature for specified place";
    public static final String WEATHER_API_PARAM_VALUE_LAT = "Latitude in decimal degrees, mandatory. " +
            "Will be rounded to 4 decimals if supplied more.";
    public static final String WEATHER_API_PARAM_EXAMPLE_LAT = "56.948112";
    public static final String WEATHER_API_PARAM_VALUE_LON = "Longitude in decimal degrees, mandatory. " +
            "Will be rounded to 4 decimals if supplied more.";
    public static final String WEATHER_API_PARAM_EXAMPLE_LON = "24.108332";
    public static final String WEATHER_API_PARAM_VALUE_ALTITUDE =
            "Height above (or sometimes below) sea level in whole meters (integers). " +
                    "Optional but recommended for precise temperature values. " +
                    "When missing the internal topography model is used, " +
                    "which is rather course and may be incorrect in hilly terrain." +
                    "If provided out of range will be adjusted.";
    public static final String WEATHER_API_PARAM_EXAMPLE_ALTITUDE = "10";
    public static final String WEATHER_API_PARAM_ALLOWED_VALUES_ALTITUDE = "range[-500, 9000]";

    /*
     * ApiResponse
     * */
    public static final String STATUS_OK_MESSAGE = "HTTP STATUS OK";
    public static final String STATUS_BAD_REQUEST_MESSAGE = "HTTP STATUS BAD REQUEST";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "INTERNAL SERVER ERROR";

    /*
     * Swagger Api Info
     * */
    static final String API_INFO_TITLE = "Weather management systems API";
    static final String API_INFO_DESCRIPTION = "MeaWallet Weather Management in new feature project";
    static final String API_INFO_VERSION = "0.1.0.SNAPSHOT";


    private SwaggerMessageStore() {
    }

}
