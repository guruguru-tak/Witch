package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.appointment.AppointmentMember;
import com.ssafy.witch.mapper.appointment.AppointmentMemberMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppointmentMemberRepository implements AppointmentMemberPort {

  private final AppointmentMemberJpaRepository appointmentMemberJpaRepository;
  private final AppointmentMemberMapper appointmentMemberMapper;

  @Override
  public AppointmentMember save(AppointmentMember appointmentMember) {
    return appointmentMemberMapper.toDomain(
        appointmentMemberJpaRepository.save(appointmentMemberMapper.toEntity(appointmentMember)));
  }

  @Override
  public boolean existsByUserIdAndAppointmentId(String userId, String appointmentId) {
    return appointmentMemberJpaRepository.existsByUserIdAndAppointmentId(userId, appointmentId);
  }

  @Override
  public Optional<AppointmentMember> findByUserIdAndAppointmentId(String userId,
      String appointmentId) {
    return appointmentMemberJpaRepository.findByUserIdAndAppointmentId(userId, appointmentId)
        .map(appointmentMemberMapper::toDomain);
  }

  @Override
  public void delete(AppointmentMember appointmentMember) {
    appointmentMemberJpaRepository.delete(appointmentMemberMapper.toEntity(appointmentMember));
  }

  @Override
  public void deleteAllByAppointmentId(String appointmentId) {
    appointmentMemberJpaRepository.deleteAllByAppointmentId(appointmentId);
  }
}
