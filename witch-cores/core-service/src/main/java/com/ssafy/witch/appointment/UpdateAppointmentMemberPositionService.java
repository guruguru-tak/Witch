package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentEventPublishPort;
import com.ssafy.witch.apoointment.AppointmentMemberPositionCachePort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.event.AppointmentArrivalEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.appointment.command.UpdateAppointmentMemberPositionCommand;
import com.ssafy.witch.common.DistanceUtils;
import com.ssafy.witch.exception.appointment.AppointmentNotOnGoingException;
import com.ssafy.witch.exception.appointment.NotJoinedAppointmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateAppointmentMemberPositionService implements
    UpdateAppointmentMemberPositionUseCase {

  private final OnGoingAppointmentCachePort onGoingAppointmentCachePort;
  private final AppointmentMemberPositionCachePort appointmentMemberPositionCachePort;
  private final AppointmentEventPublishPort appointmentEventPublishPort;

  @Override
  public void execute(UpdateAppointmentMemberPositionCommand command) {
    String userId = command.getUserId();
    String appointmentId = command.getAppointmentId();
    double latitude = command.getLatitude();
    double longitude = command.getLongitude();

    if (!onGoingAppointmentCachePort.has(appointmentId)) {
      throw new AppointmentNotOnGoingException();
    }

    AppointmentDetailProjection appointment = onGoingAppointmentCachePort.get(
        appointmentId);

    appointment.getMembers().stream()
        .filter(member -> member.getUserId().equals(userId))
        .findFirst().orElseThrow(NotJoinedAppointmentException::new);

    Position appointmentPosition =
        new Position(appointment.getLatitude(),
            appointment.getLongitude());
    Position userPosition = new Position(latitude, longitude);

    double distance = DistanceUtils.getDistance(userPosition, appointmentPosition);

    if (distance <= 50) {
      appointmentEventPublishPort.publish(new AppointmentArrivalEvent(userId, appointment));
    }

    appointmentMemberPositionCachePort.upsert(userId, new Position(latitude, longitude));
  }
}
