package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.model.GroupJoinRequestProjection;
import java.util.List;

public interface GroupJoinRequestCustomRepository {

  List<GroupJoinRequestProjection> findGroupJoinRequestListReadModelsByGroupId(String groupId);
}
