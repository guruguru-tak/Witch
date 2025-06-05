package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.GroupNameDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupValidateService implements GroupValidateUseCase {

  private final ValidateGroupPort validateGroupPort;

  @Override
  public void checkGroupNameDuplication(String name) {
    if (validateGroupPort.isNameDuplicated(name)) {
      throw new GroupNameDuplicatedException();
    }
  }
}
