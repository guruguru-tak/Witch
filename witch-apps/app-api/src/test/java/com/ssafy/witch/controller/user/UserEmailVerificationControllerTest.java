package com.ssafy.witch.controller.user;

import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.request.ConfirmUserEmailVerificationCodeRequest;
import com.ssafy.witch.controller.user.request.UserEmailVerificationCodeRequest;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import com.ssafy.witch.exception.user.UserEmailVerificationCodeNotValidException;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.user.VerifyUserEmailUseCase;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest({UserEmailVerificationController.class, UserRequestMapper.class})
class UserEmailVerificationControllerTest extends RestDocsTestSupport {

  @MockBean
  private VerifyUserEmailUseCase verifyUserEmailUseCase;

  @Test
  void generate_user_email_verification_code_200() throws Exception {

    String email = "test@test.com";
    UserEmailVerificationCodeRequest request =
        new UserEmailVerificationCodeRequest(email);

    mvc.perform(post("/users/email-verification-code")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("email")
                    .type(STRING)
                    .description("인증을 요청할 사용자 이메일")
                    .attributes(constraints("이메일 형식"))
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부")
            )
        ));
  }

  @Test
  void generate_user_email_verification_code_400_invalid_email_format() throws Exception {

    String email = "testtestcom";
    UserEmailVerificationCodeRequest request =
        new UserEmailVerificationCodeRequest(email);

    doThrow(new ConstraintViolationException(null))
        .when(verifyUserEmailUseCase).createUserEmailVerificationCode(any());

    mvc.perform(post("/users/email-verification-code")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.INVALID_FORMAT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @Test
  void generate_user_email_verification_code_400_duplicated_email() throws Exception {

    String email = "test@test.com";
    UserEmailVerificationCodeRequest request =
        new UserEmailVerificationCodeRequest(email);

    doThrow(new UserEmailDuplicatedException())
        .when(verifyUserEmailUseCase).createUserEmailVerificationCode(any());

    mvc.perform(post("/users/email-verification-code")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.EMAIL_ALREADY_IN_USE.getErrorCode()))
        .andDo(restDocs.document());
  }

  @Test
  void confirm_user_email_verification_code_200() throws Exception {
    ConfirmUserEmailVerificationCodeRequest request =
        new ConfirmUserEmailVerificationCodeRequest("test@test.com", "123456");

    mvc.perform(post("/users/email-verification-code/confirm")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("email")
                    .type(STRING)
                    .description("인증을 요청한 사용자 이메일")
                    .attributes(constraints("이메일 형식")),
                fieldWithPath("emailVerificationCode")
                    .type(STRING)
                    .description("발급된 인증 번호")
                    .attributes(constraints("6자리 숫자로 이루어진 문자열"))
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부")
            )
        ));
  }

  @Test
  void confirm_user_email_verification_code_400_invalid_data_format() throws Exception {
    ConfirmUserEmailVerificationCodeRequest request =
        new ConfirmUserEmailVerificationCodeRequest("testtestcom", "aaaaaa");

    mvc.perform(post("/users/email-verification-code/confirm")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andDo(restDocs.document());
  }

  @Test
  void confirm_user_email_verification_code_400_duplicated_email() throws Exception {
    ConfirmUserEmailVerificationCodeRequest request =
        new ConfirmUserEmailVerificationCodeRequest("test@test.com", "123456");

    BDDMockito.doThrow(new UserEmailDuplicatedException())
        .when(verifyUserEmailUseCase).verifyEmailVerificationCode(any());

    mvc.perform(post("/users/email-verification-code/confirm")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andDo(restDocs.document());
  }

  @Test
  void confirm_user_email_verification_code_400_invalid_email_verification_code() throws Exception {
    ConfirmUserEmailVerificationCodeRequest request =
        new ConfirmUserEmailVerificationCodeRequest("test@test.com", "123456");

    BDDMockito.doThrow(new UserEmailVerificationCodeNotValidException())
        .when(verifyUserEmailUseCase).verifyEmailVerificationCode(any());

    mvc.perform(post("/users/email-verification-code/confirm")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(
            ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH.getErrorCode()))
        .andDo(restDocs.document());
  }
}
