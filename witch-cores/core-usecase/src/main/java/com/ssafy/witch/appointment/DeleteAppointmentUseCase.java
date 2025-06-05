package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.command.AppointmentDeleteCommand;

public interface DeleteAppointmentUseCase {

  void deleteAppointment(AppointmentDeleteCommand command);
}
