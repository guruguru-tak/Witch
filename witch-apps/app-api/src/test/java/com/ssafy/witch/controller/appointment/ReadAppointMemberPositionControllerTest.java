package com.ssafy.witch.controller.appointment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.appointment.ReadAppointmentMemberPositionUseCase;
import com.ssafy.witch.appointment.output.AppointMemberPositionListOutput;
import com.ssafy.witch.appointment.output.AppointMemberPositionOutput;
import com.ssafy.witch.controller.appointment.mapper.AppointmentResponseMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.appointment.AppointmentNotOnGoingException;
import com.ssafy.witch.exception.appointment.NotJoinedAppointmentException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({ReadAppointMemberPositionController.class, AppointmentResponseMapper.class})
class ReadAppointMemberPositionControllerTest extends RestDocsTestSupport {

  @MockBean
  private ReadAppointmentMemberPositionUseCase readAppointmentMemberPositionUseCase;

  @WithMockWitchUser
  @Test
  void get_appointment_member_position_list_200() throws Exception {

    String appointmentId = "example-appointment-id";

    AppointMemberPositionListOutput output = new AppointMemberPositionListOutput(
        List.of(
            new AppointMemberPositionOutput(
                "example-user-id-1",
                "닉네임1",
                "http://profile.image.url.example1",
                123.45,
                45.6
            ),
            new AppointMemberPositionOutput(
                "example-user-id-2",
                "닉네임2",
                "http://profile.image.url.example2",
                45.6,
                23.45
            )
        )
    );

    given(readAppointmentMemberPositionUseCase.readAppointmentMemberPositionList(any(),
        any())).willReturn(output);

    mvc.perform(get("/appointments/{appointmentId}/members/position", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("appointmentId")
                    .description("조회하고자 하는 약속 식별자")
            ),
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.positions.[].userId")
                    .type(STRING)
                    .description("사용자 식별자"),
                fieldWithPath("data.positions.[].nickname")
                    .type(STRING)
                    .description("사용자 닉네임"),
                fieldWithPath("data.positions.[].profileImageUrl")
                    .type(STRING)
                    .description("사용자 프로필 URL"),
                fieldWithPath("data.positions.[].latitude")
                    .type(NUMBER)
                    .description("위도"),
                fieldWithPath("data.positions.[].longitude")
                    .type(NUMBER)
                    .description("경도")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_appointment_member_position_list_400_not_on_going_appointment() throws Exception {

    String appointmentId = "example-appointment-id";

    doThrow(new AppointmentNotOnGoingException())
        .when(readAppointmentMemberPositionUseCase).readAppointmentMemberPositionList(any(), any());

    mvc.perform(get("/appointments/{appointmentId}/members/position", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.APPOINTMENT_NOT_ONGOING.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_appointment_member_position_list_400_not_join_appointment() throws Exception {

    String appointmentId = "example-appointment-id";

    doThrow(new NotJoinedAppointmentException())
        .when(readAppointmentMemberPositionUseCase).readAppointmentMemberPositionList(any(), any());

    mvc.perform(get("/appointments/{appointmentId}/members/position", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NOT_JOIN_APPOINTMENT.getErrorCode()))
        .andDo(restDocs.document());
  }
}
