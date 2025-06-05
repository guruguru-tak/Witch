package com.ssafy.witch.controller.appointment.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentWithGroupListResponse {

  private List<AppointmentWithGroupResponse> appointments;
}
