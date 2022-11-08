package com.meawallet.weather.business.util;

import com.meawallet.weather.handler.exception.WeatherApiServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static com.meawallet.weather.business.message.store.WeatherApiServiceMessageStore.WEATHER_API_PARAM_ALT;
import static com.meawallet.weather.business.message.store.WeatherApiServiceMessageStore.WEATHER_API_PARAM_LAT;
import static com.meawallet.weather.business.message.store.WeatherApiServiceMessageStore.WEATHER_API_PARAM_LON;
import static com.meawallet.weather.business.message.store.WeatherApiServiceMessageStore.buildApiInvalidRequestMessage;
import static com.meawallet.weather.business.message.store.WeatherApiServiceMessageStore.buildApiNoResponseMessage;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class YrWeatherApiServiceUtil {

    public Map<String, String> buildUrlParams(Float lat, Float lon, Integer alt) {
        Map<String, String> params = new HashMap<>();
        params.put(WEATHER_API_PARAM_LAT, lat.toString());
        params.put(WEATHER_API_PARAM_LON, lon.toString());

        if (alt != null) {
            params.put(WEATHER_API_PARAM_ALT, alt.toString());
        }

        return params;
    }

    public String buildRequestUrl(String url, Map<String, String> params) {
        String altNullUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(WEATHER_API_PARAM_LAT, params.get(WEATHER_API_PARAM_LAT))
                .queryParam(WEATHER_API_PARAM_LON, params.get(WEATHER_API_PARAM_LON))
                .encode()
                .toUriString();

        if (params.containsKey(WEATHER_API_PARAM_ALT)) {
            return UriComponentsBuilder.fromHttpUrl(altNullUrl)
                    .queryParam(WEATHER_API_PARAM_ALT, params.get(WEATHER_API_PARAM_ALT))
                    .encode()
                    .toUriString();
        } else {
            return altNullUrl;
        }
    }

    public HttpHeaders getRequiredHeaders(String userAgentHeaderValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_AGENT, userAgentHeaderValue);
        headers.add(ACCEPT, APPLICATION_JSON_VALUE);
        return headers;
    }

    public void validateBody(ResponseEntity<String> responseEntity) {
        boolean bodyIsNull = responseEntity.getBody() == null;
        boolean bodyIsEmpty = true;

        if (!bodyIsNull) {
            bodyIsEmpty = responseEntity.getBody().isEmpty();
        }

        if (bodyIsNull || bodyIsEmpty) {
            throw new WeatherApiServiceException(buildApiNoResponseMessage(responseEntity.getStatusCode()));
        }

    }

    public void validateResponseStatus(ResponseEntity<String> responseEntity) {
        boolean isNot200 = !responseEntity.getStatusCode().is2xxSuccessful();

        if (isNot200) {
            throw new WeatherApiServiceException(buildApiInvalidRequestMessage(responseEntity.getBody()));
        }
    }
}
