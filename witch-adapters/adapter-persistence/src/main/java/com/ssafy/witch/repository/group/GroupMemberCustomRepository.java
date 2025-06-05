package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.model.GroupMemberProjection;
import java.util.List;

public interface GroupMemberCustomRepository {

  boolean isLeaderByUserIdAndGroupId(String userId, String groupId);

  List<GroupMemberProjection> readGroupMemberList(String groupId);
}
