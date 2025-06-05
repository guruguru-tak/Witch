package com.ssafy.witch.apoointment;

import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import java.time.Duration;

public interface OnGoingAppointmentCachePort {

  boolean has(String appointmentId);

  void save(AppointmentDetailProjection appointmentDetail, Duration duration);

  AppointmentDetailProjection get(String appointmentId);

  void remove(String appointmentId);
}
