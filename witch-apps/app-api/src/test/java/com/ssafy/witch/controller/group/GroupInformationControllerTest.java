package com.ssafy.witch.controller.group;

import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static com.ssafy.witch.validate.validator.ValidationRule.GROUP_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.group.mapper.GroupRequestMapper;
import com.ssafy.witch.controller.group.request.GroupNameChangeRequest;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.GroupNameDuplicatedException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.ChangeGroupInformationUseCase;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest({GroupInformationController.class, GroupRequestMapper.class})
class GroupInformationControllerTest extends RestDocsTestSupport {

  @MockBean
  private ChangeGroupInformationUseCase changeGroupInformationUseCase;


  @WithMockWitchUser
  @Test
  void change_group_name_200() throws Exception {

    String name = "구미푸파";
    String groupId = "sample-group-id";

    GroupNameChangeRequest request = new GroupNameChangeRequest(name);

    mvc.perform(patch("/groups/{groupId}/name", groupId)
            .contentType(APPLICATION_JSON)
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
                fieldWithPath("name")
                    .type(STRING)
                    .description("변경할 그룹 이름")
                    .attributes(constraints(GROUP_NAME.getErrorMessage()))
            ),
            pathParameters(
                parameterWithName("groupId")
                    .description("변경할 모임 번호")
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
  void change_group_name_400_group_not_exists() throws Exception {
    String name = "구미푸파";
    String groupId = "sample-group-id";

    GroupNameChangeRequest request = new GroupNameChangeRequest(name);

    doThrow(new GroupNotFoundException())
        .when(changeGroupInformationUseCase).changeGroupName(any());

    mvc.perform(patch("/groups/{groupId}/name", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }


  @WithMockWitchUser
  @Test
  void change_group_name_400_duplicated_name() throws Exception {
    String name = "구미푸파";
    String groupId = "sample-group-id";

    GroupNameChangeRequest request = new GroupNameChangeRequest(name);

    doThrow(new GroupNameDuplicatedException())
        .when(changeGroupInformationUseCase).changeGroupName(any());

    mvc.perform(patch("/groups/{groupId}/name", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.GROUP_NAME_ALREADY_IN_USE.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void change_group_name_403_user_is_not_leader() throws Exception {
    String name = "구미푸파";
    String groupId = "sample-group-id";

    GroupNameChangeRequest request = new GroupNameChangeRequest(name);

    doThrow(new UnauthorizedGroupAccessException())
        .when(changeGroupInformationUseCase).changeGroupName(any());

    mvc.perform(patch("/groups/{groupId}/name", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
//        .andExpect(status().isForbidden())
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }
}
