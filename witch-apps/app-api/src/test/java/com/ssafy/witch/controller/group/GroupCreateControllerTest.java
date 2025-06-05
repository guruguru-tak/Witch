package com.ssafy.witch.controller.group;

import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.group.mapper.GroupRequestMapper;
import com.ssafy.witch.controller.group.request.GroupCreateRequest;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.file.InvalidFileOwnerException;
import com.ssafy.witch.exception.group.GroupNameDuplicatedException;
import com.ssafy.witch.group.CreateGroupUseCase;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.validate.validator.ValidationRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({GroupCreateController.class, GroupRequestMapper.class})
class GroupCreateControllerTest extends RestDocsTestSupport {

  @MockBean
  private CreateGroupUseCase createGroupUseCase;

  @WithMockWitchUser
  @Test
  void create_group_200() throws Exception {
    String name = "구미푸파";
    String groupImageObjectKey = "/group/sample-group-image.jpg";

    GroupCreateRequest request = new GroupCreateRequest(
        name, groupImageObjectKey);

    mvc.perform(post("/groups")
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
                fieldWithPath("name")
                    .type(STRING)
                    .description("생성할 모임 이름")
                    .attributes(constraints(ValidationRule.GROUP_NAME.getErrorMessage())),
                fieldWithPath("groupImageObjectKey")
                    .type(STRING)
                    .optional()
                    .description("업로드한 모임 사진의 object key")
                    .attributes(constraints("확장자가 jpg, jpeg 또는 png여야 합니다"))
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
  void create_group_400_name_duplicated() throws Exception {

    String name = "구미푸파";
    String groupImageObjectKey = "/group/sample-group-image.jpg";

    GroupCreateRequest request = new GroupCreateRequest(
        name, groupImageObjectKey);

    doThrow(new GroupNameDuplicatedException())
        .when(createGroupUseCase).createGroup(any());

    mvc.perform(post("/groups")
            .contentType(MediaType.APPLICATION_JSON)
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
  void create_group_400_invalid_file_owner() throws Exception {

    String name = "구미푸파";
    String groupImageObjectKey = "/group/sample-group-image.jpg";

    GroupCreateRequest request = new GroupCreateRequest(
        name, groupImageObjectKey);

    doThrow(new InvalidFileOwnerException())
        .when(createGroupUseCase).createGroup(any());

    mvc.perform(post("/groups")
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
