package com.ssafy.witch.snack;

import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;

public interface SnackFileUseCase {

  PresignedUrl generateSnackFilePresignedUrl(GeneratePresignedUrlCommand command);
}
