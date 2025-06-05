package com.ssafy.witch.controller.appointment;

import com.ssafy.witch.appointment.DeleteAppointmentUseCase;
import com.ssafy.witch.appointment.command.AppointmentDeleteCommand;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppointmentDeleteController {

  private final DeleteAppointmentUseCase deleteAppointmentUseCase;

  @DeleteMapping("/appointments/{appointmentId}")
  public WitchApiResponse<Void> deleteAppointment(
      @AuthenticationPrincipal String userId,
      @PathVariable("appointmentId") String appointmentId) {
    AppointmentDeleteCommand command =
        new AppointmentDeleteCommand(userId, appointmentId);
    deleteAppointmentUseCase.deleteAppointment(command);
    return WitchApiResponse.success();
  }
}
