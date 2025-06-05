package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.command.AppointmentExitCommand;

public interface ExitAppointmentUseCase {

  void exitAppointment(AppointmentExitCommand command);
}
