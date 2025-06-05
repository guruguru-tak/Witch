package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentArrivalNotification;
import com.ssafy.witch.apoointment.AppointmentCreatedNotification;
import com.ssafy.witch.apoointment.AppointmentEndNotification;
import com.ssafy.witch.apoointment.AppointmentExitNotification;
import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.AppointmentStartNotification;
import com.ssafy.witch.apoointment.NotifyAppointmentPort;
import com.ssafy.witch.appointment.command.NotifyAppointmentArrivalCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentCreatedCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentEndCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentExitCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentJoinCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentStartCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotifyAppointmentService implements NotifyAppointmentUseCase {

  private final NotifyAppointmentPort notifyAppointmentPort;

  @Override
  public void notifyArrival(NotifyAppointmentArrivalCommand command) {
    AppointmentArrivalNotification notification = command.toNotification();
    notifyAppointmentPort.notifyArrivalAppointment(notification);
  }

  @Override
  public void notifyCreated(NotifyAppointmentCreatedCommand command) {
    AppointmentCreatedNotification notification = command.toNotification();
    notifyAppointmentPort.notifyAppointmentCreated(notification);
  }

  @Override
  public void notifyExit(NotifyAppointmentExitCommand command) {
    AppointmentExitNotification notification = command.toNotification();
    notifyAppointmentPort.notifyAppointmentExit(notification);
  }

  @Override
  public void notifyJoin(NotifyAppointmentJoinCommand command) {

    AppointmentJoinNotification notification = command.toNotification();
    notifyAppointmentPort.notifyJoinAppointment(notification);
  }

  @Override
  public void notifyStart(NotifyAppointmentStartCommand command) {
    AppointmentStartNotification notification = command.toNotification();
    notifyAppointmentPort.notifyStartAppointment(notification);
  }

  @Override
  public void notifyEnd(NotifyAppointmentEndCommand command) {
    AppointmentEndNotification notification = command.toNotification();
    notifyAppointmentPort.notifyEndAppointment(notification);
  }
}
