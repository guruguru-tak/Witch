package com.ssafy.witch.apoointment;

import com.ssafy.witch.appointment.Appointment;
import com.ssafy.witch.group.GroupWithMemberUsers;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentCreatedNotification {

  private String createUserId;
  private Appointment appointment;
  private GroupWithMemberUsers group;

}
