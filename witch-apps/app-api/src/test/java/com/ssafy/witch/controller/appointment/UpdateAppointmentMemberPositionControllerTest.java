package com.ssafy.witch.controller.appointment;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.appointment.UpdateAppointmentMemberPositionUseCase;
import com.ssafy.witch.controller.appointment.mapper.AppointmentRequestMapper;
import com.ssafy.witch.controller.appointment.request.UpdateAppointmentMemberPositionRequest;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.appointment.AppointmentNotOnGoingException;
import com.ssafy.witch.exception.appointment.NotJoinedAppointmentException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({UpdateAppointmentMemberPositionController.class, AppointmentRequestMapper.class})
class UpdateAppointmentMemberPositionControllerTest extends RestDocsTestSupport {

  @MockBean
  private UpdateAppointmentMemberPositionUseCase updateAppointmentMemberPositionUseCase;


  @WithMockWitchUser
  @Test
  void update_position_200() throws Exception {

    String appointmentId = "appointment-id-example";

    UpdateAppointmentMemberPositionRequest request = new UpdateAppointmentMemberPositionRequest(
        12.234,
        34.56
    );

    mvc.perform(put("/appointments/{appointmentId}/members/me/position", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("appointmentId")
                    .description("참여중인 약속 식별자")
            ),
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            requestFields(
                fieldWithPath("latitude")
                    .type(NUMBER)
                    .description("업데이트 할 위도"),
                fieldWithPath("longitude")
                    .type(NUMBER)
                    .description("업데이트 할 경도")
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
  void update_position_400_not_on_going_appointment() throws Exception {

    String appointmentId = "appointment-id-example";

    UpdateAppointmentMemberPositionRequest request = new UpdateAppointmentMemberPositionRequest(
        12.234,
        34.56
    );

    BDDMockito.doThrow(new AppointmentNotOnGoingException())
        .when(updateAppointmentMemberPositionUseCase).execute(any());

    mvc.perform(put("/appointments/{appointmentId}/members/me/position", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.APPOINTMENT_NOT_ONGOING.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void update_position_400_not_appointment_member() throws Exception {

    String appointmentId = "appointment-id-example";

    UpdateAppointmentMemberPositionRequest request = new UpdateAppointmentMemberPositionRequest(
        12.234,
        34.56
    );

    BDDMockito.doThrow(new NotJoinedAppointmentException())
        .when(updateAppointmentMemberPositionUseCase).execute(any());

    mvc.perform(put("/appointments/{appointmentId}/members/me/position", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NOT_JOIN_APPOINTMENT.getErrorCode()))
        .andDo(restDocs.document());
  }

}
