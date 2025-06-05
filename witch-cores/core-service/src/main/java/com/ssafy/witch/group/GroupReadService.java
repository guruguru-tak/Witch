package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.mapper.GroupOutputMapper;
import com.ssafy.witch.group.model.GroupDetailProjection;
import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import com.ssafy.witch.group.output.GroupDetailOutput;
import com.ssafy.witch.group.output.GroupWithLeaderListOutput;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GroupReadService implements GroupReadUseCase {

  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;
  private final GroupReadPort groupReadPort;

  private final GroupOutputMapper groupOutputMapper;

  @Transactional(readOnly = true)
  @Override
  public Group getGroupPreview(String groupId) {
    return groupPort.findById(groupId).orElseThrow(GroupNotFoundException::new);
  }

  @Override
  public GroupWithLeaderListOutput getGroupWithLeaderList(String userId) {
    List<GroupWithLeaderProjection> projections = groupReadPort.readGroupsWithLeaderByUserId(
        userId);

    return groupOutputMapper.toOutput(projections);
  }

  @Override
  public GroupDetailOutput getGroupDetail(String userId, String groupId) {
    if (!groupPort.existsById(groupId)) {
      throw new GroupNotFoundException();
    }

    if (!groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }

    GroupDetailProjection projection = groupReadPort.readGroupDetail(userId, groupId);
    return groupOutputMapper.toOutput(projection);
  }

}
