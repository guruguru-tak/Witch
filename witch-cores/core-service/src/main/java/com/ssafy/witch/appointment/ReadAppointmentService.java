package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.apoointment.AppointmentReadPort;
import com.ssafy.witch.appointment.mapper.AppointmentOutputMapper;
import com.ssafy.witch.appointment.output.AppointmentDetailOutput;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.appointment.output.AppointmentWithGroupListOutput;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.GroupMemberPort;
import com.ssafy.witch.group.GroupPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReadAppointmentService implements ReadAppointmentUseCase {

  private final AppointmentPort appointmentPort;
  private final GroupPort groupPort;
  private final AppointmentReadPort appointmentReadPort;
  private final GroupMemberPort groupMemberPort;
  private final AppointmentMemberPort appointmentMemberPort;

  private final AppointmentOutputMapper appointmentOutputMapper;

  @Override
  public AppointmentListOutput getAppointments(String userId, String groupId) {

    if (!groupPort.existsById(groupId)) {
      throw new GroupNotFoundException();
    }

    verifyUserInGroup(userId, groupId);
    return appointmentOutputMapper.toOutput(appointmentReadPort.getAppointments(userId, groupId));
  }

  @Override
  public AppointmentWithGroupListOutput getMyAppointments(String userId, int year, int month) {
    return appointmentOutputMapper.toWithGroupOutput(
        appointmentReadPort.getMyAppointment(userId, year, month));
  }

  @Override
  public AppointmentDetailOutput getAppointmentDetail(String userId, String appointmentId) {
    if (!appointmentPort.existsById(appointmentId)) {
      throw new AppointmentNotFoundException();
    }

    return appointmentOutputMapper.toOutput(
        appointmentReadPort.getAppointmentDetail(appointmentId));
  }


  private void verifyUserInGroup(String userId, String groupId) {
    if (!groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }
  }
}
