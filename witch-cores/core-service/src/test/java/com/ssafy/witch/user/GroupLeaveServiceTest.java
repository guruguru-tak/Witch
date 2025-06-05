package com.ssafy.witch.group;

import static org.mockito.Mockito.*;

import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.GroupNotJoinedException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.command.LeaveGroupCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GroupLeaveServiceTest {

    @Mock
    private GroupPort groupPort;

    @Mock
    private GroupMemberPort groupMemberPort;

    @InjectMocks
    private LeaveGroupService leaveGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 존재하지_않는_그룹에_탈퇴를_요청하면_예외가_발생한다() {
        // given
        String userId = "user123";
        String groupId = "invalidGroup";
        LeaveGroupCommand command = new LeaveGroupCommand(userId, groupId);

        when(groupPort.existsById(groupId)).thenReturn(false);

        // when & then
        Assertions.assertThatThrownBy(() -> leaveGroupService.leaveGroup(command))
                .isInstanceOf(GroupNotFoundException.class);
    }

    @Test
    void 가입하지_않은_그룹에서_탈퇴를_요청하면_예외가_발생한다() {
        // given
        String userId = "user123";
        String groupId = "group123";
        LeaveGroupCommand command = new LeaveGroupCommand(userId, groupId);

        when(groupPort.existsById(groupId)).thenReturn(true);
        when(groupMemberPort.existsByUserIdAndGroupId(userId, groupId)).thenReturn(false);

        // when & then
        Assertions.assertThatThrownBy(() -> leaveGroupService.leaveGroup(command))
                .isInstanceOf(GroupNotJoinedException.class);
    }

    @Test
    void 그룹장이_탈퇴를_요청하면_예외가_발생한다() {
        // given
        String userId = "leader123";
        String groupId = "group123";
        LeaveGroupCommand command = new LeaveGroupCommand(userId, groupId);

        when(groupPort.existsById(groupId)).thenReturn(true);
        when(groupMemberPort.existsByUserIdAndGroupId(userId, groupId)).thenReturn(true);
        when(groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId)).thenReturn(true);

        // when & then
        Assertions.assertThatThrownBy(() -> leaveGroupService.leaveGroup(command))
                .isInstanceOf(UnauthorizedGroupAccessException.class);
    }

    @Test
    void 정상적인_탈퇴_요청이면_그룹에서_성공적으로_탈퇴한다() {
        // given
        String userId = "user123";
        String groupId = "group123";
        LeaveGroupCommand command = new LeaveGroupCommand(userId, groupId);

        when(groupPort.existsById(groupId)).thenReturn(true);
        when(groupMemberPort.existsByUserIdAndGroupId(userId, groupId)).thenReturn(true);
        when(groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId)).thenReturn(false);

        // when
        leaveGroupService.leaveGroup(command);

        // then
        verify(groupMemberPort, times(1)).deleteMember(userId, groupId);
    }
}
