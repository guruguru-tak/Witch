package com.ssafy.witch.file.command;

import com.ssafy.witch.validate.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GeneratePresignedUrlCommand extends SelfValidating<GeneratePresignedUrlCommand> {

  private final String userId;

  @NotBlank
  private final String fileName;

  public GeneratePresignedUrlCommand(String userId, String fileName) {
    this.userId = userId;
    this.fileName = fileName;

    this.validateSelf();
  }
}
