package com.ssafy.witch.entity.Snack;

import com.ssafy.witch.user.model.UserBasicProjection;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackDetailEntityProjection {

  private String snackId;
  private String userId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;
  private LocalDateTime createdAt;
  private final UserBasicProjection user;
}
