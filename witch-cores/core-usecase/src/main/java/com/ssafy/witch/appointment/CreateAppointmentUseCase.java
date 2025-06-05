package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.command.AppointmentCreateCommand;

public interface CreateAppointmentUseCase {

  Appointment createAppointment(AppointmentCreateCommand command);
}
