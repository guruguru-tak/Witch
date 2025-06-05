package com.ssafy.witch.controller.group;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.group.mapper.GroupResponseMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.AlreadyJoinedGroupException;
import com.ssafy.witch.exception.group.ExceedMaxGroupParticipants;
import com.ssafy.witch.exception.group.GroupJoinRequestExistsException;
import com.ssafy.witch.exception.group.GroupJoinRequestNotFoundException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.CreateGroupJoinRequestUseCase;
import com.ssafy.witch.group.HandleGroupJoinRequestUseCase;
import com.ssafy.witch.group.output.GroupJoinRequestListOutput;
import com.ssafy.witch.group.output.GroupJoinRequestOutput;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.user.output.UserBasicOutput;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest({GroupJoinController.class, GroupResponseMapper.class})
class GroupJoinControllerTest extends RestDocsTestSupport {

  @MockBean
  public CreateGroupJoinRequestUseCase createGroupJoinRequestUseCase;

  @MockBean
  private HandleGroupJoinRequestUseCase handleGroupJoinRequestUseCase;


  @WithMockWitchUser
  @Test
  void create_group_join_request_200() throws Exception {

    String groupId = "sample-group-id";

    mvc.perform(post("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("groupId")
                    .description("가입을 요청하고자 하는 모임 식별자")
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
  void create_group_join_request_400_group_not_exists() throws Exception {

    String groupId = "sample-group-id";

    doThrow(new GroupNotFoundException())
        .when(createGroupJoinRequestUseCase).creatGroupJoinRequest(any());

    mvc.perform(post("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_group_join_request_400_already_joined_group() throws Exception {
    String groupId = "sample-group-id";

    doThrow(new AlreadyJoinedGroupException())
        .when(createGroupJoinRequestUseCase).creatGroupJoinRequest(any());

    mvc.perform(post("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.ALREADY_JOINED_GROUP.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_group_join_request_400_request_already_exists() throws Exception {
    String groupId = "sample-group-id";

    doThrow(new GroupJoinRequestExistsException())
        .when(createGroupJoinRequestUseCase).creatGroupJoinRequest(any());

    mvc.perform(post("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(
            ErrorCode.GROUP_JOIN_REQUEST_ALREADY_EXISTS.getErrorCode()))
        .andDo(restDocs.document());
  }


  @WithMockWitchUser
  @Test
  void approve_group_join_request_200() throws Exception {

    String joinRequestId = "sample-group-join-request-id";

    mvc.perform(post("/groups/join-requests/{joinRequestId}/approve", joinRequestId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("joinRequestId")
                    .description("모임 가입 요청 식별자")
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
  void approve_group_join_request_400_group_join_request_not_exists() throws Exception {

    String joinRequestId = "sample-group-join-request-id";

    BDDMockito.doThrow(new GroupJoinRequestNotFoundException())
        .when(handleGroupJoinRequestUseCase).approveGroupJoinRequest(any());

    mvc.perform(post("/groups/join-requests/{joinRequestId}/approve", joinRequestId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode")
                .value(ErrorCode.GROUP_JOIN_REQUEST_NOT_EXISTS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void approve_group_join_request_400_exceed_max_participants() throws Exception {

    String joinRequestId = "sample-group-join-request-id";

    BDDMockito.doThrow(new ExceedMaxGroupParticipants())
        .when(handleGroupJoinRequestUseCase).approveGroupJoinRequest(any());

    mvc.perform(post("/groups/join-requests/{joinRequestId}/approve", joinRequestId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(
                ErrorCode.EXCEED_MAX_GROUP_PARTICIPANTS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void approve_group_join_request_400_user_already_participant() throws Exception {

    String joinRequestId = "sample-group-join-request-id";

    BDDMockito.doThrow(new AlreadyJoinedGroupException())
        .when(handleGroupJoinRequestUseCase).approveGroupJoinRequest(any());

    mvc.perform(post("/groups/join-requests/{joinRequestId}/approve", joinRequestId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.ALREADY_JOINED_GROUP.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void approve_group_join_request_400_user_is_not_leader() throws Exception {

    String joinRequestId = "sample-group-join-request-id";

    BDDMockito.doThrow(new UnauthorizedGroupAccessException())
        .when(handleGroupJoinRequestUseCase).approveGroupJoinRequest(any());

    mvc.perform(post("/groups/join-requests/{joinRequestId}/approve", joinRequestId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void reject_group_join_request_200() throws Exception {

    String joinRequestId = "sample-group-join-request-id";

    mvc.perform(delete("/groups/join-requests/{joinRequestId}", joinRequestId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("joinRequestId")
                    .description("모임 가입 요청 식별자")
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
  void reject_group_join_request_400_group_join_request_not_exists() throws Exception {

    String joinRequestId = "sample-group-join-request-id";

    BDDMockito.doThrow(new GroupJoinRequestNotFoundException())
        .when(handleGroupJoinRequestUseCase).rejectGroupJoinRequest(any());

    mvc.perform(delete("/groups/join-requests/{joinRequestId}", joinRequestId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(
                ErrorCode.GROUP_JOIN_REQUEST_NOT_EXISTS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void reject_group_join_request_400_user_is_not_leader() throws Exception {

    String joinRequestId = "sample-group-join-request-id";

    BDDMockito.doThrow(new UnauthorizedGroupAccessException())
        .when(handleGroupJoinRequestUseCase).rejectGroupJoinRequest(any());

    mvc.perform(delete("/groups/join-requests/{joinRequestId}", joinRequestId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_group_join_request_list_200() throws Exception {
    String groupId = "test-group-id";

    GroupJoinRequestListOutput output = new GroupJoinRequestListOutput(
        List.of(
            new GroupJoinRequestOutput(
                "join-request-id-1",
                new UserBasicOutput("user-id-1", "nickname1", "http:://user1.profile.image.url")
            ),
            new GroupJoinRequestOutput(
                "join-request-id-2",
                new UserBasicOutput("user-id-2", "nickname2", "http:://user2.profile.image.url")
            )
        )
    );

    given(handleGroupJoinRequestUseCase.getGroupJoinRequestList(any())).willReturn(output);

    mvc.perform(get("/groups/{groupId}/join-requests", groupId)
        .contentType(APPLICATION_JSON)
    )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("groupId")
                    .description("모임 식별자")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.joinRequests.[].joinRequestId")
                    .type(STRING)
                    .description("가입 신청 식별자"),
                fieldWithPath("data.joinRequests.[].user.userId")
                    .type(STRING)
                    .description("모임 가입 신청 사용자 식별자"),
                fieldWithPath("data.joinRequests.[].user.nickname")
                    .type(STRING)
                    .description("모임 가입 신청 사용자 닉네임"),
                fieldWithPath("data.joinRequests.[].user.profileImageUrl")
                    .type(STRING)
                    .description("모임 가입 신청 사용자 프로필 URL")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_group_join_request_list_400_not_exist_group() throws Exception {

    String groupId = "test-group-id";

    given(handleGroupJoinRequestUseCase.getGroupJoinRequestList(any()))
        .willThrow(new GroupNotFoundException());

    mvc.perform(get("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_group_join_request_list_400_user_is_not_leader() throws Exception {

    String groupId = "test-group-id";

    given(handleGroupJoinRequestUseCase.getGroupJoinRequestList(any()))
        .willThrow(new UnauthorizedGroupAccessException());

    mvc.perform(get("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }
  
}
