package com.ssafy.witch.controller.appointment;


import static com.fasterxml.jackson.core.JsonParser.NumberType.DOUBLE;
import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.appointment.CreateAppointmentUseCase;
import com.ssafy.witch.controller.appointment.mapper.AppointmentRequestMapper;
import com.ssafy.witch.controller.appointment.request.AppointmentCreateRequest;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.appointment.AppointmentTimeInPastException;
import com.ssafy.witch.exception.appointment.ConflictingAppointmentTimeException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.validate.validator.ValidationRule;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({AppointmentCreateController.class, AppointmentRequestMapper.class})
class AppointmentCreateControllerTest extends RestDocsTestSupport {

  @MockBean
  private CreateAppointmentUseCase createAppointmentUseCase;

  @WithMockWitchUser
  @Test
  void create_appointment_200() throws Exception {

    String groupId = "group-id-example";

    String name = "약속이름";
    String summary = "약속 요약";
    LocalDateTime appointmentTime = LocalDateTime.of(2030, 3, 3, 3, 0);
    double latitude = 37.574187;
    double longitude = 126.976882;
    String address = "서울특별시 종로구 세종대로 172 광화문광장";

    AppointmentCreateRequest request = new AppointmentCreateRequest(name, summary,
        appointmentTime, latitude, longitude, address);

    mvc.perform(post("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("groupId")
                    .description("약속을 생성하고자 하는 모임 식별자")
            ),
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            requestFields(
                fieldWithPath("name")
                    .type(STRING)
                    .description("생성할 약속 이름")
                    .attributes(constraints(ValidationRule.APPOINTMENT_NAME.getErrorMessage())),
                fieldWithPath("summary")
                    .type(STRING)
                    .description("생성할 약속 요약"),
                fieldWithPath("appointmentTime")
                    .type(STRING)
                    .description("생성할 약속 시간")
                    .attributes(constraints("약속 시간은 1시간 이후의 10분 단위여야 합니다.")),
                fieldWithPath("latitude")
                    .type(DOUBLE)
                    .description("약속 장소의 위도"),
                fieldWithPath("longitude")
                    .type(DOUBLE)
                    .description("약속 장소의 경도"),
                fieldWithPath("address")
                    .type(STRING)
                    .description("약속 장소의 주소")
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
  void create_appointment_400_invalid_data_format() throws Exception {

    String groupId = "group-id-example";

    String name = "";
    String summary = "약속 요약";
    LocalDateTime appointmentTime = LocalDateTime.of(2030, 3, 3, 3, 1);
    double latitude = 200;
    double longitude = 200;
    String address = "";

    AppointmentCreateRequest request = new AppointmentCreateRequest(name, summary,
        appointmentTime, latitude, longitude, address);

    mvc.perform(post("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_appointment_400_group_not_exists() throws Exception {

    String groupId = "group-id-example";

    String name = "약속이름";
    String summary = "약속 요약";
    LocalDateTime appointmentTime = LocalDateTime.of(2030, 3, 3, 3, 0);
    double latitude = 37.574187;
    double longitude = 126.976882;
    String address = "서울특별시 종로구 세종대로 172 광화문광장";

    AppointmentCreateRequest request = new AppointmentCreateRequest(name, summary,
        appointmentTime, latitude, longitude, address);

    BDDMockito.doThrow(new GroupNotFoundException())
        .when(createAppointmentUseCase).createAppointment(any());

    mvc.perform(post("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_appointment_400_not_group_member() throws Exception {

    String groupId = "group-id-example";

    String name = "약속이름";
    String summary = "약속 요약";
    LocalDateTime appointmentTime = LocalDateTime.of(2030, 3, 3, 3, 0);
    double latitude = 37.574187;
    double longitude = 126.976882;
    String address = "서울특별시 종로구 세종대로 172 광화문광장";

    AppointmentCreateRequest request = new AppointmentCreateRequest(name, summary,
        appointmentTime, latitude, longitude, address);

    BDDMockito.doThrow(new UnauthorizedGroupAccessException())
        .when(createAppointmentUseCase).createAppointment(any());

    mvc.perform(post("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_appointment_400_past_appointment() throws Exception {

    String groupId = "group-id-example";

    String name = "약속이름";
    String summary = "약속 요약";
    LocalDateTime appointmentTime = LocalDateTime.of(2030, 3, 3, 3, 0);
    double latitude = 37.574187;
    double longitude = 126.976882;
    String address = "서울특별시 종로구 세종대로 172 광화문광장";

    AppointmentCreateRequest request = new AppointmentCreateRequest(name, summary,
        appointmentTime, latitude, longitude, address);

    BDDMockito.doThrow(new AppointmentTimeInPastException())
        .when(createAppointmentUseCase).createAppointment(any());

    mvc.perform(post("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.APPOINTMENT_TIME_IN_PAST.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_appointment_400_has_ongoing_appointment() throws Exception {

    String groupId = "group-id-example";

    String name = "약속이름";
    String summary = "약속 요약";
    LocalDateTime appointmentTime = LocalDateTime.of(2030, 3, 3, 3, 0);
    double latitude = 37.574187;
    double longitude = 126.976882;
    String address = "서울특별시 종로구 세종대로 172 광화문광장";

    AppointmentCreateRequest request = new AppointmentCreateRequest(name, summary,
        appointmentTime, latitude, longitude, address);

    BDDMockito.doThrow(new ConflictingAppointmentTimeException())
        .when(createAppointmentUseCase).createAppointment(any());

    mvc.perform(post("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(
            ErrorCode.CONFLICTING_APPOINTMENT_TIME.getErrorCode()))
        .andDo(restDocs.document());
  }
}
