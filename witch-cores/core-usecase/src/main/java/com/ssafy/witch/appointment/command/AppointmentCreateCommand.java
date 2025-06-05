package com.ssafy.witch.appointment.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.AppointmentName;
import com.ssafy.witch.validate.annotation.AppointmentTime;
import com.ssafy.witch.validate.annotation.Latitude;
import com.ssafy.witch.validate.annotation.Longitude;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AppointmentCreateCommand extends SelfValidating<AppointmentCreateCommand> {

  @NotNull
  private final String userId;

  @NotNull
  private final String groupId;

  @AppointmentName
  private final String name;

  private final String summary;

  @NotNull
  @AppointmentTime
  private final LocalDateTime appointmentTime;

  @NotNull
  @Longitude
  private final Double longitude;

  @NotNull
  @Latitude
  private final Double latitude;

  private final String address;

  public AppointmentCreateCommand(String userId, String groupId, String name, String summary,
      LocalDateTime appointmentTime, Double longitude, Double latitude, String address) {
    this.userId = userId;
    this.groupId = groupId;
    this.name = name;
    this.summary = summary;
    this.appointmentTime = appointmentTime;
    this.longitude = longitude;
    this.latitude = latitude;
    this.address = address;

    this.validateSelf();
  }
}
