package com.ssafy.witch.apoointment;

import com.ssafy.witch.user.UserNotification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentArrivalNotification {

  private String appointmentId;
  private String name;
  private String arrivalUserId;
  private List<UserNotification> members;

}
