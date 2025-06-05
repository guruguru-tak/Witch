package com.ssafy.witch.controller.group;

import static com.fasterxml.jackson.databind.node.JsonNodeType.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.group.mapper.GroupMemberResponseMapper;
import com.ssafy.witch.group.ReadGroupMemberUseCase;
import com.ssafy.witch.group.output.GroupMemberListOutput;
import com.ssafy.witch.group.output.GroupMemberOutput;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({GroupMemberReadController.class, GroupMemberResponseMapper.class})
class GroupMemberReadControllerTest extends RestDocsTestSupport {


  @MockBean
  private ReadGroupMemberUseCase readGroupMemberUseCase;

  @WithMockWitchUser
  @Test
  void get_group_member_list_200() throws Exception {

    String groupId = "group-id-example";

    GroupMemberListOutput output = new GroupMemberListOutput(
        List.of(
            new GroupMemberOutput(
                "example-user-id-1",
                "닉네임1",
                "http://profile.image.url.example1",
                true
            ),
            new GroupMemberOutput(
                "example-user-id-2",
                "닉네임2",
                "http://profile.image.url.example2",
                false
            ),
            new GroupMemberOutput(
                "example-user-id-3",
                "닉네임3",
                "http://profile.image.url.example3",
                false
            )
        )
    );

    given(readGroupMemberUseCase.getGroupMembers(any())).willReturn(output);

    mvc.perform(get("/groups/{groupId}/members", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("groupId")
                    .description("모임원 목록 조회를 요청하고자 하는 모임 식별자")
            ),
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.members.[].userId")
                    .type(STRING)
                    .description("사용자 식별자"),
                fieldWithPath("data.members.[].nickname")
                    .type(STRING)
                    .description("사용자 닉네임"),
                fieldWithPath("data.members.[].profileImageUrl")
                    .type(STRING)
                    .description("사용자 프로필 이미지 URL"),
                fieldWithPath("data.members.[].isLeader")
                    .type(BOOLEAN)
                    .description("사용자 모임 리더 여부")
            )
        ));
  }
}
