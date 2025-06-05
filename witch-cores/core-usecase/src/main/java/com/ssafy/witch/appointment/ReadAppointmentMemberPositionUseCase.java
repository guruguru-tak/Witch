package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.output.AppointMemberPositionListOutput;

public interface ReadAppointmentMemberPositionUseCase {

  AppointMemberPositionListOutput readAppointmentMemberPositionList(String userId,
      String appointmentId);

}
