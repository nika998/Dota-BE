package com.artigo.dota.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
@Getter
@Setter
public class S3BucketProperties {

    private String bucket;

    private String imageUrlPrefix;

    private String rootFolder;

}
