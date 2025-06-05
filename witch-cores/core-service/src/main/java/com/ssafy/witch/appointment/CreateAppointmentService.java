package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentEventPublishPort;
import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.apoointment.AppointmentReadPort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.event.AppointmentCreatedEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.appointment.command.AppointmentCreateCommand;
import com.ssafy.witch.exception.appointment.AppointmentTimeInPastException;
import com.ssafy.witch.exception.appointment.ConflictingAppointmentTimeException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.GroupMemberPort;
import com.ssafy.witch.group.GroupPort;
import com.ssafy.witch.group.GroupReadPort;
import com.ssafy.witch.group.GroupWithMemberUsers;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateAppointmentService implements CreateAppointmentUseCase {

  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;
  private final AppointmentPort appointmentPort;
  private final AppointmentMemberPort appointmentMemberPort;
  private final OnGoingAppointmentCachePort ongoingAppointmentCachePort;
  private final AppointmentReadPort appointmentReadPort;
  private final GroupReadPort groupReadPort;
  private final AppointmentEventPublishPort appointmentEventPublishPort;

  private void verifyFutureAppointment(LocalDateTime appointmentTime) {
    LocalDateTime now = LocalDateTime.now();

    if (!appointmentTime.isAfter(now)) {
      throw new AppointmentTimeInPastException();
    }
  }

  @Override
  public Appointment createAppointment(AppointmentCreateCommand command) {
    String userId = command.getUserId();
    String groupId = command.getGroupId();
    LocalDateTime appointmentTime = command.getAppointmentTime();

    validateGroupExists(groupId);
    validateGroupAuthorization(userId, groupId);
    verifyFutureAppointment(appointmentTime);
    verifyIsAppointmentTimeConflict(userId, appointmentTime);

    Appointment newAppointment = appointmentPort.save(Appointment.createNewAppointment(
        command.getGroupId(),
        command.getName(),
        command.getSummary(),
        command.getAppointmentTime(),
        calculateStatus(command.getAppointmentTime()),
        command.getLongitude(),
        command.getLatitude(),
        command.getAddress()
    ));

    String appointmentId = newAppointment.getAppointmentId();

    appointmentMemberPort.save(
        AppointmentMember.createNewLeader(userId, appointmentId)
    );

    if (calculateStatus(command.getAppointmentTime()).equals(AppointmentStatus.ONGOING)) {
      AppointmentDetailProjection appointmentDetail = appointmentReadPort.getAppointmentDetail(
          appointmentId);
      ongoingAppointmentCachePort.save(appointmentDetail,
          Duration.between(LocalDateTime.now(), appointmentTime));
    }

    GroupWithMemberUsers groupWithFcmTokenMember = groupReadPort.findGroupWithFcmTokenMember(
        groupId);

    appointmentEventPublishPort.publish(
        new AppointmentCreatedEvent(userId, newAppointment, groupWithFcmTokenMember));

    return newAppointment;
  }

  private void verifyIsAppointmentTimeConflict(String userId, LocalDateTime appointmentTime) {
    if (appointmentPort.existsConflictAppointment(userId, appointmentTime)) {
      throw new ConflictingAppointmentTimeException();
    }
  }

  private AppointmentStatus calculateStatus(LocalDateTime appointmentTime) {
    LocalDateTime now = LocalDateTime.now();

    if (appointmentTime.isBefore(now.plusHours(1))) {
      return AppointmentStatus.ONGOING;
    }

    return AppointmentStatus.SCHEDULED;
  }

  private void validateGroupAuthorization(String userId, String groupId) {
    if (!groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }
  }

  private void validateGroupExists(String groupId) {
    if (!groupPort.existsById(groupId)) {
      throw new GroupNotFoundException();
    }
  }
}
