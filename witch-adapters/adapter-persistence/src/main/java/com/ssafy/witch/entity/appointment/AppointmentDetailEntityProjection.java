package com.ssafy.witch.entity.appointment;

import com.ssafy.witch.apoointment.model.AppointmentMemberProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentDetailEntityProjection {

  private String appointmentId;
  private String name;
  private AppointmentEntityStatus appointmentStatus;
  private String summary;
  private LocalDateTime appointmentTime;
  private String address;
  private Double longitude;
  private Double latitude;

  private List<AppointmentMemberProjection> members;
}
