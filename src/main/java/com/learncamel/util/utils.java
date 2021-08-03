package com.learncamel.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;

public class utils {
    public static Object readExchangeBody(Exchange exchange) {
        if (exchange != null && exchange.getIn() != null && exchange.getIn().getBody() != null) {
            return exchange.getIn().getBody();
        }
        return null;
    }
}
