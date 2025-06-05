package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.GroupNotJoinedException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.command.LeaveGroupCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LeaveGroupService implements LeaveGroupUseCase {

    private final GroupMemberPort groupMemberPort;
    private final GroupPort groupPort;

    @Transactional
    @Override
    public void leaveGroup(LeaveGroupCommand command) {
        String userId = command.getUserId();
        String groupId = command.getGroupId();

        // 1. 존재하지 않는 모임에 대한 요청입니다.
        // 2. 대상이 이미 가입하지 않은 모임입니다.
        // 3. 대상이 리더인 경우

        if (!groupPort.existsById(command.getGroupId())) {
            throw new GroupNotFoundException();
        }

        if (!groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
            throw new GroupNotJoinedException();
        }

        if (groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId)) {
            throw new UnauthorizedGroupAccessException();
        }

        groupMemberPort.deleteMember(userId, groupId);
    }

}
