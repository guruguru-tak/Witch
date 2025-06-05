package com.ssafy.witch.controller.appointment.mapper;

import com.ssafy.witch.appointment.command.AppointmentCreateCommand;
import com.ssafy.witch.appointment.command.UpdateAppointmentMemberPositionCommand;
import com.ssafy.witch.controller.appointment.request.AppointmentCreateRequest;
import com.ssafy.witch.controller.appointment.request.UpdateAppointmentMemberPositionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentRequestMapper {

  AppointmentCreateCommand toCommand(
      String userId, String groupId,
      AppointmentCreateRequest request);

  UpdateAppointmentMemberPositionCommand toCommand(String userId, String appointmentId,
      UpdateAppointmentMemberPositionRequest request);
}
