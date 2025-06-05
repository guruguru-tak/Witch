package com.ssafy.witch.controller.appointment.response;

import com.ssafy.witch.appointment.AppointmentStatus;
import com.ssafy.witch.controller.group.response.GroupResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentWithGroupResponse {

  private String appointmentId;
  private String name;
  private AppointmentStatus status;
  private LocalDateTime appointmentTime;

  private GroupResponse group;
}
