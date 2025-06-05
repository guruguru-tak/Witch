package com.ssafy.witch.controller.appointment;

import com.ssafy.witch.appointment.CreateAppointmentUseCase;
import com.ssafy.witch.appointment.command.AppointmentCreateCommand;
import com.ssafy.witch.controller.appointment.mapper.AppointmentRequestMapper;
import com.ssafy.witch.controller.appointment.request.AppointmentCreateRequest;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AppointmentCreateController {

  private final CreateAppointmentUseCase createAppointmentUseCase;
  private final AppointmentRequestMapper appointmentRequestMapper;

  @PostMapping("/groups/{groupId}/appointments")
  public WitchApiResponse<Void> createAppointment(
      @AuthenticationPrincipal String userId,
      @PathVariable("groupId") String groupId,
      @RequestBody AppointmentCreateRequest request) {
    AppointmentCreateCommand command = appointmentRequestMapper.toCommand(userId, groupId, request);
    createAppointmentUseCase.createAppointment(command);
    return WitchApiResponse.success();
  }
}
