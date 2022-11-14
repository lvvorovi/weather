package com.meawallet.weather;

import com.meawallet.weather.controller.impl.integration.WeatherControllerImplIntegrationTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses(WeatherControllerImplIntegrationTest.class)
public class IntegrationTestSuite {
}
