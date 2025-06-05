package com.ssafy.witch.mapper.appointment;

import com.ssafy.witch.appointment.AppointmentMember;
import com.ssafy.witch.entity.appointment.AppointmentMemberEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMemberMapper {

  AppointmentMemberEntity toEntity(AppointmentMember appointmentMember);

  AppointmentMember toDomain(AppointmentMemberEntity appointmentMemberEntity);
}
