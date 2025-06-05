package com.ssafy.witch.apoointment.event;

import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AppointmentExitEvent {

  private User exitUser;
  private AppointmentDetailProjection appointment;
}
