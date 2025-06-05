package com.ssafy.witch.init;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.ssafy.witch.properties.S3Properties;
import com.ssafy.witch.properties.WitchS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Initializer implements CommandLineRunner {

  private final AmazonS3 amazonS3;
  private final S3Properties s3Properties;
  private final WitchS3Properties witchS3Properties;

  @Override
  public void run(String... args) {
    String bucketName = witchS3Properties.getBucketName();
    String region = s3Properties.getRegion();

    if (!amazonS3.doesBucketExistV2(bucketName)) {
      amazonS3.createBucket(new CreateBucketRequest(bucketName, region));
    }
  }
}