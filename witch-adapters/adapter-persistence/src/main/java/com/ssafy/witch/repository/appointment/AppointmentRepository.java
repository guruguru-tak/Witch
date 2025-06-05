package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.apoointment.AppointmentReadPort;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentProjection;
import com.ssafy.witch.apoointment.model.AppointmentWithGroupProjection;
import com.ssafy.witch.appointment.Appointment;
import com.ssafy.witch.mapper.appointment.AppointmentMapper;
import com.ssafy.witch.mapper.appointment.AppointmentProjectionMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppointmentRepository implements AppointmentPort, AppointmentReadPort {

  private final AppointmentJpaRepository appointmentJpaRepository;
  private final AppointmentMapper appointmentMapper;
  private final AppointmentProjectionMapper appointmentProjectionMapper;

  @Override
  public Appointment save(Appointment appointment) {
    return appointmentMapper.toDomain(
        appointmentJpaRepository.save(appointmentMapper.toEntity(appointment))
    );
  }


  @Override
  public boolean existsConflictAppointment(String userId, LocalDateTime appointmentTime) {
    return appointmentJpaRepository.existsConflictAppointment(userId, appointmentTime);
  }

  @Override
  public Optional<Appointment> findById(String appointmentId) {
    return appointmentJpaRepository.findById(appointmentId).map(appointmentMapper::toDomain);
  }

  @Override
  public boolean existsById(String appointmentId) {
    return appointmentJpaRepository.existsById(appointmentId);
  }

  @Override
  public void delete(Appointment appointment) {
    appointmentJpaRepository.delete(appointmentMapper.toEntity(appointment));
  }

  @Override
  public List<Appointment> findAllByAppointmentTime(LocalDateTime appointmentTime) {
    return appointmentJpaRepository.findByAppointmentTime(appointmentTime)
        .stream()
        .map(appointmentMapper::toDomain)
        .toList();
  }

  @Override
  public void saveAll(List<Appointment> appointments) {
    appointmentJpaRepository.saveAll(
        appointments.stream().map(appointmentMapper::toEntity).toList());
  }


  @Override
  public List<AppointmentProjection> getAppointments(String userId, String groupId) {
    return appointmentJpaRepository.getAppointments(userId, groupId)
        .stream()
        .map(appointmentProjectionMapper::toProjection)
        .toList();
  }

  @Override
  public List<AppointmentWithGroupProjection> getMyAppointment(String userId, int year, int month) {
    return appointmentJpaRepository.getMyAppointments(userId, year, month).stream()
        .map(appointmentProjectionMapper::toProjection)
        .toList();
  }

  @Override
  public AppointmentDetailProjection getAppointmentDetail(String appointmentId) {
    return appointmentProjectionMapper.toProjection(
        appointmentJpaRepository.getAppointmentDetail(appointmentId));
  }
}
