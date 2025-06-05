package com.ssafy.witch.appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface UpdateAppointmentUseCase {

  List<Appointment> startAppointments(LocalDateTime appointmentTime);

  List<Appointment> endAppointments(LocalDateTime now);
}
