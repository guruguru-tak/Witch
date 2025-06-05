package com.ssafy.witch.repository.group;

import com.ssafy.witch.entity.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJpaRepository extends JpaRepository<GroupEntity, String>,
    GroupCustomRepository {

  boolean existsByName(String name);

}
