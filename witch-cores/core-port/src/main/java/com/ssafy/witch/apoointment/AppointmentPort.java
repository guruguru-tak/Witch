package com.ssafy.witch.apoointment;

import com.ssafy.witch.appointment.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentPort {

  Appointment save(Appointment appointment);

  boolean existsConflictAppointment(String userId, LocalDateTime appointmentTime);


  Optional<Appointment> findById(String appointmentId);

  boolean existsById(String appointmentId);

  void delete(Appointment appointment);

  List<Appointment> findAllByAppointmentTime(LocalDateTime appointmentTime);

  void saveAll(List<Appointment> appointments);
}
