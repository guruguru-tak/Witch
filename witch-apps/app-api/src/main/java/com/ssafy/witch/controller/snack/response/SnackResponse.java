package com.ssafy.witch.controller.snack.response;

import com.ssafy.witch.controller.user.response.UserResponse;
import com.ssafy.witch.user.model.UserBasicProjection;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackResponse {

  private String snackId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;
  private LocalDateTime createdAt;

  private UserResponse user;
}
