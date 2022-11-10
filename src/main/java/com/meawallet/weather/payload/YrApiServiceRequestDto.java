package com.meawallet.weather.payload;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Builder
@Data
public class YrApiServiceRequestDto {

    private String url;
    private HttpMethod httpMethod;
    private HttpEntity<?> httpEntity;

}
