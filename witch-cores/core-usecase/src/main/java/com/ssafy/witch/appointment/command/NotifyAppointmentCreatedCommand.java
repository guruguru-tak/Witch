package com.ssafy.witch.appointment.command;

import com.ssafy.witch.apoointment.AppointmentCreatedNotification;
import com.ssafy.witch.apoointment.event.AppointmentCreatedEvent;
import com.ssafy.witch.appointment.Appointment;
import com.ssafy.witch.group.GroupWithMemberUsers;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotifyAppointmentCreatedCommand {

  private String createUserId;
  private Appointment appointment;
  private GroupWithMemberUsers group;

  public NotifyAppointmentCreatedCommand(AppointmentCreatedEvent event) {
    this.createUserId = event.getCreateUserId();
    this.appointment = event.getAppointment();
    this.group = event.getGroup();
  }

  public AppointmentCreatedNotification toNotification() {
    return new AppointmentCreatedNotification(
        this.createUserId, this.appointment, this.group);
  }

}
