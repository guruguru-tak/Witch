package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.GroupMember;
import com.ssafy.witch.group.GroupMemberPort;
import com.ssafy.witch.mapper.group.GroupMemberMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class GroupMemberRepository implements GroupMemberPort {

  private final GroupMemberJpaRepository groupMemberJpaRepository;

  private final GroupMemberMapper mapper;

  @Override
  public GroupMember save(GroupMember groupMember) {
    return mapper.toDomain(groupMemberJpaRepository.save(mapper.toEntity(groupMember)));
  }

  @Override
  public boolean existsByUserIdAndGroupId(String userId, String groupId) {
    return groupMemberJpaRepository.existsByUserIdAndGroupId(userId, groupId);
  }

  @Override
  public List<GroupMember> findAllByGroupId(String groupId) {
    return groupMemberJpaRepository.findAllByGroupId(groupId)
        .stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public boolean isLeaderByUserIdAndGroupId(String userId, String groupId) {
    return groupMemberJpaRepository.isLeaderByUserIdAndGroupId(userId, groupId);
  }

  @Transactional
  @Override
  public void deleteMember(String userId, String groupId) {
    groupMemberJpaRepository.deleteByUserIdAndGroupId(userId, groupId);
  }

}
