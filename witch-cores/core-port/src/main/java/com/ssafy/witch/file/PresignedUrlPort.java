package com.ssafy.witch.file;

public interface PresignedUrlPort {

  PresignedUrl generatePresignedUrl(String fileName, String directory);

  String getAccessUrl(String objectKey);
}
