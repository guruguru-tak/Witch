package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.entity.appointment.AppointmentEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentJpaRepository extends
    JpaRepository<AppointmentEntity, String>, AppointmentCustomRepository {

  List<AppointmentEntity> findByAppointmentTime(LocalDateTime appointmentTime);
}
