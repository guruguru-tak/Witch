package com.ssafy.witch.controller.appointment;

import com.ssafy.witch.appointment.UpdateAppointmentMemberPositionUseCase;
import com.ssafy.witch.controller.appointment.mapper.AppointmentRequestMapper;
import com.ssafy.witch.controller.appointment.request.UpdateAppointmentMemberPositionRequest;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UpdateAppointmentMemberPositionController {

  private final UpdateAppointmentMemberPositionUseCase updateAppointmentMemberPositionUseCase;
  private final AppointmentRequestMapper mapper;

  @PutMapping("/appointments/{appointmentId}/members/me/position")
  public WitchApiResponse<Void> updatePosition(
      @AuthenticationPrincipal String userId,
      @PathVariable("appointmentId") String appointmentId,
      @RequestBody UpdateAppointmentMemberPositionRequest request) {
    updateAppointmentMemberPositionUseCase.execute(
        mapper.toCommand(userId, appointmentId, request));
    return WitchApiResponse.success();
  }

}
