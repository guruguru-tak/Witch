package com.ssafy.witch.group;

import com.ssafy.witch.group.model.GroupJoinRequestProjection;
import java.util.List;
import java.util.Optional;

public interface GroupJoinRequestPort {

  void save(GroupJoinRequest groupJoinRequest);

  boolean existsByUserIdAndGroupId(String userId, String groupId);

  Optional<GroupJoinRequest> findById(String groupJoinRequestId);

  void deleteById(String groupJoinRequestId);

  List<GroupJoinRequestProjection> readGroupJoinRequestsByGroupId(String groupId);
}
