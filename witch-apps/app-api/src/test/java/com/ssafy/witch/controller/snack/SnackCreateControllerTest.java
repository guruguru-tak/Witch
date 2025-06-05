package com.ssafy.witch.controller.snack;

import static com.fasterxml.jackson.core.JsonParser.NumberType.DOUBLE;
import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static org.mockito.Mockito.any;
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

import com.ssafy.witch.controller.snack.mapper.SnackRequestMapper;
import com.ssafy.witch.controller.snack.request.SnackCreateRequest;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.exception.file.InvalidFileOwnerException;
import com.ssafy.witch.exception.snack.NotOngoingAppointmentException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.snack.CreateSnackUseCase;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({SnackCreateController.class, SnackRequestMapper.class})
class SnackCreateControllerTest extends RestDocsTestSupport {

  @MockBean
  private CreateSnackUseCase createSnackUseCase;

  @WithMockWitchUser
  @Test
  void create_snack_200() throws Exception {
    String appointmentId = "test-appointment-id";

    Double longitude = 138.23;
    Double latitude = 37.927;
    String snackImageObjectKey = "snack/test-image.jpg";
    String snackSoundObjectKey = "snack/test-sound.mpeg";

    SnackCreateRequest request = new SnackCreateRequest(longitude, latitude, snackImageObjectKey, snackSoundObjectKey);

    mvc.perform(post("/appointments/{appointmentId}/snacks", appointmentId)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer sample.access.token")
        .content(objectMapper.writeValueAsString(request))
    )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("appointmentId")
                    .description("스낵을 생성하고자 하는 약속 식별자")
            ),
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            requestFields(
                fieldWithPath("longitude")
                    .type(DOUBLE)
                    .description("스낵을 생성할 장소의 경도"),
                fieldWithPath("latitude")
                    .type(DOUBLE)
                    .description("스낵을 생성할 장소의 위도"),
                fieldWithPath("snackImageObjectKey")
                    .type(STRING)
                    .description("업로드할 스낵 사진의 object key")
                    .attributes(constraints("확장자가 jpg, jpeg 또는 png여야 합니다.")),
                fieldWithPath("snackSoundObjectKey")
                    .type(STRING)
                    .optional()
                    .description("업로드할 스낵 음성의 object key")
                    .attributes(constraints("확장자가 mp3 또는 mpeg여야 합니다."))
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
  void create_snack_400_appointment_not_exists() throws Exception {
    String appointmentId = "test-appointment-id";

    Double longitude = 138.23;
    Double latitude = 37.927;
    String snackImageObjectKey = "snack/test-image.jpg";
    String snackSoundObjectKey = "snack/test-sound.mpeg";

    SnackCreateRequest request = new SnackCreateRequest(longitude, latitude, snackImageObjectKey, snackSoundObjectKey);

    BDDMockito
        .doThrow(new AppointmentNotFoundException())
        .when(createSnackUseCase).createSnack(any());

    mvc.perform(post("/appointments/{appointmentId}/snacks", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NON_EXISTENT_APPOINTMENT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_snack_400_appointment_not_ongoing() throws Exception {
    String appointmentId = "test-appointment-id";

    Double longitude = 138.23;
    Double latitude = 37.927;
    String snackImageObjectKey = "snack/test-image.jpg";
    String snackSoundObjectKey = "snack/test-sound.mpeg";

    SnackCreateRequest request = new SnackCreateRequest(longitude, latitude, snackImageObjectKey, snackSoundObjectKey);

    BDDMockito
        .doThrow(new NotOngoingAppointmentException())
        .when(createSnackUseCase).createSnack(any());

    mvc.perform(post("/appointments/{appointmentId}/snacks", appointmentId)
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
  void create_snack_400_not_appointment_member() throws Exception {
    String appointmentId = "test-appointment-id";

    Double longitude = 138.23;
    Double latitude = 37.927;
    String snackImageObjectKey = "snack/test-image.jpg";
    String snackSoundObjectKey = "snack/test-sound.mpeg";

    SnackCreateRequest request = new SnackCreateRequest(longitude, latitude, snackImageObjectKey, snackSoundObjectKey);

    BDDMockito
        .doThrow(new UnauthorizedAppointmentAccessException())
        .when(createSnackUseCase).createSnack(any());

    mvc.perform(post("/appointments/{appointmentId}/snacks", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_APPOINTMENT_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_snack_400_invalid_file_owner() throws Exception {
    String appointmentId = "test-appointment-id";

    Double longitude = 138.23;
    Double latitude = 37.927;
    String snackImageObjectKey = "snack/test-image.jpg";
    String snackSoundObjectKey = "snack/test-sound.mpeg";

    SnackCreateRequest request = new SnackCreateRequest(longitude, latitude, snackImageObjectKey, snackSoundObjectKey);

    BDDMockito
        .doThrow(new InvalidFileOwnerException())
        .when(createSnackUseCase).createSnack(any());

    mvc.perform(post("/appointments/{appointmentId}/snacks", appointmentId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.INVALID_FILE_OWNER.getErrorCode()))
        .andDo(restDocs.document());
  }
}