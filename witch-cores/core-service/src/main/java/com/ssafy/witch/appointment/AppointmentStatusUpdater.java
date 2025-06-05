package com.ssafy.witch.appointment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentStatusUpdater {

  private final UpdateAppointmentUseCase updateAppointmentUseCase;

  @Scheduled(cron = "0 0/10 * * * *")
  public void startAppointments() {
    LocalDateTime afterOneHour = LocalDateTime.now()
        .plusHours(1)
        .truncatedTo(ChronoUnit.MINUTES);

    List<Appointment> appointments = updateAppointmentUseCase.startAppointments(afterOneHour);

  }

  @Scheduled(cron = "0 0/10 * * * *")
  public void endAppointments() {
    LocalDateTime now = LocalDateTime.now()
        .truncatedTo(ChronoUnit.MINUTES);

    List<Appointment> appointments = updateAppointmentUseCase.endAppointments(now);

  }

}
