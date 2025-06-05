package com.ssafy.witch.controller.snack.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SnackCreateRequest {

  private Double longitude;
  private Double latitude;
  private String snackImageObjectKey;
  private String snackSoundObjectKey;

}
