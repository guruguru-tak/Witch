package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.mapper.GroupMemberOutputMapper;
import com.ssafy.witch.group.model.GroupMemberProjection;
import com.ssafy.witch.group.output.GroupMemberListOutput;
import com.ssafy.witch.group.query.GroupMemberListQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReadGroupMemberService implements ReadGroupMemberUseCase {

  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;
  private final GroupMemberReadPort groupMemberReadPort;
  private final GroupMemberOutputMapper groupMemberOutputMapper;

  @Transactional(readOnly = true)
  @Override
  public GroupMemberListOutput getGroupMembers(GroupMemberListQuery query) {
    String userId = query.getUserId();
    String groupId = query.getGroupId();

    validateGroupExists(groupId);
    validateUserGroupMember(userId, groupId);

    List<GroupMemberProjection> groupMemberProjections =
        groupMemberReadPort.readGroupMemberList(groupId);

    return groupMemberOutputMapper.toOutput(groupMemberProjections);
  }

  private void validateGroupExists(String groupId) {
    if (!groupPort.existsById(groupId)) {
      throw new GroupNotFoundException();
    }
  }

  private void validateUserGroupMember(String userId, String groupId) {
    if (!groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }
  }
}
