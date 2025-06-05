package com.ssafy.witch.mapper.appointment;

import com.ssafy.witch.appointment.AppointmentStatus;
import com.ssafy.witch.entity.appointment.AppointmentEntityStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface AppointmentStatusMapper {

  default AppointmentStatus toDomain(AppointmentEntityStatus status) {
    return status != null ? AppointmentStatus.valueOf(status.name()) : null;
  }

  default AppointmentEntityStatus toEntity(AppointmentStatus status) {
    return status != null ? AppointmentEntityStatus.valueOf(status.name()) : null;
  }
}
