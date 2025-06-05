package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.output.AppointmentDetailOutput;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.appointment.output.AppointmentWithGroupListOutput;

public interface ReadAppointmentUseCase {

  AppointmentListOutput getAppointments(String userId, String groupId);

  AppointmentWithGroupListOutput getMyAppointments(String userId, int year, int month);

  AppointmentDetailOutput getAppointmentDetail(String userId, String appointmentId);
}
