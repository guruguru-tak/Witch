package com.ssafy.witch.controller.appointment;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.appointment.JoinAppointmentUseCase;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.appointment.AlreadyJoinedAppointmentException;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.ConflictingAppointmentTimeException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(AppointmentJoinController.class)
class AppointmentJoinControllerTest extends RestDocsTestSupport {

  @MockBean
  private JoinAppointmentUseCase joinAppointmentUseCase;

  @WithMockWitchUser
  @Test
  void join_appointment_200() throws Exception {

    String appointmentId = "appointment-id-example";

    mvc.perform(post("/appointments/{appointmentId}/members", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("appointmentId")
                    .description("참여하고자 하는 모임 식별자")
            ),
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void join_appointment_400_not_group_member() throws Exception {

    String appointmentId = "appointment-id-example";

    BDDMockito.willThrow(new UnauthorizedGroupAccessException())
        .given(joinAppointmentUseCase).joinAppointment(any());

    mvc.perform(post("/appointments/{appointmentId}/members", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void join_appointment_400_already_appointment_member() throws Exception {

    String appointmentId = "appointment-id-example";

    BDDMockito.willThrow(new AlreadyJoinedAppointmentException())
        .given(joinAppointmentUseCase).joinAppointment(any());

    mvc.perform(post("/appointments/{appointmentId}/members", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.ALREADY_JOIN_APPOINTMENT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void join_appointment_400_appointment_not_exists() throws Exception {

    String appointmentId = "appointment-id-example";

    BDDMockito.willThrow(new AppointmentNotFoundException())
        .given(joinAppointmentUseCase).joinAppointment(any());

    mvc.perform(post("/appointments/{appointmentId}/members", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NON_EXISTENT_APPOINTMENT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void join_appointment_400_appointment_time_conflict() throws Exception {

    String appointmentId = "appointment-id-example";

    BDDMockito.willThrow(new ConflictingAppointmentTimeException())
        .given(joinAppointmentUseCase).joinAppointment(any());

    mvc.perform(post("/appointments/{appointmentId}/members", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(
                ErrorCode.CONFLICTING_APPOINTMENT_TIME.getErrorCode()))
        .andDo(restDocs.document());
  }

}
