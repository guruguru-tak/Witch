package com.ssafy.witch.appointment.mapper;

import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentProjection;
import com.ssafy.witch.apoointment.model.AppointmentWithGroupProjection;
import com.ssafy.witch.appointment.output.AppointmentDetailOutput;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.appointment.output.AppointmentOutput;
import com.ssafy.witch.appointment.output.AppointmentWithGroupListOutput;
import com.ssafy.witch.appointment.output.AppointmentWithGroupOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentOutputMapper {

  default AppointmentListOutput toOutput(List<AppointmentProjection> projections) {
    return new AppointmentListOutput(appointmentProjectionToOutputList(projections));
  }

  default AppointmentWithGroupListOutput toWithGroupOutput(
      List<AppointmentWithGroupProjection> projections) {
    return new AppointmentWithGroupListOutput(
        appointmentWithGroupProjectionToAppointmentWithGroupOutputList(projections));
  }

  List<AppointmentOutput> appointmentProjectionToOutputList(
      List<AppointmentProjection> projections);

  List<AppointmentWithGroupOutput> appointmentWithGroupProjectionToAppointmentWithGroupOutputList(
      List<AppointmentWithGroupProjection> projections);

  AppointmentDetailOutput toOutput(AppointmentDetailProjection projection);
}
