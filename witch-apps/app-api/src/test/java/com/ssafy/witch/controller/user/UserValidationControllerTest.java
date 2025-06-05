package com.ssafy.witch.controller.user;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import com.ssafy.witch.exception.user.UserNicknameDuplicatedException;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.user.ValidateUserUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(UserValidationController.class)
class UserValidationControllerTest extends RestDocsTestSupport {

  @MockBean
  private ValidateUserUseCase validateUserUseCase;

  @Test
  void validate_email_duplication_200() throws Exception {

    String email = "test@test.com";

    mvc.perform(get("/users/email/is-unique")
            .param("email", email))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            queryParameters(
                parameterWithName("email")
                    .description("unique함을 검증할 이메일")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부")
            )
        ));
  }

  @Test
  void validate_email_duplication_400() throws Exception {

    String email = "test@test.com";

    BDDMockito.doThrow(new UserEmailDuplicatedException())
        .when(validateUserUseCase).checkUserEmailDuplication(any());

    mvc.perform(get("/users/email/is-unique")
            .param("email", email))
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.EMAIL_ALREADY_IN_USE.getErrorCode()))
        .andExpect(status().isBadRequest())
        .andDo(restDocs.document());
  }

  @Test
  void validate_nickname_duplication_200() throws Exception {

    String nickname = "sameNick";

    mvc.perform(get("/users/nickname/is-unique")
            .param("nickname", nickname))
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            queryParameters(
                parameterWithName("nickname")
                    .description("unique함을 검증할 닉네임")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부")
            )
        ));
  }

  @Test
  void validate_nickname_duplication_400() throws Exception {

    String nickname = "sameNick";

    BDDMockito.doThrow(new UserNicknameDuplicatedException())
        .when(validateUserUseCase).checkUserNicknameDuplication(any());

    mvc.perform(get("/users/nickname/is-unique")
            .param("nickname", nickname))
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NICKNAME_ALREADY_IN_USE.getErrorCode()))
        .andExpect(status().isBadRequest())
        .andDo(restDocs.document());
  }

}
