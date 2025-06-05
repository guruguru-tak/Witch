package com.ssafy.witch.snack.output;

import com.ssafy.witch.user.output.UserBasicOutput;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackDetailOutput {

  private String snackId;
  private String appointmentId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;
  private LocalDateTime createdAt;

  private UserBasicOutput user;
}
