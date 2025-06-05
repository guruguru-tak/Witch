package com.ssafy.witch.controller.snack.response;

import com.ssafy.witch.controller.user.response.UserResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackDetailResponse {
  private String snackId;
  private String appointmentId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;
  private LocalDateTime createdAt;

  private UserResponse user;

}
