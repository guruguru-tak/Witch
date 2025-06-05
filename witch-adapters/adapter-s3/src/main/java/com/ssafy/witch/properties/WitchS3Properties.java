package com.ssafy.witch.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WitchS3Properties {

  @Value("${witch.s3.bucket-name}")
  private String bucketName;


  @Value("${witch.s3.presigned-url-expiration-seconds}")
  private Long presignedUrlExpirationSeconds;

}
