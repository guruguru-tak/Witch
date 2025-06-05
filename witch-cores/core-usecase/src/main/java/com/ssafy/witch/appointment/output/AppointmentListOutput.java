package com.ssafy.witch.appointment.output;

import java.util.List;
import lombok.Getter;

@Getter
public class AppointmentListOutput {

  private final List<AppointmentOutput> appointments;

  public AppointmentListOutput(List<AppointmentOutput> appointments) {
    this.appointments = appointments;
  }
}
