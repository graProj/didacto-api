package com.didacto.config.pay;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    String apikey = "6213286820175160";
    String secretKey = "jFHaAVeJAK12x2K7yhdKEbxm5KKGK03Is5PdWNMuaxuwZSH9MG4abidehgwdAd3c2WzgpbrR51KYpSb7";

    @Bean
    public IamportClient iamportClient(){
        return new IamportClient(apikey, secretKey);
    }
}
