package com.ssafy.witch.apoointment;

import com.ssafy.witch.user.UserNotification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentEndNotification {

  private String appointmentId;
  private String name;
  private LocalDateTime appointmentTime;
  private List<UserNotification> members;

}
