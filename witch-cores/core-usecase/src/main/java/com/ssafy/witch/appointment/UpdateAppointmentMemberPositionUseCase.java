package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.command.UpdateAppointmentMemberPositionCommand;

public interface UpdateAppointmentMemberPositionUseCase {

  void execute(UpdateAppointmentMemberPositionCommand command);
}
