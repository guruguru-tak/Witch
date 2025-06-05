package com.ssafy.witch.file.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PresignedUrlResponse {

  private String presignedUrl;
  private String objectKey;

}
