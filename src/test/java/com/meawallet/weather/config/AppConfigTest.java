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

import static com.meawallet.weather.test.util.WeatherTestUtil.CONNECT_TIMEOUT;
import static com.meawallet.weather.test.util.WeatherTestUtil.CONNECTION_REQUEST_TIMEOUT;
import static com.meawallet.weather.test.util.WeatherTestUtil.MAX_CONNECTIONS_PER_ROUTE_DEFAULT;
import static com.meawallet.weather.test.util.WeatherTestUtil.MAX_TOTAL_CONNECTIONS;
import static com.meawallet.weather.test.util.WeatherTestUtil.SOCKET_TIMEOUT;
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
        when(connectionProperties.getMaxTotalConnections()).thenReturn(MAX_TOTAL_CONNECTIONS);
        when(connectionProperties.getMaxConnectionsPerRouteDefault()).thenReturn(MAX_CONNECTIONS_PER_ROUTE_DEFAULT);
        PoolingHttpClientConnectionManager result = victim.connectionManager();

        assertEquals(MAX_TOTAL_CONNECTIONS, result.getMaxTotal());
        assertEquals(MAX_CONNECTIONS_PER_ROUTE_DEFAULT, result.getDefaultMaxPerRoute());

        verify(connectionProperties, times(1)).getMaxTotalConnections();
        verify(connectionProperties, times(1)).getMaxConnectionsPerRouteDefault();
        verifyNoMoreInteractions(connectionProperties);
        verifyNoInteractions(taskSchedulerProperties);
    }

    @Test
    void requestConfig_whenCalled_setUpRequestConfigBean_andReturn() {
        when(connectionProperties.getConnectionRequestTimeout()).thenReturn(CONNECTION_REQUEST_TIMEOUT);
        when(connectionProperties.getSocketTimeout()).thenReturn(SOCKET_TIMEOUT);
        when(connectionProperties.getConnectTimeout()).thenReturn(CONNECT_TIMEOUT);

        RequestConfig result = victim.requestConfig();

        assertEquals(CONNECTION_REQUEST_TIMEOUT, result.getConnectionRequestTimeout());
        assertEquals(SOCKET_TIMEOUT, result.getSocketTimeout());
        assertEquals(CONNECT_TIMEOUT, result.getConnectTimeout());
        verify(connectionProperties, times(1)).getConnectionRequestTimeout();
        verify(connectionProperties, times(1)).getSocketTimeout();
        verify(connectionProperties, times(1)).getConnectTimeout();
        verifyNoMoreInteractions(connectionProperties);
        verifyNoInteractions(taskSchedulerProperties);
    }


}