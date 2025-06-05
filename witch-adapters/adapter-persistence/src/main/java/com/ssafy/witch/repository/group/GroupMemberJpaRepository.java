package com.ssafy.witch.repository.group;

import com.ssafy.witch.entity.group.GroupMemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GroupMemberJpaRepository extends JpaRepository<GroupMemberEntity, String>,
    GroupMemberCustomRepository {

  boolean existsByUserIdAndGroupId(String userId, String groupId);

  Optional<GroupMemberEntity> findByUserIdAndGroupId(String userId, String groupId);

  List<GroupMemberEntity> findAllByGroupId(String groupId);

  @Transactional
  void deleteByUserIdAndGroupId(String userId, String groupId);
}
