package com.ssafy.witch.appointment.command;

import com.ssafy.witch.apoointment.AppointmentStartNotification;
import com.ssafy.witch.apoointment.event.AppointmentStartEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.user.UserNotification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotifyAppointmentStartCommand {

  private String appointmentId;
  private String name;
  private LocalDateTime appointmentTime;
  private List<UserNotification> members;

  public NotifyAppointmentStartCommand(AppointmentStartEvent event) {
    AppointmentDetailProjection appointment = event.getAppointment();
    this.appointmentId = appointment.getAppointmentId();
    this.name = appointment.getName();
    this.appointmentTime = appointment.getAppointmentTime();

    this.members = appointment.getMembers()
        .stream()
        .map(member -> new UserNotification(member.getUserId(), member.getNickname(),
            member.getFcmToken()))
        .toList();
  }

  public AppointmentStartNotification toNotification() {
    return new AppointmentStartNotification(
        this.getAppointmentId(),
        this.getName(),
        this.getAppointmentTime(),
        this.members);
  }


}
