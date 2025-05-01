package com.vinicius.ecommerce.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.encoder")
@Getter
@Setter
public class EncoderProperties {

    private String id = "pbkdf2";
    private String secret = "";
    private int iterations = 185000;
    private int hashWidth = 256;


}
