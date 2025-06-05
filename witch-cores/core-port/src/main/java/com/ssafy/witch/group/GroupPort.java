package com.ssafy.witch.group;

import java.util.Optional;

public interface GroupPort {

  Group save(Group group);

  Optional<Group> findById(String groupId);

  boolean existsById(String groupId);

}
