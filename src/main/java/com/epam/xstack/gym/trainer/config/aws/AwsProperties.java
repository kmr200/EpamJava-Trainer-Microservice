package com.epam.xstack.gym.trainer.config.aws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Data
public class AwsProperties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String dynamoTableName;
}
