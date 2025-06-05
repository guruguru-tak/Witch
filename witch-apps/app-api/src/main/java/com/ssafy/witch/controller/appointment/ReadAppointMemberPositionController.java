package com.ssafy.witch.controller.appointment;

import com.ssafy.witch.appointment.ReadAppointmentMemberPositionUseCase;
import com.ssafy.witch.appointment.output.AppointMemberPositionListOutput;
import com.ssafy.witch.controller.appointment.mapper.AppointmentResponseMapper;
import com.ssafy.witch.controller.appointment.response.AppointmentMemberPositionListResponse;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReadAppointMemberPositionController {

  private final ReadAppointmentMemberPositionUseCase readAppointmentMemberPositionUseCase;
  private final AppointmentResponseMapper appointmentResponseMapper;

  @GetMapping("/appointments/{appointmentId}/members/position")
  public WitchApiResponse<AppointmentMemberPositionListResponse> getMemberPositions(
      @AuthenticationPrincipal String userId,
      @PathVariable String appointmentId) {
    AppointMemberPositionListOutput output = readAppointmentMemberPositionUseCase.readAppointmentMemberPositionList(
        userId, appointmentId);
    AppointmentMemberPositionListResponse response = appointmentResponseMapper.toResponse(output);
    return WitchApiResponse.success(response);
  }
}
