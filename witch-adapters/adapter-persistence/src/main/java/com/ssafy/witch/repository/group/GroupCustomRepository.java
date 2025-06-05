package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.GroupWithMemberUsers;
import com.ssafy.witch.group.model.GroupDetailProjection;
import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import java.util.List;

public interface GroupCustomRepository {

  List<GroupWithLeaderProjection> findGroupListReadModelsByUserId(String userId);

  GroupDetailProjection readGroupDetail(String userId, String groupId);

  GroupWithMemberUsers readGroupWithMemberUsers(String groupId);
}
