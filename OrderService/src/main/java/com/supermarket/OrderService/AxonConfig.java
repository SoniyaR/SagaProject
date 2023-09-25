package com.supermarket.OrderService;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;

public class AxonConfig {
    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[] {
                "com.supermarket.**"
        });
        return xStream;
    }
}
