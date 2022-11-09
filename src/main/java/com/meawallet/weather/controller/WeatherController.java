package com.meawallet.weather.controller;


import com.meawallet.weather.model.ErrorDto;
import com.meawallet.weather.model.WeatherResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.meawallet.weather.swagger.SwaggerMessageStore.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.meawallet.weather.swagger.SwaggerMessageStore.STATUS_BAD_REQUEST_MESSAGE;
import static com.meawallet.weather.swagger.SwaggerMessageStore.STATUS_FORBIDDEN_MESSAGE;
import static com.meawallet.weather.swagger.SwaggerMessageStore.STATUS_OK_MESSAGE;
import static com.meawallet.weather.swagger.SwaggerMessageStore.STATUS_UNAUTHORIZED_MESSAGE;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_ALLOWED_VALUES_ALTITUDE;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_ALLOWED_VALUES_LAT;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_ALLOWED_VALUES_LON;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_EXAMPLE_ALTITUDE;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_EXAMPLE_LAT;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_EXAMPLE_LON;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_VALUE_ALTITUDE;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_VALUE_LAT;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_API_PARAM_VALUE_LON;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_CONTROLLER_TAG_NAME;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_OPERATION_NOTES;
import static com.meawallet.weather.swagger.SwaggerMessageStore.WEATHER_OPERATION_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = WEATHER_CONTROLLER_TAG_NAME)
@Validated
public interface WeatherController {

    @ApiOperation(
            value = WEATHER_OPERATION_VALUE,
            notes = WEATHER_OPERATION_NOTES,
            response = WeatherResponseDto.class
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = STATUS_OK_MESSAGE, response = WeatherResponseDto.class),
            @ApiResponse(code = 400, message = STATUS_BAD_REQUEST_MESSAGE, response = ErrorDto.class),
            @ApiResponse(code = 401, message = STATUS_UNAUTHORIZED_MESSAGE, response = ErrorDto.class),
            @ApiResponse(code = 403, message = STATUS_FORBIDDEN_MESSAGE, response = ErrorDto.class),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR_MESSAGE, response = ErrorDto.class)
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<WeatherResponseDto> findByLatAndLonAndAlt(
            @ApiParam(
                    value = WEATHER_API_PARAM_VALUE_LAT,
                    allowableValues = WEATHER_API_PARAM_ALLOWED_VALUES_LAT,
                    required = true,
                    example = WEATHER_API_PARAM_EXAMPLE_LAT)
            @Min(-90) @Max(90)
            @RequestParam Float lat,

            @ApiParam(
                    value = WEATHER_API_PARAM_VALUE_LON,
                    allowableValues = WEATHER_API_PARAM_ALLOWED_VALUES_LON,
                    required = true,
                    example = WEATHER_API_PARAM_EXAMPLE_LON)
            @Min(-180) @Max(180)
            @RequestParam Float lon,

            @ApiParam(
                    value = WEATHER_API_PARAM_VALUE_ALTITUDE,
                    allowableValues = WEATHER_API_PARAM_ALLOWED_VALUES_ALTITUDE,
                    example = WEATHER_API_PARAM_EXAMPLE_ALTITUDE)
            @Min(-500) @Max(9000)
            @RequestParam(required = false) Integer altitude);

}
