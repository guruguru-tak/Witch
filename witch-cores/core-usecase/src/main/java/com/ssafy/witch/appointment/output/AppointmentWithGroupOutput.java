package com.ssafy.witch.appointment.output;

import com.ssafy.witch.appointment.AppointmentStatus;
import com.ssafy.witch.group.output.GroupOutput;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentWithGroupOutput {

  private final String appointmentId;
  private final String name;
  private final LocalDateTime appointmentTime;
  private final AppointmentStatus status;
  private final GroupOutput group;

}
