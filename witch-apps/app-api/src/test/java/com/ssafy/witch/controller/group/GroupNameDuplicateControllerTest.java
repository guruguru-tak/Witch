package com.ssafy.witch.controller.group;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.GroupNameDuplicatedException;
import com.ssafy.witch.group.GroupValidateUseCase;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(GroupNameDuplicateController.class)
class GroupNameDuplicateControllerTest extends RestDocsTestSupport {

  @MockBean
  private GroupValidateUseCase groupValidateUseCase;

  @WithMockWitchUser
  @Test
  void check_group_name_unique_200() throws Exception {
    String name = "구미푸파";

    mvc.perform(get("/groups/name/is-unique")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .param("name", name)
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            //스프링부트 3.0 이상부터 requestParameters 삭제되고 queryParameters로 대체
            queryParameters(
                parameterWithName("name")
                    .description("중복 검사를 진행할 모임 이름")
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
  void check_group_name_unique_400_name_duplicate() throws Exception {

    String name = "구미푸파";

    BDDMockito.doThrow(new GroupNameDuplicatedException())
            .when(groupValidateUseCase).checkGroupNameDuplication(name);

    mvc.perform(get("/groups/name/is-unique")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer sample.access.token")
        .param("name", name)
    )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.GROUP_NAME_ALREADY_IN_USE.getErrorCode()))
        .andDo(restDocs.document());
  }
}