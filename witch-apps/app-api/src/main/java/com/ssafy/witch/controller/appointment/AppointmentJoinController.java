package com.ssafy.witch.controller.appointment;

import com.ssafy.witch.appointment.JoinAppointmentUseCase;
import com.ssafy.witch.appointment.command.AppointmentJoinCommand;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppointmentJoinController {

  private final JoinAppointmentUseCase joinAppointmentUseCase;

  @PostMapping("/appointments/{appointmentId}/members")
  public WitchApiResponse<Void> joinAppointment(
      @AuthenticationPrincipal String userId,
      @PathVariable("appointmentId") String appointmentId) {
    AppointmentJoinCommand command = new AppointmentJoinCommand(userId, appointmentId);
    joinAppointmentUseCase.joinAppointment(command);
    return WitchApiResponse.success();
  }
}
