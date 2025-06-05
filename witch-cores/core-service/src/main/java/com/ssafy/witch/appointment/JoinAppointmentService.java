package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentEventPublishPort;
import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.apoointment.AppointmentReadPort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.appointment.command.AppointmentJoinCommand;
import com.ssafy.witch.exception.appointment.AlreadyJoinedAppointmentException;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.ConflictingAppointmentTimeException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.GroupMemberPort;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JoinAppointmentService implements JoinAppointmentUseCase {

  private final AppointmentPort appointmentPort;
  private final GroupMemberPort groupMemberPort;
  private final AppointmentMemberPort appointmentMemberPort;
  private final AppointmentEventPublishPort appointmentEventPublishPort;
  private final AppointmentReadPort appointmentReadPort;
  private final OnGoingAppointmentCachePort ongoingAppointmentCachePort;

  @Transactional
  @Override
  public void joinAppointment(AppointmentJoinCommand command) {
    String userId = command.getUserId();
    String appointmentId = command.getAppointmentId();

    Appointment appointment = appointmentPort.findById(appointmentId)
        .orElseThrow(AppointmentNotFoundException::new);

    String groupId = appointment.getGroupId();
    LocalDateTime appointmentTime = appointment.getAppointmentTime();

    verifyUserInGroup(userId, groupId);
    validateUserNotJoinedAtAppointment(userId, appointmentId);
    verifyIsAppointmentTimeConflict(userId, appointmentTime);

    AppointmentMember newMember = AppointmentMember.createNewMember(userId, appointmentId);
    appointmentMemberPort.save(newMember);

    AppointmentDetailProjection appointmentDetailProjection = appointmentReadPort.getAppointmentDetail(
        appointmentId);

    if (!Objects.isNull(appointmentDetailProjection)
        && appointmentDetailProjection.getAppointmentStatus().equals(AppointmentStatus.ONGOING)) {
      ongoingAppointmentCachePort.save(appointmentDetailProjection,
          Duration.between(LocalDateTime.now(), appointmentTime));
    }

    appointmentEventPublishPort.publish(
        new AppointmentJoinEvent(userId, appointmentDetailProjection));
  }

  private void verifyIsAppointmentTimeConflict(String userId, LocalDateTime appointmentTime) {
    if (appointmentPort.existsConflictAppointment(userId, appointmentTime)) {
      throw new ConflictingAppointmentTimeException();
    }
  }

  private void verifyUserInGroup(String userId, String groupId) {
    if (!groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }
  }

  private void validateUserNotJoinedAtAppointment(String userId, String appointmentId) {
    if (appointmentMemberPort.existsByUserIdAndAppointmentId(userId, appointmentId)) {
      throw new AlreadyJoinedAppointmentException();
    }
  }
}
