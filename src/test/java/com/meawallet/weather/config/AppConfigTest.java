package com.meawallet.weather.config;

import com.meawallet.weather.properties.ConnectionProperties;
import com.meawallet.weather.properties.TaskSchedulerProperties;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.meawallet.weather.util.WeatherTestUtil.connectTimeout;
import static com.meawallet.weather.util.WeatherTestUtil.connectionRequestTimeout;
import static com.meawallet.weather.util.WeatherTestUtil.maxConnectionsPerRouteDefault;
import static com.meawallet.weather.util.WeatherTestUtil.maxTotalConnections;
import static com.meawallet.weather.util.WeatherTestUtil.socketTimeout;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppConfigTest {

    @Mock
    ConnectionProperties connectionProperties;
    @Mock
    TaskSchedulerProperties taskSchedulerProperties;

    @InjectMocks
    AppConfig victim;

    @Test
    void connectionManager_whenCalled_setUpConnectionManagerBean_andReturn() {
        when(connectionProperties.getMaxTotalConnections()).thenReturn(maxTotalConnections);
        when(connectionProperties.getMaxConnectionsPerRouteDefault()).thenReturn(maxConnectionsPerRouteDefault);
        PoolingHttpClientConnectionManager result = victim.connectionManager();

        assertEquals(maxTotalConnections, result.getMaxTotal());
        assertEquals(maxConnectionsPerRouteDefault, result.getDefaultMaxPerRoute());

        verify(connectionProperties, times(1)).getMaxTotalConnections();
        verify(connectionProperties, times(1)).getMaxConnectionsPerRouteDefault();
        verifyNoMoreInteractions(connectionProperties);
        verifyNoInteractions(taskSchedulerProperties);
    }

    @Test
    void requestConfig_whenCalled_setUpRequestConfigBean_andReturn() {
        when(connectionProperties.getConnectionRequestTimeout()).thenReturn(connectionRequestTimeout);
        when(connectionProperties.getSocketTimeout()).thenReturn(socketTimeout);
        when(connectionProperties.getConnectTimeout()).thenReturn(connectTimeout);

        RequestConfig result = victim.requestConfig();

        assertEquals(connectionRequestTimeout, result.getConnectionRequestTimeout());
        assertEquals(socketTimeout, result.getSocketTimeout());
        assertEquals(connectTimeout, result.getConnectTimeout());
        verify(connectionProperties, times(1)).getConnectionRequestTimeout();
        verify(connectionProperties, times(1)).getSocketTimeout();
        verify(connectionProperties, times(1)).getConnectTimeout();
        verifyNoMoreInteractions(connectionProperties);
        verifyNoInteractions(taskSchedulerProperties);
    }


}