package com.ssafy.witch.appointment.command;

import com.ssafy.witch.apoointment.AppointmentEndNotification;
import com.ssafy.witch.apoointment.event.AppointmentEndEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.user.UserNotification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotifyAppointmentEndCommand {

  private String appointmentId;
  private String name;
  private LocalDateTime appointmentTime;
  private List<UserNotification> members;

  public NotifyAppointmentEndCommand(AppointmentEndEvent event) {
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

  public AppointmentEndNotification toNotification() {
    return new AppointmentEndNotification(
        this.getAppointmentId(),
        this.getName(),
        this.getAppointmentTime(),
        this.members);
  }


}
