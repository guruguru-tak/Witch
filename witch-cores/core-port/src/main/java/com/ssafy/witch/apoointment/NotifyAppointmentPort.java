package com.ssafy.witch.apoointment;

public interface NotifyAppointmentPort {

  void notifyJoinAppointment(AppointmentJoinNotification appointmentJoinNotification);

  void notifyStartAppointment(AppointmentStartNotification appointmentStartNotification);

  void notifyEndAppointment(AppointmentEndNotification notification);

  void notifyArrivalAppointment(AppointmentArrivalNotification notification);

  void notifyAppointmentCreated(AppointmentCreatedNotification notification);

  void notifyAppointmentExit(AppointmentExitNotification notification);
}
