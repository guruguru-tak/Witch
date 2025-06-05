package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.command.AppointmentJoinCommand;

public interface JoinAppointmentUseCase {

  void joinAppointment(AppointmentJoinCommand command);
}
