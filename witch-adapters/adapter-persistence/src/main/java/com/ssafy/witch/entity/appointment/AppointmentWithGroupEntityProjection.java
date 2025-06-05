package com.ssafy.witch.entity.appointment;

import com.ssafy.witch.group.model.GroupProjection;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentWithGroupEntityProjection {

  private final String appointmentId;
  private final String name;
  private final LocalDateTime appointmentTime;
  private final AppointmentEntityStatus status;
  private final GroupProjection group;
}
