package com.ssafy.witch.group;

import com.ssafy.witch.group.model.GroupDetailProjection;
import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import java.util.List;

public interface GroupReadPort {

  List<GroupWithLeaderProjection> readGroupsWithLeaderByUserId(String userId);

  GroupDetailProjection readGroupDetail(String userId, String groupId);

  GroupWithMemberUsers findGroupWithFcmTokenMember(String groupId);
}
