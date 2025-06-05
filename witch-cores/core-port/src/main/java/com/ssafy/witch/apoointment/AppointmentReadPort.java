package com.ssafy.witch.apoointment;

import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentProjection;
import com.ssafy.witch.apoointment.model.AppointmentWithGroupProjection;
import java.util.List;

public interface AppointmentReadPort {

  List<AppointmentProjection> getAppointments(String userId, String groupId);

  List<AppointmentWithGroupProjection> getMyAppointment(String userId, int year, int month);

  AppointmentDetailProjection getAppointmentDetail(String appointmentId);
}
