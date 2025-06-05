package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.entity.appointment.AppointmentMemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentMemberJpaRepository extends
    JpaRepository<AppointmentMemberEntity, String> {

  boolean existsByUserIdAndAppointmentId(String userId, String appointmentId);

  Optional<AppointmentMemberEntity> findByUserIdAndAppointmentId(String userId,
      String appointmentId);

  void deleteAllByAppointmentId(String appointmentId);

}
