package com.ssafy.witch.repository.group;

import com.ssafy.witch.entity.group.GroupJoinRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJoinRequestJpaRepository extends
    JpaRepository<GroupJoinRequestEntity, String>,
    GroupJoinRequestCustomRepository {

  boolean existsByUserIdAndGroupId(String userId, String groupId);

}
