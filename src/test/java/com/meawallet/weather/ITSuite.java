package com.meawallet.weather;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "com.meawallet.weather.controller.impl.integration"
})
public class ITSuite {
}
