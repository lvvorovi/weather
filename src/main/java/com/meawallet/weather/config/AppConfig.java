package com.meawallet.weather.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meawallet.weather.properties.ConnectionProperties;
import com.meawallet.weather.properties.TaskSchedulerProperties;
import lombok.RequiredArgsConstructor;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import static org.apache.http.protocol.HTTP.CONN_KEEP_ALIVE;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class AppConfig {

    private final ConnectionProperties connectionProperties;
    private final TaskSchedulerProperties taskSchedulerProperties;

    @Bean
    public PoolingHttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(connectionProperties.getMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(connectionProperties.getMaxConnectionsPerRouteDefault());
        return connectionManager;
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig
                .custom()
                .setConnectionRequestTimeout(connectionProperties.getConnectionRequestTimeout())
                .setSocketTimeout(connectionProperties.getSocketTimeout())
                .setConnectTimeout(connectionProperties.getConnectTimeout())
                .build();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager())
                .setDefaultRequestConfig(requestConfig())
                .setKeepAliveStrategy(keepAliveStrategy())
                .build();
    }

    @Bean
    ClientHttpRequestFactory requestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(requestFactory());
    }

    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(taskSchedulerProperties.getPoolSize());
        taskScheduler.setThreadNamePrefix(taskSchedulerProperties.getThreadNamePrefix());
        return taskScheduler;
    }

    @Bean
    public ConnectionKeepAliveStrategy keepAliveStrategy() {
        return (response, context) -> {
            HeaderElementIterator elementIterator = new BasicHeaderElementIterator
                    (response.headerIterator(CONN_KEEP_ALIVE));

            while (elementIterator.hasNext()) {
                HeaderElement headerElement = elementIterator.nextElement();
                boolean paramIsTimeout = headerElement.getName().equalsIgnoreCase("timeout");
                String value = headerElement.getValue();

                if (paramIsTimeout && value != null) {
                    return Long.parseLong(value) * 1000;
                }
            }

            return connectionProperties.getConnectionKeepAlive();
        };
    }
}
