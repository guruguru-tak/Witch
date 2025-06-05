package com.ssafy.witch.apoointment.event;

import com.ssafy.witch.appointment.Appointment;
import com.ssafy.witch.group.GroupWithMemberUsers;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AppointmentCreatedEvent {

  private String createUserId;
  private Appointment appointment;
  private GroupWithMemberUsers group;
}
