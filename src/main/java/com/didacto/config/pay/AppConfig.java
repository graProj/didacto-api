package com.didacto.config.pay;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${imp.api.key}")
    private String apikey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apikey, secretKey);
    }
}
