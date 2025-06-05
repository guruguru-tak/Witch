package com.ssafy.witch.apoointment;

import com.ssafy.witch.apoointment.event.AppointmentArrivalEvent;
import com.ssafy.witch.apoointment.event.AppointmentCreatedEvent;
import com.ssafy.witch.apoointment.event.AppointmentEndEvent;
import com.ssafy.witch.apoointment.event.AppointmentExitEvent;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import com.ssafy.witch.apoointment.event.AppointmentStartEvent;

public interface AppointmentEventPublishPort {

  void publish(AppointmentJoinEvent event);

  void publish(AppointmentStartEvent event);

  void publish(AppointmentEndEvent event);

  void publish(AppointmentArrivalEvent event);

  void publish(AppointmentCreatedEvent event);

  void publish(AppointmentExitEvent event);
}
