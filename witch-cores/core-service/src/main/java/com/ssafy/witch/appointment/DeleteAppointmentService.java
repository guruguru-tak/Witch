package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.appointment.command.AppointmentDeleteCommand;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.NotJoinedAppointmentException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteAppointmentService implements DeleteAppointmentUseCase {

  private final AppointmentPort appointmentPort;
  private final AppointmentMemberPort appointmentMemberPort;

  @Transactional
  @Override
  public void deleteAppointment(AppointmentDeleteCommand command) {
    String userId = command.getUserId();
    String appointmentId = command.getAppointmentId();

    Appointment appointment = appointmentPort.findById(appointmentId)
        .orElseThrow(AppointmentNotFoundException::new);

    AppointmentMember appointmentMember = appointmentMemberPort.findByUserIdAndAppointmentId(userId,
            appointmentId)
        .orElseThrow(NotJoinedAppointmentException::new);

    validateUserIsLeader(appointmentMember);

    appointmentMemberPort.deleteAllByAppointmentId(appointmentId);
    appointmentPort.delete(appointment);
  }

  private void validateUserIsLeader(AppointmentMember appointmentMember) {
    if (!appointmentMember.getIsLeader()) {
      throw new UnauthorizedAppointmentAccessException();
    }
  }
}
