package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.GroupMemberReadPort;
import com.ssafy.witch.group.model.GroupMemberProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GroupMemberReadRepository implements GroupMemberReadPort {

  private final GroupMemberJpaRepository groupMemberJpaRepository;

  @Override
  public List<GroupMemberProjection> readGroupMemberList(String groupId) {
    return groupMemberJpaRepository.readGroupMemberList(groupId);
  }
}
