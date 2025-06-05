package com.ssafy.witch.controller.appointment.mapper;

import com.ssafy.witch.appointment.output.AppointMemberPositionListOutput;
import com.ssafy.witch.appointment.output.AppointmentDetailOutput;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.appointment.output.AppointmentWithGroupListOutput;
import com.ssafy.witch.controller.appointment.response.AppointmentDetailResponse;
import com.ssafy.witch.controller.appointment.response.AppointmentListResponse;
import com.ssafy.witch.controller.appointment.response.AppointmentMemberPositionListResponse;
import com.ssafy.witch.controller.appointment.response.AppointmentWithGroupListResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentResponseMapper {

  AppointmentListResponse toResponse(AppointmentListOutput output);

  AppointmentWithGroupListResponse toResponse(AppointmentWithGroupListOutput output);

  AppointmentDetailResponse toResponse(AppointmentDetailOutput output);

  AppointmentMemberPositionListResponse toResponse(AppointMemberPositionListOutput output);
}
