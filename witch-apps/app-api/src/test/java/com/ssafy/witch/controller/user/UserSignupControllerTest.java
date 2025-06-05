package com.ssafy.witch.controller.user;


import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static com.ssafy.witch.validate.validator.ValidationRule.EMAIL;
import static com.ssafy.witch.validate.validator.ValidationRule.EMAIL_VERIFICATION_CODE;
import static com.ssafy.witch.validate.validator.ValidationRule.NICKNAME;
import static com.ssafy.witch.validate.validator.ValidationRule.PASSWORD;
import static org.mockito.ArgumentMatchers.any;
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
import com.ssafy.witch.controller.user.request.UserSignupRequest;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import com.ssafy.witch.exception.user.UserEmailVerificationCodeNotValidException;
import com.ssafy.witch.exception.user.UserNicknameDuplicatedException;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.user.SignupUserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest({UserSignupController.class, UserRequestMapper.class})
class UserSignupControllerTest extends RestDocsTestSupport {

  @MockBean
  private SignupUserUseCase signupUserUseCase;

  @Test
  void generate_user_email_verification_code_200() throws Exception {

    String email = "test@test.com";
    String password = "123asd!@#";
    String nickname = "newNick";
    String emailVerificationCode = "123456";

    UserSignupRequest request =
        new UserSignupRequest(
            email,
            password,
            nickname,
            emailVerificationCode);

    mvc.perform(post("/users")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email")
                        .type(STRING)
                        .description("가입할 사용자 이메일")
                        .attributes(constraints(EMAIL.getErrorMessage())),
                    fieldWithPath("emailVerificationCode")
                        .type(STRING)
                        .description("이메일 인증 코드")
                        .attributes(constraints(EMAIL_VERIFICATION_CODE.getErrorMessage())),
                    fieldWithPath("nickname")
                        .type(STRING)
                        .description("닉네임")
                        .attributes(constraints(NICKNAME.getErrorMessage())),
                    fieldWithPath("password")
                        .type(STRING)
                        .description("패스워드")
                        .attributes(constraints(PASSWORD.getErrorMessage()))
                ),
                responseFields(
                    fieldWithPath("success")
                        .type(BOOLEAN)
                        .description("성공 여부")
                )
            )
        );
  }

  @Test
  void generate_user_email_verification_code_400_duplicated_email() throws Exception {
    String email = "test@test.com";
    String password = "123asd!@#";
    String nickname = "newNick";
    String emailVerificationCode = "123456";

    UserSignupRequest request =
        new UserSignupRequest(
            email,
            password,
            nickname,
            emailVerificationCode);

    BDDMockito.doThrow(new UserEmailDuplicatedException())
        .when(signupUserUseCase).signup(any());

    mvc.perform(post("/users")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.EMAIL_ALREADY_IN_USE.getErrorCode()))
        .andDo(restDocs.document());
  }


  @Test
  void generate_user_email_verification_code_400_duplicated_nickname() throws Exception {

    String email = "test@test.com";
    String password = "123asd!@#";
    String nickname = "newNick";
    String emailVerificationCode = "123456";

    UserSignupRequest request =
        new UserSignupRequest(
            email,
            password,
            nickname,
            emailVerificationCode);

    BDDMockito.doThrow(new UserNicknameDuplicatedException())
        .when(signupUserUseCase).signup(any());

    mvc.perform(post("/users")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NICKNAME_ALREADY_IN_USE.getErrorCode()))
        .andDo(restDocs.document());
  }

  @Test
  void generate_user_email_verification_code_400_invalid_data() throws Exception {

    String email = "";
    String password = "";
    String nickname = "";
    String emailVerificationCode = "";

    UserSignupRequest request =
        new UserSignupRequest(
            email,
            password,
            nickname,
            emailVerificationCode);

    mvc.perform(post("/users")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.INVALID_FORMAT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @Test
  void generate_user_email_verification_code_400_invalid_email_verification_code()
      throws Exception {

    String email = "test@test.com";
    String password = "123asd!@#";
    String nickname = "newNick";
    String emailVerificationCode = "123456";

    UserSignupRequest request =
        new UserSignupRequest(
            email,
            password,
            nickname,
            emailVerificationCode);

    BDDMockito.doThrow(new UserEmailVerificationCodeNotValidException())
        .when(signupUserUseCase).signup(any());

    mvc.perform(post("/users")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(
                ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH.getErrorCode()))
        .andDo(restDocs.document());
  }
}
