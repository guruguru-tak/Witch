package com.ssafy.witch.appointment.output;

import com.ssafy.witch.appointment.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentDetailOutput {


  private String appointmentId;
  private String name;
  private AppointmentStatus appointmentStatus;
  private String summary;
  private LocalDateTime appointmentTime;
  private String address;
  private Double latitude;
  private Double longitude;

  private List<AppointmentMemberOutput> members;

}
