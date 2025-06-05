package com.ssafy.witch.group;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.command.GetGroupJoinRequestListCommand;
import com.ssafy.witch.group.mapper.GroupJoinRequestListOutputMapper;
import com.ssafy.witch.group.model.GroupJoinRequestProjection;
import com.ssafy.witch.group.output.GroupJoinRequestListOutput;
import com.ssafy.witch.group.output.GroupJoinRequestOutput;
import com.ssafy.witch.user.model.UserBasicProjection;
import com.ssafy.witch.user.output.UserBasicOutput;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class HandleGroupJoinRequestServiceTest {

  @InjectMocks
  private HandleGroupJoinRequestService sut;

  @Mock
  private GroupJoinRequestPort groupJoinRequestPort;

  @Mock
  private GroupPort groupPort;

  @Mock
  private GroupMemberPort groupMemberPort;

  @Mock
  private GroupJoinRequestListOutputMapper groupJoinRequestListOutputMapper;

  //존재하지 않는 모임 조회
  //권한 없는 모임 조회

  @Test
  void 존재하지_않는_모임의_가입_신청_목록을_조회할_수_없다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";

    given(groupPort.existsById(groupId))
        .willReturn(false);

    //when&then
    Assertions.assertThatThrownBy(() -> {
      GetGroupJoinRequestListCommand command = new GetGroupJoinRequestListCommand(userId, groupId);
      sut.getGroupJoinRequestList(command);
    }).isInstanceOf(GroupNotFoundException.class);
  }

  @Test
  void 권한이_없는_모임의_가입_신청_목록을_조회할_수_없다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";

    given(groupPort.existsById(any()))
        .willReturn(true);

    given(groupMemberPort.isLeaderByUserIdAndGroupId(any(), any()))
        .willReturn(false);

    //when&then
    Assertions.assertThatThrownBy(() -> {
      GetGroupJoinRequestListCommand command = new GetGroupJoinRequestListCommand(userId, groupId);
      sut.getGroupJoinRequestList(command);
    }).isInstanceOf(UnauthorizedGroupAccessException.class);
  }

  @Test
  void 모임장은_모임_가입_신청_사용자_목록을_조회할_수_있다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";

    List<GroupJoinRequestProjection> dummyProjections = List.of(
        new GroupJoinRequestProjection("join-request-id-1",
            new UserBasicProjection("user-id-1", "nickname1", "http://user1.profile.image.url")),
        new GroupJoinRequestProjection("join-request-id-2",
            new UserBasicProjection("user-id-2", "nickname2", "http://user2.profile.image.url"))
    );

    GroupJoinRequestListOutput expectedOutput = new GroupJoinRequestListOutput(
        List.of(
            new GroupJoinRequestOutput(
                "join-request-id-1",
                new UserBasicOutput("user-id-1", "nickname1", "http://user1.profile.image.url")
            ),
            new GroupJoinRequestOutput(
                "join-request-id-2",
                new UserBasicOutput("user-id-2", "nickname2", "http://user2.profile.image.url")
            )
        )
    );

    given(groupPort.existsById(any()))
        .willReturn(true);

    given(groupMemberPort.isLeaderByUserIdAndGroupId(any(), any()))
        .willReturn(true);

    given(groupJoinRequestPort.readGroupJoinRequestsByGroupId(groupId))
        .willReturn(dummyProjections);

    given(groupJoinRequestListOutputMapper.toOutput(dummyProjections))
        .willReturn(expectedOutput);

    //when
    GetGroupJoinRequestListCommand command = new GetGroupJoinRequestListCommand(userId, groupId);
    GroupJoinRequestListOutput output = sut.getGroupJoinRequestList(command);

    //then
    Assertions.assertThat(output).isEqualTo(expectedOutput);
  }


}