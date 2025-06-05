package com.ssafy.witch.apoointment.event;

import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AppointmentEndEvent {

  private AppointmentDetailProjection appointment;
}
