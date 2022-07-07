package com.micro.sale.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import util.NumberGenerator;
import util.TemplateResponse;

/**
 * @author jrodriguez
 */
@Configuration
public class Config {
    @Bean
    NumberGenerator generator(){
        return new NumberGenerator();
    }

    @Bean
    TemplateResponse templateResponse(){
        return new TemplateResponse();
    }
}
