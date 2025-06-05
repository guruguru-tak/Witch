package com.ssafy.witch.controller.appointment;

import com.ssafy.witch.appointment.ExitAppointmentUseCase;
import com.ssafy.witch.appointment.command.AppointmentExitCommand;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AppointmentExitController {

  private final ExitAppointmentUseCase exitAppointmentUseCase;

  @DeleteMapping("/appointments/{appointmentId}/members/me")
  public WitchApiResponse<Void> exitAppointment(
      @AuthenticationPrincipal String userId,
      @PathVariable String appointmentId) {
    AppointmentExitCommand command = new AppointmentExitCommand(userId,
        appointmentId);
    exitAppointmentUseCase.exitAppointment(command);
    return WitchApiResponse.success();
  }
}
