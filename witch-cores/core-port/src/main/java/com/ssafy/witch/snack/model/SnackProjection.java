package com.ssafy.witch.snack.model;

import com.ssafy.witch.user.model.UserBasicProjection;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackProjection {

  private String snackId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;
  private LocalDateTime createdAt;

  private UserBasicProjection user;

}
