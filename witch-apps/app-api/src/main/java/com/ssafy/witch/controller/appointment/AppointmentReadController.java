package com.ssafy.witch.controller.appointment;

import com.ssafy.witch.appointment.ReadAppointmentUseCase;
import com.ssafy.witch.appointment.output.AppointmentDetailOutput;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.appointment.output.AppointmentWithGroupListOutput;
import com.ssafy.witch.controller.appointment.mapper.AppointmentResponseMapper;
import com.ssafy.witch.controller.appointment.response.AppointmentDetailResponse;
import com.ssafy.witch.controller.appointment.response.AppointmentListResponse;
import com.ssafy.witch.controller.appointment.response.AppointmentWithGroupListResponse;
import com.ssafy.witch.response.WitchApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class AppointmentReadController {

  private final ReadAppointmentUseCase readAppointmentUseCase;
  private final AppointmentResponseMapper appointmentResponseMapper;

  @GetMapping("/groups/{groupId}/appointments")
  public WitchApiResponse<AppointmentListResponse> getAppointments(
      @AuthenticationPrincipal String userId,
      @PathVariable("groupId") String groupId) {
    AppointmentListOutput output = readAppointmentUseCase.getAppointments(userId, groupId);
    AppointmentListResponse response = appointmentResponseMapper.toResponse(output);
    return WitchApiResponse.success(response);
  }

  @GetMapping("/appointments/me")
  public WitchApiResponse<AppointmentWithGroupListResponse> getMyAppointments(
      @AuthenticationPrincipal String userId,
      @RequestParam @Validated @Min(2025) int year,
      @RequestParam @Validated @Min(1) @Max(12) int month
  ) {
    AppointmentWithGroupListOutput output = readAppointmentUseCase.getMyAppointments(userId, year,
        month);
    AppointmentWithGroupListResponse response = appointmentResponseMapper.toResponse(output);
    return WitchApiResponse.success(response);
  }

  @GetMapping("/appointments/{appointmentId}")
  public WitchApiResponse<AppointmentDetailResponse> getAppointmentDetail(
      @AuthenticationPrincipal String userId,
      @PathVariable String appointmentId) {
    AppointmentDetailOutput output =
        readAppointmentUseCase.getAppointmentDetail(userId, appointmentId);
    AppointmentDetailResponse response = appointmentResponseMapper.toResponse(output);
    return WitchApiResponse.success(response);
  }
}
