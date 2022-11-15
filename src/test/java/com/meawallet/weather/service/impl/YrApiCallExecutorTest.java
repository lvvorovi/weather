package com.meawallet.weather.service.impl;

import com.meawallet.weather.handler.exception.YrApiCallExecutorException;
import com.meawallet.weather.payload.YrApiServiceRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.meawallet.weather.message.store.WeatherApiCallExecutorMessageStore.buildApiCallExceptionMessage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YrApiCallExecutorTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    YrApiCallExecutor victim;

    @Test
    void execute_whenRequestDto_andStringClass_thenReturnResponse() {
        ResponseEntity<String> expected = ResponseEntity.ok("TestStringBody");
        YrApiServiceRequestDto requestDtoMock = mock(YrApiServiceRequestDto.class);
        when(restTemplate.exchange(
                requestDtoMock.getUrl(),
                requestDtoMock.getHttpMethod(),
                requestDtoMock.getHttpEntity(),
                String.class
        )).thenReturn(expected);

        ResponseEntity<String> result = victim.execute(requestDtoMock, String.class);

        assertEquals(expected, result);
        verify(restTemplate, times(1)).exchange(
                requestDtoMock.getUrl(),
                requestDtoMock.getHttpMethod(),
                requestDtoMock.getHttpEntity(),
                String.class);

        verify(requestDtoMock, times(3)).getUrl();
        verify(requestDtoMock, times(3)).getHttpEntity();
        verify(requestDtoMock, times(3)).getHttpMethod();
        verifyNoMoreInteractions(restTemplate, requestDtoMock);
    }

    @Test
    void execute_RestTemplateThrowsRestClientException_thenThrowYrApiCallExecutorException() {
        YrApiServiceRequestDto requestDtoMock = mock(YrApiServiceRequestDto.class);
        when(restTemplate.exchange(
                requestDtoMock.getUrl(),
                requestDtoMock.getHttpMethod(),
                requestDtoMock.getHttpEntity(),
                String.class
        )).thenThrow(new RestClientException("TestRestClientExceptionMessage"));

        assertThatThrownBy(() -> victim.execute(requestDtoMock, String.class))
                .isInstanceOf(YrApiCallExecutorException.class)
                .hasMessage(buildApiCallExceptionMessage("TestRestClientExceptionMessage"));

        verify(restTemplate, times(1)).exchange(
                requestDtoMock.getUrl(),
                requestDtoMock.getHttpMethod(),
                requestDtoMock.getHttpEntity(),
                String.class);

        verify(requestDtoMock, times(3)).getUrl();
        verify(requestDtoMock, times(3)).getHttpEntity();
        verify(requestDtoMock, times(3)).getHttpMethod();
        verifyNoMoreInteractions(restTemplate, requestDtoMock);
    }


}