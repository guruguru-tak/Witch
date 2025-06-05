package com.ssafy.witch.controller.group;

import static com.fasterxml.jackson.databind.node.JsonNodeType.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.GroupNotJoinedException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.LeaveGroupUseCase;
import com.ssafy.witch.group.command.LeaveGroupCommand;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(GroupLeaveController.class)
class GroupLeaveControllerTest extends RestDocsTestSupport {

    @MockBean
    private LeaveGroupUseCase leaveGroupUseCase;

    private static final String GROUP_ID = "group123";

    // 정상적인 모임 탈퇴 요청
    @WithMockWitchUser
    @Test
    void 정상적인_요청이면_200_응답을_반환한다() throws Exception {
        willDoNothing().given(leaveGroupUseCase).leaveGroup(any(LeaveGroupCommand.class));

        mvc.perform(delete("/groups/{groupId}/members/me", GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer sample.access.token")
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("groupId").description("탈퇴할 모임 식별자")
                        ),
                        responseFields(
                                fieldWithPath("success").type(BOOLEAN).description("성공 여부")
                        )
                ));
    }

    // 존재하지 않는 그룹에 대한 탈퇴 요청
    @WithMockWitchUser
    @Test
    void 존재하지_않는_그룹이면_400_응답을_반환한다() throws Exception {
        willThrow(new GroupNotFoundException()).given(leaveGroupUseCase)
                .leaveGroup(any(LeaveGroupCommand.class));

        mvc.perform(delete("/groups/{groupId}/members/me", "invalidGroup")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
                .andDo(restDocs.document());
    }

    // 가입하지 않은 그룹에 대한 탈퇴 요청
    @WithMockWitchUser
    @Test
    void 가입하지_않은_그룹이면_400_응답을_반환한다() throws Exception {
        willThrow(new GroupNotJoinedException()).given(leaveGroupUseCase)
                .leaveGroup(any(LeaveGroupCommand.class));

        mvc.perform(delete("/groups/{groupId}/members/me", GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer sample.access.token")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.errorCode")
                        .value(ErrorCode.NOT_JOINED_MEETING.getErrorCode()))
                .andDo(restDocs.document());
    }

    // 그룹장이 탈퇴 요청을 하면
    @WithMockWitchUser
    @Test
    void 그룹장이_탈퇴_요청하면_400_응답을_반환한다() throws Exception {
        willThrow(new UnauthorizedGroupAccessException()).given(leaveGroupUseCase)
                .leaveGroup(any(LeaveGroupCommand.class));

        mvc.perform(delete("/groups/{groupId}/members/me", GROUP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer sample.access.token")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
                .andDo(restDocs.document());
    }
}
