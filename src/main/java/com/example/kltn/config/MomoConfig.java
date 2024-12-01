package com.example.kltn.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "momo")
@Getter
@Setter
public class MomoConfig {
    private String endpoint;
    private String partnerCode;
    private String accessKey;
    private String secretKey;
    private String returnUrl;
    private String notifyUrl;
    private String partnerName = "EventEase";
    private String storeId = "EventEaseStore";
    private String lang = "vi";
} 