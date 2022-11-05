package com.meawallet.weather.business.service.impl;

import com.meawallet.weather.business.handler.exception.WeatherEntityNotFoundException;
import com.meawallet.weather.business.mapper.WeatherMapper;
import com.meawallet.weather.business.repository.WeatherRepository;
import com.meawallet.weather.business.repository.entity.WeatherEntity;
import com.meawallet.weather.business.validation.service.WeatherValidationService;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_DELETED_LOG;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_FOUND_LOG;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_NOT_FOUND_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_SAVED_LOG;
import static com.meawallet.weather.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.util.WeatherTestUtil.LON;
import static com.meawallet.weather.util.WeatherTestUtil.weatherApiDto;
import static com.meawallet.weather.util.WeatherTestUtil.weatherEntity;
import static com.meawallet.weather.util.WeatherTestUtil.weatherResponseDto;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.assertThat;
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

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class WeatherServiceImplTest {

    @Mock
    WeatherRepository repository;
    @Mock
    WeatherMapper mapper;
    @Mock
    WeatherValidationService validationService;

    @InjectMocks
    WeatherServiceImpl victim;

    @Test
    void findByLatAndLonAndAlt_whenFoundInDb_thenReturnResponse(CapturedOutput output) {
        WeatherEntity entity = weatherEntity();
        WeatherResponseDto expected = weatherResponseDto();
        when(repository.findByLatAndLonAndAltitudeAndTimeStamp(LAT, LON, ALTITUDE, LocalDateTime.now().truncatedTo(HOURS)))
                .thenReturn(Optional.of(entity));
        when(mapper.entityToDto(entity)).thenReturn(expected);

        WeatherResponseDto result = victim.findByLatAndLonAndAlt(LAT, LON, ALTITUDE);

        assertEquals(expected, result);
        assertThat(output.getOut()).contains(String.format(WEATHER_ENTITY_FOUND_LOG, entity.getId()));
        verify(repository, times(1))
                .findByLatAndLonAndAltitudeAndTimeStamp(LAT, LON, ALTITUDE, LocalDateTime.now().truncatedTo(HOURS));
        verify(mapper, times(1)).entityToDto(entity);
        verifyNoMoreInteractions(repository, mapper);
        verifyNoInteractions(validationService);
    }

    @Test
    void findByLatAndLonAndAlt_whenNotFoundInDb_thenThrowWeatherEntityNotFoundException(CapturedOutput output) {
        LocalDateTime now =  LocalDateTime.now().truncatedTo(HOURS);
        when(repository.findByLatAndLonAndAltitudeAndTimeStamp(LAT, LON, ALTITUDE,now))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.findByLatAndLonAndAlt(LAT, LON, ALTITUDE))
                .isInstanceOf(WeatherEntityNotFoundException.class)
                .hasMessage(String.format(WEATHER_ENTITY_NOT_FOUND_MESSAGE, LAT, LON, ALTITUDE));

        assertThat(output.getOut()).isEmpty();
        verify(repository, times(1)).findByLatAndLonAndAltitudeAndTimeStamp(LAT, LON, ALTITUDE, now);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, mapper);
    }

    @Test
    void save_whenValidRequest_thenSave_andReturn(CapturedOutput output) {
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
        assertThat(output.getOut()).contains(String.format(WEATHER_ENTITY_SAVED_LOG, entity.getId()));
        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).dtoToEntity(requestDto);
        verify(mockedEntity, times(1)).setId(anyString());
        verify(repository, times(1)).save(mockedEntity);
        verify(mapper, times(1)).entityToDto(entity);
        verifyNoMoreInteractions(validationService, repository, mapper);
    }

    @Test
    void deleteOutdated_whenFound_thenDelete(CapturedOutput output) {
        List<WeatherEntity> entityList = List.of(weatherEntity(), weatherEntity());
        when(repository.deleteByTimeStampBefore(any())).thenReturn(entityList);

        assertThatNoException().isThrownBy(() -> victim.deleteOutdated());

        verify(repository, times(1)).deleteByTimeStampBefore(any());
        entityList.forEach(entity-> assertThat(output.getOut())
                .contains(WEATHER_ENTITY_DELETED_LOG + entity));
    }

    @Test
    void deleteOutdated_whenNotFound_thenDoNothing(CapturedOutput output) {
        when(repository.deleteByTimeStampBefore(any())).thenReturn(List.of());

        assertThatNoException().isThrownBy(() -> victim.deleteOutdated());

        verify(repository, times(1)).deleteByTimeStampBefore(any());
       assertThat(output.getOut()).isEmpty();
    }


}