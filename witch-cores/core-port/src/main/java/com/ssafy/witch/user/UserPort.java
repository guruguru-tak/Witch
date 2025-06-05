package com.ssafy.witch.user;

import java.util.Optional;

public interface UserPort {

  User save(User user);

  Optional<User> findByEmail(String email);

  Optional<User> findById(String userId);
}
