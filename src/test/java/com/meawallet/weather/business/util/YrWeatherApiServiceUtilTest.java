package com.meawallet.weather.business.util;

import com.meawallet.weather.business.handler.exception.WeatherApiServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_API_NOT_VALID_REQUEST_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_API_NO_RESPONSE_BODY_MESSAGE;
import static com.meawallet.weather.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.util.WeatherTestUtil.LON;
import static com.meawallet.weather.util.WeatherTestUtil.WEATHER_API_URL;
import static com.meawallet.weather.util.WeatherTestUtil.WEATHER_API_URL_WITH_ALL_PARAMS;
import static com.meawallet.weather.util.WeatherTestUtil.WEATHER_API_URL_WITH_NO_ALT_PARAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.USER_AGENT;

@ExtendWith(MockitoExtension.class)
class YrWeatherApiServiceUtilTest {

    @InjectMocks
    YrWeatherApiServiceUtil victim;

    @Test
    void buildUrlParams_whenAltNull_thenReturnMapWithLatAndLon() {
        Map<String, String> result = victim.buildUrlParams(LAT, LON, null);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("lat"));
        assertEquals(LAT.toString(), result.get("lat"));
        assertTrue(result.containsKey("lon"));
        assertEquals(LON.toString(), result.get("lon"));
    }

    @Test
    void buildUrlParams_whenAltNotNull_thenReturnMapWithLatAndLonAndAlt() {
        Map<String, String> result = victim.buildUrlParams(LAT, LON, ALTITUDE);

        assertEquals(3, result.size());
        assertTrue(result.containsKey("lat"));
        assertEquals(LAT.toString(), result.get("lat"));
        assertTrue(result.containsKey("lon"));
        assertEquals(LON.toString(), result.get("lon"));
        assertTrue(result.containsKey("altitude"));
        assertEquals(ALTITUDE.toString(), result.get("altitude"));
    }

    @Test
    void buildRequestUrl_whenNoAltParam_thenBuildUrlWithNoAltParam() {
        Map<String, String> params = new HashMap<>();
        params.put("lat", LAT.toString());
        params.put("lon", LON.toString());

        String result = victim.buildRequestUrl(WEATHER_API_URL, params);

        assertThat(result).isEqualTo(WEATHER_API_URL_WITH_NO_ALT_PARAM);
    }


    @Test
    void buildRequestUrl_whenIsAltParam_thenBuildUrlWithAltParam() {
        Map<String, String> params = new HashMap<>();
        params.put("lat", LAT.toString());
        params.put("lon", LON.toString());
        params.put("altitude", ALTITUDE.toString());

        String result = victim.buildRequestUrl(WEATHER_API_URL, params);

        assertThat(result).isEqualTo(WEATHER_API_URL_WITH_ALL_PARAMS);
    }

    @Test
    void getRequiredHeaders_whenRequest_thenReceiveRequiredHeaders() {
        HttpHeaders result = victim.getRequiredHeaders();

        assertEquals(2, result.size());
        assertTrue(result.containsKey(USER_AGENT));
        assertTrue(result.containsKey(ACCEPT));
    }

    @Test
    void validateBody_whenValidBody_thenDoNothing() {
        ResponseEntity<String> entity = ResponseEntity.ok().body("NotEmpty");

        assertThatNoException().isThrownBy(() -> victim.validateBody(entity));
    }


    @Test
    void validateBody_whenEmptyBody_thenThrowWeatherApiServiceException() {
        ResponseEntity<String> entity = ResponseEntity.badRequest().body("");

        assertThatThrownBy(() -> victim.validateBody(entity))
                .isInstanceOf(WeatherApiServiceException.class)
                .hasMessage(WEATHER_API_NO_RESPONSE_BODY_MESSAGE + entity.getStatusCode());
    }

    @Test
    void validateBody_whenBodyIsNull_thenThrowWeatherApiServiceException() {
        ResponseEntity<String> entity = ResponseEntity.ok().build();

        assertThatThrownBy(() -> victim.validateBody(entity))
                .isInstanceOf(WeatherApiServiceException.class)
                .hasMessage(WEATHER_API_NO_RESPONSE_BODY_MESSAGE + entity.getStatusCode());
    }

    @Test
    void validateResponseStatus_when200_thenDoNothing() {
        ResponseEntity<String> entity = ResponseEntity.ok().build();

        assertThatNoException().isThrownBy(() -> victim.validateResponseStatus(entity));
    }

    @Test
    void validateResponseStatus_whenNot200_thenThrowWeatherApiServiceException() {
        ResponseEntity<String> entity = ResponseEntity.badRequest().body("Error message");

        assertThatThrownBy(() -> victim.validateResponseStatus(entity))
                .isInstanceOf(WeatherApiServiceException.class)
                .hasMessage(WEATHER_API_NOT_VALID_REQUEST_MESSAGE + entity.getBody());
    }


}