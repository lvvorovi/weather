package com.meawallet.weather.service.impl;

import com.meawallet.weather.handler.exception.WeatherEntityAlreadyExistsException;
import com.meawallet.weather.handler.exception.WeatherEntityNotFoundException;
import com.meawallet.weather.mapper.WeatherMapper;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.properties.WeatherProperties;
import com.meawallet.weather.repository.WeatherRepository;
import com.meawallet.weather.repository.entity.WeatherEntity;
import com.meawallet.weather.validation.service.WeatherValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static com.meawallet.weather.message.store.WeatherServiceMessageStore.buildAlreadyExistsMessage;
import static com.meawallet.weather.message.store.WeatherServiceMessageStore.buildDtoNotFoundMessage;
import static com.meawallet.weather.test.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.test.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.test.util.WeatherTestUtil.LON;
import static com.meawallet.weather.test.util.WeatherTestUtil.NOW_TRUNCATED_HOURS;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherApiDto;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherEntity;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherResponseDto;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class WeatherServiceImplTest {

    @Mock
    WeatherRepository repository;
    @Mock
    WeatherMapper mapper;
    @Mock
    WeatherValidationService validationService;
    @Mock
    WeatherProperties properties;

    @InjectMocks
    WeatherServiceImpl victim;

    @Test
    void findByLatAndLonAndAlt_whenFoundInDb_thenReturnResponse() {
        WeatherResponseDto expected = weatherResponseDto();
        when(repository.findDtoByLatAndLonAndAltitudeAndTimeStamp(LAT, LON, ALTITUDE, NOW_TRUNCATED_HOURS))
                .thenReturn(Optional.of(expected));

        WeatherResponseDto result = victim.findDtoByLatAndLonAndAlt(LAT, LON, ALTITUDE);

        assertEquals(expected, result);
        verify(repository, times(1))
                .findDtoByLatAndLonAndAltitudeAndTimeStamp(LAT, LON, ALTITUDE, NOW_TRUNCATED_HOURS);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, properties, mapper);
    }

    @Test
    void findByLatAndLonAndAlt_whenNotFoundInDb_thenThrowWeatherEntityNotFoundException() {
        when(repository.findDtoByLatAndLonAndAltitudeAndTimeStamp(LAT, LON, ALTITUDE, NOW_TRUNCATED_HOURS))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.findDtoByLatAndLonAndAlt(LAT, LON, ALTITUDE))
                .isInstanceOf(WeatherEntityNotFoundException.class)
                .hasMessage(buildDtoNotFoundMessage(LAT, LON, ALTITUDE, NOW_TRUNCATED_HOURS));

        verify(repository, times(1)).findDtoByLatAndLonAndAltitudeAndTimeStamp(LAT, LON, ALTITUDE, NOW_TRUNCATED_HOURS);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, mapper, properties);
    }

    @Test
    void save_whenValidRequest_thenSave_andReturn() {
        WeatherApiDto requestDto = weatherApiDto();
        WeatherResponseDto expected = weatherResponseDto();
        WeatherEntity mockedEntity = mock(WeatherEntity.class);
        WeatherEntity entity = weatherEntity();
        doNothing().when(validationService).validate(requestDto);
        when(mapper.dtoToEntity(requestDto)).thenReturn(mockedEntity);
        doNothing().when(mockedEntity).setId(anyString());
        when(repository.save(mockedEntity)).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(expected);

        WeatherResponseDto result = victim.save(requestDto);

        assertEquals(expected, result);
        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).dtoToEntity(requestDto);
        verify(mockedEntity, times(1)).setId(anyString());
        verify(repository, times(1)).save(mockedEntity);
        verify(mapper, times(1)).entityToDto(entity);
        verifyNoMoreInteractions(validationService, repository, mapper);
        verifyNoInteractions(properties);
    }

    @Test
    void save_whenValidRequest_andFoundInDb_thenThrowWeatherEntityAlreadyExistsException() {
        WeatherApiDto requestDto = weatherApiDto();
        WeatherEntity mockedEntity = mock(WeatherEntity.class);
        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("TestDataIntegrityViolationException");
        doNothing().when(validationService).validate(requestDto);
        when(mapper.dtoToEntity(requestDto)).thenReturn(mockedEntity);
        doNothing().when(mockedEntity).setId(anyString());
        when(repository.save(mockedEntity)).thenThrow(exception);

        assertThatThrownBy(() -> victim.save(requestDto))
                .isInstanceOf(WeatherEntityAlreadyExistsException.class)
                .hasMessage(buildAlreadyExistsMessage(mockedEntity));

        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).dtoToEntity(requestDto);
        verify(mockedEntity, times(1)).setId(anyString());
        verify(repository, times(1)).save(mockedEntity);
        verifyNoMoreInteractions(validationService, repository, mapper);
        verifyNoInteractions(properties);
    }

    @Test
    void deleteOutdated_whenFound_thenDelete() {
        List<WeatherEntity> entityList = List.of(weatherEntity(), weatherEntity());
        when(properties.getEntityTtlHours()).thenReturn(2);
        when(repository.deleteByTimeStampBefore(any())).thenReturn(entityList);

        assertThatNoException().isThrownBy(() -> victim.deleteOutdated());

        verify(repository, times(1)).deleteByTimeStampBefore(any());
        verifyNoMoreInteractions(properties, repository);
        verifyNoInteractions(validationService, mapper);
    }

    @Test
    void deleteOutdated_whenNotFound_thenDoNothing() {
        when(properties.getEntityTtlHours()).thenReturn(2);
        when(repository.deleteByTimeStampBefore(any())).thenReturn(List.of());

        assertThatNoException().isThrownBy(() -> victim.deleteOutdated());

        verify(repository, times(1)).deleteByTimeStampBefore(any());
        verifyNoMoreInteractions(repository, properties);
        verifyNoInteractions(validationService, mapper);
    }

}