package com.ssafy.witch.file;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.util.StringUtils;
import com.ssafy.witch.properties.S3Properties;
import com.ssafy.witch.properties.WitchS3Properties;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class S3Adapter implements PresignedUrlPort {

  private final AmazonS3 s3Client;
  private final WitchS3Properties witchS3Properties;
  private final S3Properties s3Properties;

  @Override
  public PresignedUrl generatePresignedUrl(
      String fileName, String directory) {
    String objectKey = createObjectKey(directory, fileName);
    GeneratePresignedUrlRequest request =
        new GeneratePresignedUrlRequest(witchS3Properties.getBucketName(), objectKey)
            .withMethod(HttpMethod.PUT)
            .withExpiration(getExpiration());

    String presignedUrl = s3Client.generatePresignedUrl(request).toString();

    return new PresignedUrl(presignedUrl, objectKey);
  }

  @Override
  public String getAccessUrl(String objectKey) {
    if (StringUtils.isNullOrEmpty(objectKey)) {
      return null;
    }
    return s3Properties.getS3Endpoint() + "/" + witchS3Properties.getBucketName() + "/" + objectKey;
  }

  private Date getExpiration() {
    return Date.from(
        Instant.now().plusSeconds(witchS3Properties.getPresignedUrlExpirationSeconds()));
  }

  private String createObjectKey(String directory, String fileName) {
    return directory + UUID.randomUUID() + "." + extractExt(fileName);
  }

  private String extractExt(String fileName) {
    return fileName.substring(fileName.lastIndexOf(".") + 1);
  }

}
