package com.ssafy.witch.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ssafy.witch.properties.S3Properties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@AllArgsConstructor
@Configuration
public class S3Config {

  private final S3Properties s3Properties;


  @Bean
  @Profile("default")
  public AmazonS3 s3Client() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(s3Properties.getAccessKey(),
        s3Properties.getSecretKey());

    return AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withEndpointConfiguration(
            new AmazonS3ClientBuilder
                .EndpointConfiguration(s3Properties.getS3Endpoint(), s3Properties.getRegion()))
        .enablePathStyleAccess()
        .build();
  }
}
