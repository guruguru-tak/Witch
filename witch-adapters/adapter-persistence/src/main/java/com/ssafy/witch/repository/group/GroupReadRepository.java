package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.GroupReadPort;
import com.ssafy.witch.group.GroupWithMemberUsers;
import com.ssafy.witch.group.model.GroupDetailProjection;
import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GroupReadRepository implements GroupReadPort {

  private final GroupJpaRepository groupJpaRepository;

  @Override
  public List<GroupWithLeaderProjection> readGroupsWithLeaderByUserId(String userId) {
    return groupJpaRepository.findGroupListReadModelsByUserId(userId);
  }

  @Override
  public GroupDetailProjection readGroupDetail(String userId, String groupId) {
    return groupJpaRepository.readGroupDetail(userId, groupId);
  }

  @Override
  public GroupWithMemberUsers findGroupWithFcmTokenMember(String groupId) {
    return groupJpaRepository.readGroupWithMemberUsers(groupId);
  }
}
