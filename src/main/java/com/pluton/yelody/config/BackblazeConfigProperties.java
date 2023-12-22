package com.pluton.yelody.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties("backblaze.b2")
@Data
public class BackblazeConfigProperties {
    private String applicationKeyId;
    private String applicationKey;
    private String userBucketId;
    private String karaokeBucketId;
    private String userBucketName;
    private String karaokeBucketName;


}
