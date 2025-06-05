package com.ssafy.witch.controller.group;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.group.mapper.GroupResponseMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.Group;
import com.ssafy.witch.group.GroupReadUseCase;
import com.ssafy.witch.group.output.GroupDetailOutput;
import com.ssafy.witch.group.output.GroupWithLeaderListOutput;
import com.ssafy.witch.group.output.GroupWithLeaderOutput;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.user.output.UserBasicOutput;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({GroupReadController.class, GroupResponseMapper.class})
class GroupReadControllerTest extends RestDocsTestSupport {

  @MockBean
  private GroupReadUseCase groupReadUseCase;

  @WithMockWitchUser
  @Test
  void get_group_preview_200() throws Exception {

    Group group = new Group(
        "group-id-example",
        "구미푸파",
        "http://group.image.example"
    );

    given(groupReadUseCase.getGroupPreview(any())).willReturn(group);

    mvc.perform(get("/groups/{groupId}/preview", group.getGroupId())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("groupId")
                    .description("조회하고자 하는 모임 식별자")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.groupId")
                    .type(STRING)
                    .description("모임 식별키"),
                fieldWithPath("data.name")
                    .type(STRING)
                    .description("모임 이름"),
                fieldWithPath("data.groupImageUrl")
                    .type(STRING)
                    .description("모임 이미지 URL")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_group_preview_400_not_exist_group_id() throws Exception {

    Group group = new Group(
        "group-id-example",
        "구미푸파",
        "http://group.image.example"
    );

    given(groupReadUseCase.getGroupPreview(any()))
        .willThrow(new GroupNotFoundException());

    mvc.perform(get("/groups/{groupId}/preview", group.getGroupId())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_my_group_list_200() throws Exception {

    GroupWithLeaderListOutput output = new GroupWithLeaderListOutput(
        List.of(
            new GroupWithLeaderOutput(
                "group-id-1",
                "모임이름1",
                "http://group1.image.url",
                LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0),
                new UserBasicOutput("user-id-1", "nickname1", "http:://user1.profile.image.url")
            ),
            new GroupWithLeaderOutput(
                "group-id-2",
                "모임이름2",
                "http://group2.image.url",
                LocalDateTime.of(2001, 1, 1, 0, 0, 0, 0),
                new UserBasicOutput("user-id-2", "nickname2", "http:://user2.profile.image.url")
            )
        )
    );

    given(groupReadUseCase.getGroupWithLeaderList(any())).willReturn(output);

    mvc.perform(get("/groups/me")
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
                fieldWithPath("data.groups.[].groupId")
                    .type(STRING)
                    .description("모임 식별키"),
                fieldWithPath("data.groups.[].name")
                    .type(STRING)
                    .description("모임 이름"),
                fieldWithPath("data.groups.[].groupImageUrl")
                    .type(STRING)
                    .description("모임 이미지 URL"),
                fieldWithPath("data.groups.[].createdAt")
                    .type(STRING)
                    .description("모임 생성 시간"),
                fieldWithPath("data.groups.[].leader.userId")
                    .type(STRING)
                    .description("모임 리더 식별자"),
                fieldWithPath("data.groups.[].leader.nickname")
                    .type(STRING)
                    .description("모임 리더 닉네임"),
                fieldWithPath("data.groups.[].leader.profileImageUrl")
                    .type(STRING)
                    .description("모임 리더 닉네임")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_group_detail_200() throws Exception {

    String groupId = "example-group-id";

    GroupDetailOutput output = new GroupDetailOutput(
        groupId,
        "모임이름",
        "http://group.image.url.example",
        true,
        5
    );

    given(groupReadUseCase.getGroupDetail(any(), any())).willReturn(output);

    mvc.perform(get("/groups/{groupId}", groupId)
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
                fieldWithPath("data..groupId")
                    .type(STRING)
                    .description("모임 식별키"),
                fieldWithPath("data..name")
                    .type(STRING)
                    .description("모임 이름"),
                fieldWithPath("data..groupImageUrl")
                    .type(STRING)
                    .description("모임 이미지 URL"),
                fieldWithPath("data.isLeader")
                    .type(BOOLEAN)
                    .description("조회자 모임 리더 여부"),
                fieldWithPath("data.cntLateArrival")
                    .type(NUMBER)
                    .description("조회자 모임 내 지각 횟수")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_group_detail_400_group_not_exists() throws Exception {

    String groupId = "example-group-id";

    given(groupReadUseCase.getGroupDetail(any(), any()))
        .willThrow(new GroupNotFoundException());

    mvc.perform(get("/groups/{groupId}", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode")
            .value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_group_detail_400_not_group_member() throws Exception {

    String groupId = "example-group-id";

    given(groupReadUseCase.getGroupDetail(any(), any()))
        .willThrow(new UnauthorizedGroupAccessException());

    mvc.perform(get("/groups/{groupId}", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode")
            .value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }
}

