package com.ssafy.witch.appointment.command;

import com.ssafy.witch.apoointment.AppointmentArrivalNotification;
import com.ssafy.witch.apoointment.event.AppointmentArrivalEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.user.UserNotification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotifyAppointmentArrivalCommand {

  private String appointmentId;
  private String name;
  private String arrivalUserId;
  private List<UserNotification> members;

  public NotifyAppointmentArrivalCommand(AppointmentArrivalEvent event) {
    AppointmentDetailProjection appointment = event.getAppointment();
    this.appointmentId = appointment.getAppointmentId();
    this.name = appointment.getName();

    this.arrivalUserId = event.getArrivalUserId();

    this.members = appointment.getMembers()
        .stream()
        .map(UserNotification::of)
        .toList();
  }

  public AppointmentArrivalNotification toNotification() {
    return new AppointmentArrivalNotification(
        this.getAppointmentId(), this.getName(), this.getArrivalUserId(),
        this.getMembers());
  }

}
