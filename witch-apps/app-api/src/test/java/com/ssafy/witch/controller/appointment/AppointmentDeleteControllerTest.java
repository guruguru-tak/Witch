package com.ssafy.witch.controller.appointment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.appointment.DeleteAppointmentUseCase;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.NotJoinedAppointmentException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(AppointmentDeleteController.class)
class AppointmentDeleteControllerTest extends RestDocsTestSupport {

  @MockBean
  private DeleteAppointmentUseCase deleteAppointmentUseCase;

  @WithMockWitchUser
  @Test
  void delete_appointment_200() throws Exception {

    String appointmentId = "appointment-id-example";

    mvc.perform(delete("/appointments/{appointmentId}", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("appointmentId")
                    .description("삭제하고자 하는 모임 식별자")
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
  void delete_appointment_400_appointment_not_exists() throws Exception {

    String appointmentId = "appointment-id-example";

    doThrow(new AppointmentNotFoundException())
        .when(deleteAppointmentUseCase).deleteAppointment(any());

    mvc.perform(delete("/appointments/{appointmentId}", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode")
                .value(ErrorCode.NON_EXISTENT_APPOINTMENT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void delete_appointment_400_not_appointment_member() throws Exception {

    String appointmentId = "appointment-id-example";

    doThrow(new NotJoinedAppointmentException())
        .when(deleteAppointmentUseCase).deleteAppointment(any());

    mvc.perform(delete("/appointments/{appointmentId}", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode")
                .value(ErrorCode.NOT_JOIN_APPOINTMENT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void delete_appointment_400_user_is_not_leader() throws Exception {

    String appointmentId = "appointment-id-example";

    doThrow(new UnauthorizedAppointmentAccessException())
        .when(deleteAppointmentUseCase).deleteAppointment(any());

    mvc.perform(delete("/appointments/{appointmentId}", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode")
                .value(ErrorCode.UNAUTHORIZED_APPOINTMENT_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }

}
