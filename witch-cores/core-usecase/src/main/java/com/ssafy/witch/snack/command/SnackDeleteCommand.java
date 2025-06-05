package com.ssafy.witch.snack.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackDeleteCommand {

  private final String userId;
  private final String SnackId;
}
