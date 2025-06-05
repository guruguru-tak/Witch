package com.ssafy.witch.mapper.appointment;

import com.ssafy.witch.appointment.Appointment;
import com.ssafy.witch.entity.appointment.AppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

  Appointment toDomain(AppointmentEntity entity);

  AppointmentEntity toEntity(Appointment appointment);
}
