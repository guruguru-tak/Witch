package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.entity.appointment.AppointmentDetailEntityProjection;
import com.ssafy.witch.entity.appointment.AppointmentEntityProjection;
import com.ssafy.witch.entity.appointment.AppointmentWithGroupEntityProjection;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentCustomRepository {

  boolean existsConflictAppointment(String userId, LocalDateTime appointmentTime);

  List<AppointmentEntityProjection> getAppointments(String userId, String groupId);

  List<AppointmentWithGroupEntityProjection> getMyAppointments(String userId, int year, int month);

  AppointmentDetailEntityProjection getAppointmentDetail(String appointmentId);

}
