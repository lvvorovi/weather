package com.meawallet.weather.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TestJsonProcessingException extends JsonProcessingException {

    public TestJsonProcessingException(String msg) {
        super(msg);
    }


}
