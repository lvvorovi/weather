package com.meawallet.weather;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "com.meawallet.weather.config",
        "com.meawallet.weather.deserializer.impl",
        "com.meawallet.weather.mapper.impl",
        "com.meawallet.weather.service.impl",
        "com.meawallet.weather.util",
        "com.meawallet.weather.controller.impl.unit",
        "com.meawallet.weather.validation"
})
public class UTSuite {
}
