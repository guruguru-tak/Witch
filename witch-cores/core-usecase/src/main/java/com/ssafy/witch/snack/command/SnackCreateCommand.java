package com.ssafy.witch.snack.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.Latitude;
import com.ssafy.witch.validate.annotation.Longitude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SnackCreateCommand extends SelfValidating<SnackCreateCommand> {

  @NotNull
  private final String userId;

  @NotNull
  private final String appointmentId;

  @NotNull
  @Longitude
  private final Double longitude;

  @NotNull
  @Latitude
  private final Double latitude;

  @NotNull
  private final String snackImageObjectKey;
  private final String snackSoundObjectKey;

  public SnackCreateCommand(String userId, String appointmentId, Double longitude, Double latitude,
      String snackImageObjectKey, String snackSoundObjectKey) {
    this.userId = userId;
    this.appointmentId = appointmentId;
    this.longitude = longitude;
    this.latitude = latitude;
    this.snackImageObjectKey = snackImageObjectKey;
    this.snackSoundObjectKey = snackSoundObjectKey;

    this.validateSelf();
  }
}
