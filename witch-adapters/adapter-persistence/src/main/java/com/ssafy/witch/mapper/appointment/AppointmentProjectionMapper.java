package com.ssafy.witch.mapper.appointment;

import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentProjection;
import com.ssafy.witch.apoointment.model.AppointmentWithGroupProjection;
import com.ssafy.witch.entity.appointment.AppointmentDetailEntityProjection;
import com.ssafy.witch.entity.appointment.AppointmentEntityProjection;
import com.ssafy.witch.entity.appointment.AppointmentWithGroupEntityProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentProjectionMapper {

  AppointmentProjection toProjection(AppointmentEntityProjection appointmentEntityProjection);

  AppointmentWithGroupProjection toProjection(
      AppointmentWithGroupEntityProjection projection);

  AppointmentDetailProjection toProjection(
      AppointmentDetailEntityProjection appointmentDetailEntityProjection);
}
