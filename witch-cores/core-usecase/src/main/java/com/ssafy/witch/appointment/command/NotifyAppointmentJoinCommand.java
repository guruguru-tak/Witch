package com.ssafy.witch.appointment.command;

import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.user.UserNotification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotifyAppointmentJoinCommand {

  private String appointmentId;
  private String name;
  private String joinUserId;
  private List<UserNotification> members;

  public NotifyAppointmentJoinCommand(AppointmentJoinEvent event) {
    AppointmentDetailProjection appointment = event.getAppointment();
    this.appointmentId = appointment.getAppointmentId();
    this.name = appointment.getName();

    this.joinUserId = event.getJoinUserId();

    this.members = appointment.getMembers()
        .stream()
        .map(UserNotification::of)
        .toList();
  }

  public AppointmentJoinNotification toNotification() {
    return new AppointmentJoinNotification(
        this.getAppointmentId(), this.getName(), this.getJoinUserId(),
        this.getMembers());
  }

}
