package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReadUserService implements ReadUserUseCase {

  private final UserPort userPort;

  @Override
  public User getUser(String userId) {
    return userPort.findById(userId).orElseThrow(UserNotFoundException::new);
  }
}
