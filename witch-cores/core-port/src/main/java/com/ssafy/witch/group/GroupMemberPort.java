package com.ssafy.witch.group;

import java.util.List;

public interface GroupMemberPort {

  GroupMember save(GroupMember groupMember);

  boolean existsByUserIdAndGroupId(String userId, String groupId);

  List<GroupMember> findAllByGroupId(String groupId);

  boolean isLeaderByUserIdAndGroupId(String userId, String groupId);

  void deleteMember(String userId, String groupId);
}
