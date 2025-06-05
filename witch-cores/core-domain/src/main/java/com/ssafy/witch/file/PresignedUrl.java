package com.ssafy.witch.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PresignedUrl {

  private final String presignedUrl;
  private final String objectKey;

}
