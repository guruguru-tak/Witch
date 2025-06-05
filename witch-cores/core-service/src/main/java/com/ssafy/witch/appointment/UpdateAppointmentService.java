package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentEventPublishPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.apoointment.AppointmentReadPort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.event.AppointmentEndEvent;
import com.ssafy.witch.apoointment.event.AppointmentStartEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateAppointmentService implements UpdateAppointmentUseCase {

  private final AppointmentPort appointmentPort;
  private final AppointmentReadPort appointmentReadPort;
  private final AppointmentEventPublishPort appointmentEventPublishPort;
  private final OnGoingAppointmentCachePort onGoingAppointmentCachePort;

  @Transactional
  public List<Appointment> startAppointments(LocalDateTime appointmentTime) {

    List<Appointment> appointments =
        appointmentPort.findAllByAppointmentTime(appointmentTime);

    appointments.forEach(Appointment::startScheduledAppointment);

    for (Appointment appointment : appointments) {
      AppointmentDetailProjection appointmentDetail = appointmentReadPort.getAppointmentDetail(
          appointment.getAppointmentId());
      AppointmentStartEvent event = new AppointmentStartEvent(appointmentDetail);
      appointmentEventPublishPort.publish(event);
      onGoingAppointmentCachePort.save(appointmentDetail, Duration.ofHours(1));
    }
    appointmentPort.saveAll(appointments);

    return appointments;
  }

  @Override
  public List<Appointment> endAppointments(LocalDateTime now) {

    List<Appointment> appointments =
        appointmentPort.findAllByAppointmentTime(now);

    appointments.forEach(Appointment::endOngoingAppointment);

    for (Appointment appointment : appointments) {
      AppointmentDetailProjection appointmentDetail = appointmentReadPort.getAppointmentDetail(
          appointment.getAppointmentId());
      AppointmentEndEvent event = new AppointmentEndEvent(appointmentDetail);
      appointmentEventPublishPort.publish(event);
      onGoingAppointmentCachePort.remove(appointmentDetail.getAppointmentId());
    }
    appointmentPort.saveAll(appointments);

    return appointments;
  }
}
