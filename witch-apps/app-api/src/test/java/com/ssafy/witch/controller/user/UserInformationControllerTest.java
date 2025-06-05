package com.ssafy.witch.controller.user;

import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static com.ssafy.witch.validate.validator.ValidationRule.NICKNAME;
import static com.ssafy.witch.validate.validator.ValidationRule.PASSWORD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.mapper.UserResponseMapper;
import com.ssafy.witch.controller.user.request.UserNicknameChangeRequest;
import com.ssafy.witch.controller.user.request.UserPasswordChangeRequest;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.user.IncorrectPasswordException;
import com.ssafy.witch.exception.user.UserNicknameDuplicatedException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.user.ChangeUserInformationUseCase;
import com.ssafy.witch.user.ReadUserUseCase;
import com.ssafy.witch.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({UserInformationController.class, UserRequestMapper.class, UserResponseMapper.class})
class UserInformationControllerTest extends RestDocsTestSupport {

  @MockBean
  ReadUserUseCase readUserUseCase;

  @MockBean
  private ChangeUserInformationUseCase changeUserInformationUseCase;

  @WithMockWitchUser
  @Test
  void change_nickname_200() throws Exception {

    String nickname = "newNick";

    UserNicknameChangeRequest request = new UserNicknameChangeRequest(nickname);

    mvc.perform(patch("/users/me/nickname")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            requestFields(
                fieldWithPath("nickname")
                    .type(STRING)
                    .description("변경할 닉네임")
                    .attributes(constraints(NICKNAME.getErrorMessage()))
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
  void change_nickname_400_invalid_nickname_format() throws Exception {

    String nickname = "";

    UserNicknameChangeRequest request = new UserNicknameChangeRequest(nickname);

    mvc.perform(patch("/users/me/nickname")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.INVALID_FORMAT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void change_nickname_400_duplicated_nickname() throws Exception {
    String nickname = "newNick";

    UserNicknameChangeRequest request = new UserNicknameChangeRequest(nickname);

    doThrow(new UserNicknameDuplicatedException())
        .when(changeUserInformationUseCase).changeUserNickname(any());

    mvc.perform(patch("/users/me/nickname")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NICKNAME_ALREADY_IN_USE.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void change_password_200() throws Exception {

    String password = "123123@@";
    String newPassword = "456456@@";

    UserPasswordChangeRequest request = new UserPasswordChangeRequest(password, newPassword);

    mvc.perform(patch("/users/me/password")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            requestFields(
                fieldWithPath("password")
                    .type(STRING)
                    .description("기존 비밀번호"),
                fieldWithPath("newPassword")
                    .type(STRING)
                    .description("새로운 비밀번호")
                    .attributes(constraints(PASSWORD.getErrorMessage()))
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
  void change_password_400_invalid_password_format() throws Exception {

    String password = null;
    String newPassword = "";

    UserPasswordChangeRequest request = new UserPasswordChangeRequest(password, newPassword);

    mvc.perform(patch("/users/me/password")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.INVALID_FORMAT.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void change_password_400_incorrect_password() throws Exception {

    String password = "123123@@";
    String newPassword = "456456@@";

    UserPasswordChangeRequest request = new UserPasswordChangeRequest(password, newPassword);

    doThrow(new IncorrectPasswordException())
        .when(changeUserInformationUseCase).changePassword(any());

    mvc.perform(patch("/users/me/password")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.INCORRECT_PASSWORD.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_user_200() throws Exception {

    User user = new User(
        "user-id-example",
        "test@test.com",
        "pasword-to-not-response",
        "happy",
        "http://test.file.url"
    );

    given(readUserUseCase.getUser(any())).willReturn(user);

    mvc.perform(get("/users/me")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.userId")
                    .type(STRING)
                    .description("사용자 식별키"),
                fieldWithPath("data.email")
                    .type(STRING)
                    .description("사용자 이메일"),
                fieldWithPath("data.nickname")
                    .type(STRING)
                    .description("사용자 닉네임"),
                fieldWithPath("data.profileImageUrl")
                    .type(STRING)
                    .description("사용자 프로필 이미지 주소")
            )
        ));
  }
}
